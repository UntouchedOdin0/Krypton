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
package org.kryptonmc.krypton.registry

import com.google.common.collect.HashBiMap
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.resource.elementKey
import org.kryptonmc.krypton.util.IdMap
import org.kryptonmc.krypton.util.KEY_CODEC
import org.kryptonmc.krypton.util.identityStrategy
import java.util.OptionalInt
import java.util.stream.Stream
import kotlin.math.max

open class KryptonRegistry<T : Any>(override val key: ResourceKey<out Registry<T>>) : Registry<T>, Codec<T>, Keyable, IdMap<T> {

    @Suppress("MagicNumber")
    private val byId = ObjectArrayList<T>(256)
    private val toId = Object2IntOpenCustomHashMap<T>(identityStrategy())
    private val storage = HashBiMap.create<Key, T>()
    private val keyStorage = HashBiMap.create<ResourceKey<T>, T>()

    private var nextId = 0

    override fun <V : T> register(key: ResourceKey<T>, value: V) = register(nextId, key, value)

    @Suppress("ReplacePutWithAssignment") // We want to use fastutil's specialised no unboxing put
    override fun <V : T> register(id: Int, key: ResourceKey<T>, value: V): V {
        byId.size(max(byId.size, id + 1))
        byId[id] = value
        toId.put(value, id)
        storage[key.location] = value
        keyStorage[key] = value
        if (nextId <= id) nextId = id + 1
        return value
    }

    fun <V : T> registerOrOverride(optionalId: OptionalInt, key: ResourceKey<T>, value: V): V {
        val existing = keyStorage[key]
        val id = if (existing == null) {
            if (optionalId.isPresent) optionalId.asInt else nextId
        } else {
            toId.getInt(existing).apply {
                if (optionalId.isPresent && optionalId.asInt != this) error("ID mismatch")
                toId.removeInt(this)
            }
        }
        return register(id, key, value)
    }

    override fun get(key: Key) = storage[key]

    override fun get(id: Int) = if (id in byId.indices) byId[id] else null

    override fun get(value: T) = storage.inverse()[value]

    override fun get(key: ResourceKey<T>) = keyStorage[key]

    override fun resourceKey(value: T) = keyStorage.inverse()[value]

    override fun idOf(value: T) = toId.getInt(value)

    override fun contains(key: Key) = key in storage

    override fun containsKey(key: ResourceKey<T>) = key in keyStorage

    override fun containsValue(value: T) = storage.containsValue(value)

    override fun isEmpty() = storage.isEmpty()

    override fun iterator() = storage.values.iterator()

    override fun <U> encode(input: T, ops: DynamicOps<U>, prefix: U): DataResult<U> {
        val key = get(input) ?: return DataResult.error("Unknown registry element $input!")
        return ops.mergeToPrimitive(prefix, if (ops.compressMaps()) ops.createInt(idOf(input)) else ops.createString(key.asString()))
    }

    override fun <U> decode(ops: DynamicOps<U>, input: U): DataResult<Pair<T, U>> = if (ops.compressMaps()) ops.getNumberValue(input).flatMap { id ->
        get(id.toInt())?.let { DataResult.success(it) } ?: DataResult.error("Unknown registry ID $id!")
    }.map { Pair.of(it, ops.empty()) } else KEY_CODEC.decode(ops, input).flatMap { pair ->
        get(pair.first)?.let { DataResult.success(Pair.of(it, pair.second)) } ?: DataResult.error("Unknown registry key ${pair.first}")
    }

    override fun <U> keys(ops: DynamicOps<U>): Stream<U> = keySet.stream().map { ops.createString(it.asString()) }

    override val keySet: Set<Key>
        get() = storage.keys

    override val entries: Set<Map.Entry<ResourceKey<T>, T>>
        get() = keyStorage.entries

    override val keys: Set<ResourceKey<T>>
        get() = keyStorage.keys

    override val values: Collection<T>
        get() = storage.values

    override val size: Int
        get() = storage.size

    companion object {

        fun <T : Any> ResourceKey<out Registry<T>>.directCodec(elementCodec: Codec<T>): Codec<KryptonRegistry<T>> = Codec.unboundedMap(KEY_CODEC.xmap({ ResourceKey.of(this, it) }, ResourceKey<*>::location), elementCodec).xmap(
            { map -> KryptonRegistry(this).apply { map.forEach { (k, v) -> register(k as ResourceKey<T>, v) } } },
            { it.keyStorage.toMap() }
        )
    }
}

fun <T : Any> ResourceKey<out Registry<T>>.networkCodec(elementCodec: Codec<T>): Codec<KryptonRegistry<T>> = withNameAndId(elementCodec.fieldOf("element")).codec().listOf().xmap(
    { list -> KryptonRegistry(this).apply { list.forEach { register(it.id, it.key, it.value) } } },
    { registry -> registry.values.map { RegistryEntry(registry.resourceKey(it)!!, registry.idOf(it), it) } }
)

private fun <T : Any> ResourceKey<out Registry<T>>.withNameAndId(elementCodec: MapCodec<T>): MapCodec<RegistryEntry<T>> = RecordCodecBuilder.mapCodec {
    it.group(
        KEY_CODEC.xmap(elementKey(), ResourceKey<*>::location).fieldOf("name").forGetter(RegistryEntry<T>::key),
        Codec.INT.fieldOf("id").forGetter(RegistryEntry<T>::id),
        elementCodec.forGetter(RegistryEntry<T>::value)
    ).apply(it, ::RegistryEntry)
}

data class RegistryEntry<T : Any>(
    val key: ResourceKey<T>,
    val id: Int,
    val value: T
)
