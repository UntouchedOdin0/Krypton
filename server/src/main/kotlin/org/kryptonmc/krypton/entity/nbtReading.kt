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
package org.kryptonmc.krypton.entity

import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.ListTag
import org.spongepowered.math.vector.Vector3f

fun CompoundTag.getRotation(key: String): Vector3f? {
    if (!contains(key, ListTag.ID)) return null
    val list = getList(key, FloatTag.ID)
    return Vector3f(list.getFloat(0), list.getFloat(1), list.getFloat(2))
}
