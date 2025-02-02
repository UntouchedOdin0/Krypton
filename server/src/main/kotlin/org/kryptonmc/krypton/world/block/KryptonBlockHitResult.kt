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
package org.kryptonmc.krypton.world.block

import org.kryptonmc.api.block.BlockHitResult
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.HitResult
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class KryptonBlockHitResult(
    override val clickLocation: Vector3d,
    override val type: HitResult.Type,
    override val position: Vector3i,
    override val direction: Direction,
    override val isInside: Boolean
) : BlockHitResult {

    object Factory : BlockHitResult.Factory {

        override fun of(
            clickLocation: Vector3d,
            position: Vector3i,
            direction: Direction,
            missed: Boolean,
            isInside: Boolean
        ): BlockHitResult = KryptonBlockHitResult(
            clickLocation,
            if (missed) HitResult.Type.MISS else HitResult.Type.BLOCK,
            position,
            direction,
            isInside
        )
    }
}
