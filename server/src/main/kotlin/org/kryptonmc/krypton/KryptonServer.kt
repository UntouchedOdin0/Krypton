/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton

import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.Server
import org.kryptonmc.api.event.server.ServerStartEvent
import org.kryptonmc.api.event.server.ServerStopEvent
import org.kryptonmc.api.event.server.TickEndEvent
import org.kryptonmc.api.event.server.TickStartEvent
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.KryptonProfileCache
import org.kryptonmc.krypton.command.KryptonCommandManager
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.config.category.ForwardingMode
import org.kryptonmc.krypton.console.KryptonConsole
import org.kryptonmc.krypton.item.KryptonItemManager
import org.kryptonmc.krypton.packet.PacketRegistry
import org.kryptonmc.krypton.packet.out.play.PacketOutTimeUpdate
import org.kryptonmc.krypton.plugin.KryptonEventManager
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.server.PlayerManager
import org.kryptonmc.krypton.server.ban.KryptonBanManager
import org.kryptonmc.krypton.server.whitelist.KryptonWhitelistManager
import org.kryptonmc.krypton.service.KryptonServicesManager
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.util.KryptonFactoryProvider
import org.kryptonmc.krypton.util.tryCreateDirectory
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.register
import org.kryptonmc.krypton.world.KryptonWorldManager
import org.kryptonmc.krypton.world.block.KryptonBlockManager
import org.kryptonmc.krypton.world.fluid.KryptonFluidManager
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.net.InetSocketAddress
import java.nio.file.Path
import java.util.Locale
import java.util.UUID
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.math.max
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

/**
 * This is the centre of operations here at Krypton Inc. Everything stems from
 * this class.
 */
