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
package org.kryptonmc.krypton.network.netty

import com.velocitypowered.natives.encryption.VelocityCipher
import com.velocitypowered.natives.util.MoreByteBufUtils
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import org.kryptonmc.krypton.util.logger

/**
 * Decrypts packets using a stream cipher provided by the specified [cipher].
 *
 * Thanks Velocity for the native stuff! :)
 */
class PacketDecrypter(private val cipher: VelocityCipher) : MessageToMessageDecoder<ByteBuf>() {

    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        val compatible = MoreByteBufUtils.ensureCompatible(ctx.alloc(), cipher, msg).slice()
        try {
            cipher.process(compatible)
            out.add(compatible)
            LOGGER.debug("Decrypted bytes length ${compatible.readableBytes()}")
        } catch (exception: Exception) {
            compatible.release()
            throw exception
        }
    }

    companion object {

        const val NETTY_NAME = "decrypter"
        private val LOGGER = logger<PacketDecrypter>()
    }
}
