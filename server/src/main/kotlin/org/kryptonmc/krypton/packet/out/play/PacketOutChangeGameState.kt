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
import org.kryptonmc.krypton.packet.Packet

@JvmRecord
data class PacketOutChangeGameState(
    val state: GameState,
    val value: Float = 0.0F
) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeByte(state.ordinal)
        buf.writeFloat(value)
    }
}

enum class GameState {

    NO_RESPAWN_BLOCK_AVAILABLE,
    END_RAINING,
    BEGIN_RAINING,
    CHANGE_GAMEMODE,
    WIN_GAME,
    DEMO_EVENT,
    ARROW_HIT_PLAYER,
    RAIN_LEVEL_CHANGE,
    THUNDER_LEVEL_CHANGE,
    PLAY_PUFFERFISH_STING_SOUND,
    PLAY_ELDER_GUARDIAN_MOB_APPEARANCE,
    ENABLE_RESPAWN_SCREEN;

    companion object {

        private val BY_ID = values()

        @JvmStatic
        fun fromId(id: Int): GameState? = BY_ID.getOrNull(id)
    }
}