class KryptonServer(
    val config: KryptonConfig,
    val useDataConverter: Boolean,
    override val profileCache: KryptonProfileCache,
    private val configPath: Path,
    worldFolder: Path
) : Server {

    override val platform = KryptonPlatform
    override val maxPlayers = config.status.maxPlayers
    override val motd = config.status.motd
    override val isOnline = config.server.onlineMode
    override val address = InetSocketAddress(config.server.ip, config.server.port)

    val playerManager = PlayerManager(this)
    override val players = playerManager.players

    override val console = KryptonConsole(this)
    override val scoreboard = KryptonScoreboard(this)

    override val worldManager = KryptonWorldManager(this, worldFolder)
    override val commandManager = KryptonCommandManager
    override val pluginManager = KryptonPluginManager
    override val eventManager = KryptonEventManager
    override val servicesManager = KryptonServicesManager
    override val registryManager = KryptonRegistryManager
    override val tagManager = KryptonTagManager
    override val blockManager = KryptonBlockManager
    override val itemManager = KryptonItemManager
    override val fluidManager = KryptonFluidManager
    override val scheduler = KryptonScheduler()
    override val factoryProvider = KryptonFactoryProvider
    override val banManager = KryptonBanManager(this)
    override val whitelistManager = KryptonWhitelistManager(this)

    @Volatile
    var isRunning = true
        private set

    // These help us keep track of how fast the server is running and how far
    // behind it is at any one time.
    private var lastTickTime = 0L
    private var lastOverloadWarning = 0L
    private var tickCount = 0
    private var oversleepFactor = 0L

    init {
        KryptonFactoryProvider.register<Scoreboard.Factory>(KryptonScoreboard.Factory(this))
    }

    // The order of loading here is pretty important, as some things depend on
    // others to function properly.
    fun start() {
        LOGGER.info("Starting Krypton server on ${config.server.ip}:${config.server.port}...")
        val startTime = System.nanoTime()

        LOGGER.debug("Loading packets...")
        PacketRegistry.bootstrap()
        LOGGER.debug("Registering commands...")
        commandManager.registerBuiltins()

        // Create server config lists here before we allow any players in.
        LOGGER.debug("Loading server config lists...")
        playerManager.bannedPlayers.validatePath()
        playerManager.whitelist.validatePath()
        playerManager.bannedIps.validatePath()
        playerManager.ops.validatePath()
        playerManager.whitelistedIps.validatePath()

        // Start the metrics system.
        LOGGER.debug("Starting bStats metrics")
        KryptonMetrics.initialize(this, config.other.metrics)

        // Warn about piracy being unsupported if proxy forwarding is not enabled, because video game piracy is bad.
        if (!config.server.onlineMode && config.proxy.mode == ForwardingMode.NONE) {
            LOGGER.warn("-----------------------------------------------------------------------------------")
            LOGGER.warn("THIS SERVER IS IN OFFLINE MODE! NO ATTEMPTS WILL BE MADE TO AUTHENTICATE USERS!")
            LOGGER.warn("While this may allow players without full Minecraft accounts to connect, it also allows hackers to connect with any " +
                    "username they choose! Beware!")
            LOGGER.warn("Please beware that connections made to the server will not be encrypted, meaning hackers could potentially intercept " +
                    "sensitive data!")
            LOGGER.warn("To get rid of this message, change \"online-mode\" to true in the configuration file")
            LOGGER.warn("-----------------------------------------------------------------------------------")
        }

        LOGGER.info("Preparing world ${config.world.name}...")
        worldManager.init()

        // Load plugins here because most of everything they need is available now.
        loadPlugins()

        // Fire the event that signals the server starting. We fire it here so that plugins can listen to it as part of their lifecycle,
        // and we call it sync so plugins can finish initialising before we do.
        eventManager.fireAndForgetSync(ServerStartEvent)

        // Initialize console permissions after plugins are loaded. We do this here so plugins have had a chance to register their listeners
        // so they can actually catch this event and set up their own permission providers for the console.
        console.setupPermissions()

        // Start accepting connections
        LOGGER.debug("Starting Netty...")
        NettyProcess.run(this)

        Runtime.getRuntime().addShutdownHook(Thread({ stop(false) }, "Shutdown Handler").apply { isDaemon = false })
        LOGGER.info("Done (${"%.3fs".format(Locale.ROOT, (System.nanoTime() - startTime) / 1.0E9)})! Type \"help\" for help.")

        LOGGER.debug("Starting console handler")
        Thread(console::start, "Console Handler").apply {
            uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, exception ->
                KryptonConsole.LOGGER.error("Caught previously unhandled exception ", exception)
            }
            isDaemon = true
        }.start()

        run()
    }

    fun run() = try {
        // Set the last tick time early (avoids initial check sending an overload warning every time)
        lastTickTime = System.currentTimeMillis()

        while (isRunning) {
            val nextTickTime = System.currentTimeMillis() - lastTickTime
            if (nextTickTime > 2000L && lastTickTime - lastOverloadWarning >= 15_000L) {
                LOGGER.warn("Can't keep up! Running $nextTickTime ms (${nextTickTime / 50} ticks) behind!")
                lastOverloadWarning = lastTickTime
            }

            eventManager.fireAndForgetSync(TickStartEvent(tickCount))
            val tickTime = measureTimeMillis(::tick)

            val finishTime = System.currentTimeMillis()
            eventManager.fireAndForgetSync(TickEndEvent(tickCount, tickTime, finishTime))
            lastTickTime = finishTime

            // This logic ensures that ticking isn't delayed by the overhead from Thread.sleep, which seems to be up to 10 ms,
            // which is enough that the average TPS would be reporting at around 15-16, rather than 20.
            val sleepTime = measureTimeMillis { Thread.sleep(max(0, MILLISECONDS_PER_TICK - tickTime - oversleepFactor)) }
            oversleepFactor = sleepTime - (MILLISECONDS_PER_TICK - tickTime)
        }
    } catch (exception: Throwable) { // This is hacky, but ensures that we catch absolutely everything that may be thrown here.
        LOGGER.error("Encountered an unexpected exception", exception)
        stop()
    }

    private fun loadPlugins() {
        LOGGER.info("Loading plugins...")
        try {
            val pluginPath = Path.of("plugins")
            if (!pluginPath.exists()) pluginPath.tryCreateDirectory()
            if (!pluginPath.isDirectory()) {
                LOGGER.warn("Plugin path $pluginPath is not a directory! Plugins will not be loaded!")
                return
            }

            pluginManager.loadPlugins(pluginPath, this)
        } catch (exception: Exception) {
            LOGGER.error("Failed to load plugins!", exception)
        }

        // Register all of the plugin instances as event listeners, so that plugins can listen for events such as
        // ServerStartEvent and ServerStopEvent in their main class.
        pluginManager.plugins.forEach {
            val instance = it.instance ?: return@forEach
            try {
                eventManager.registerUnchecked(it, instance)
            } catch (exception: Exception) {
                LOGGER.error("Unable to register plugin listener for plugin ${it.description.name}!", exception)
            }
        }

        LOGGER.info("Finished plugin loading! Loaded ${pluginManager.plugins.size} plugins.")
    }

    private fun tick() {
        tickCount++

        val time = System.currentTimeMillis()
        playerManager.tick(time)
        if (playerManager.players.isEmpty()) return // don't tick if there are no players on the server

        worldManager.worlds.forEach { (_, world) ->
            if (tickCount % TICKS_PER_SECOND == 0) {
                playerManager.sendToAll(PacketOutTimeUpdate(
                    world.data.time,
                    world.data.dayTime,
                    world.data.gameRules[GameRules.DO_DAYLIGHT_CYCLE]
                ), world)
            }
            world.tick()
        }
        if (config.world.autosaveInterval > 0 && tickCount % config.world.autosaveInterval == 0) {
            LOGGER.info("Auto save started.")
            worldManager.saveAll()
            LOGGER.info("Auto save finished.")
        }
        if (tickCount % SAVE_PROFILE_CACHE_INTERVAL == 0) {
            LOGGER.debug("Saving authenticated user cache...")
            profileCache.save()
        }
    }

    fun updateConfig() {
        val config = HoconConfigurationLoader.builder()
            .path(configPath)
            .defaultOptions(KryptonConfig.OPTIONS)
            .build()
        val node = config.load().set(this.config)
        config.save(node)
    }

    override fun player(uuid: UUID) = playerManager.playersByUUID[uuid]

    override fun player(name: String) = playerManager.playersByName[name]

    override fun sendMessage(message: Component, permission: String) {
        playerManager.players.forEach { if (it.hasPermission(permission)) it.sendMessage(message) }
        console.sendMessage(message)
        return
    }

    override fun audiences() = players + console

    fun getPermissionLevel(profile: KryptonGameProfile): Int {
        if (playerManager.ops.contains(profile)) return playerManager.ops[profile]?.permissionLevel ?: 1
        return 1
    }

    fun stop(explicitExit: Boolean = true) {
        if (!isRunning) return // Ensure we cannot accidentally run this twice

        val shutdownProcess = Runnable {
            // Stop server and shut down session manager (disconnecting all players)
            LOGGER.info("Starting shutdown for Krypton version ${KryptonPlatform.version}...")
            isRunning = false
            playerManager.disconnectAll()
            NettyProcess.shutdown()

            // Save data
            LOGGER.info("Saving player, world, and region data...")
            worldManager.saveAll()
            playerManager.saveAll()

            // Shut down plugins and unregister listeners
            LOGGER.info("Shutting down plugins and unregistering listeners...")
            eventManager.fireAndForgetSync(ServerStopEvent)
            eventManager.shutdown()

            // Shut down scheduler
            scheduler.shutdown()
            LOGGER.info("Goodbye")

            // Manually shut down Log4J 2 here so it doesn't shut down before we've finished logging
            LogManager.shutdown()
            if (explicitExit) exitProcess(0)
        }

        if (explicitExit) {
            val thread = Thread(shutdownProcess)
            thread.start()
        } else {
            shutdownProcess.run()
        }
    }

    fun restart() {
        stop()
        val split = config.other.restartScript.split(" ")
        if (split.isNotEmpty()) {
            if (!Path.of(split[0]).isRegularFile()) {
                println("Unable to find restart script ${split[0]}! Refusing to restart!")
                return
            }
            println("Attempting to restart the server with script ${split[0]}...")
            val os = System.getProperty("os.name").lowercase()
            val runCommand = if (os.contains("win")) "cmd /c start " else "sh "
            Runtime.getRuntime().exec(runCommand + config.other.restartScript)
        }
    }

    companion object {

        private const val MILLISECONDS_PER_TICK = 50L // milliseconds in a tick
        private const val TICKS_PER_SECOND = 20
        private const val SAVE_PROFILE_CACHE_INTERVAL = 600
        val LOGGER = logger<KryptonServer>()
    }
}
