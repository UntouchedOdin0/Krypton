/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

/**
 * Represents something that holds attributes.
 */
interface Attributable {

    /**
     * All attributes currently stored for this attributable.
     */
    val attributes: Map<AttributeType, Attribute>

    /**
     * Gets the attribute for the specified [type], or returns null if there
     * is no attribute for the given [type].
     *
     * @param type the type of the attribute
     * @return the attribute, or null if not present
     */
    fun getAttribute(type: AttributeType): Attribute? = attributes[type]
}
