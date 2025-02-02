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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarLong
import org.kryptonmc.krypton.world.KryptonWorldBorder

@JvmRecord
data class PacketOutInitializeWorldBorder(override val border: KryptonWorldBorder) : WorldBorderPacket {

    override fun write(buf: ByteBuf) {
        buf.writeDouble(border.center.x())
        buf.writeDouble(border.center.y())
        buf.writeDouble(border.size)
        buf.writeDouble(border.size)
        buf.writeVarLong(0)
        buf.writeVarInt(PORTAL_TELEPORT_BOUNDARY)
        buf.writeVarInt(border.warningBlocks)
        buf.writeVarInt(border.warningTime)
    }

    companion object {

        private const val PORTAL_TELEPORT_BOUNDARY = 29_999_984
    }
}
