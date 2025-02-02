/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import kotlin.String
import kotlin.Suppress
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Tag::class)
public object BlockTags {

    // @formatter:off
    @JvmField public val WOOL: Tag<Block> = get("wool")
    @JvmField public val PLANKS: Tag<Block> = get("planks")
    @JvmField public val STONE_BRICKS: Tag<Block> = get("stone_bricks")
    @JvmField public val WOODEN_BUTTONS: Tag<Block> = get("wooden_buttons")
    @JvmField public val BUTTONS: Tag<Block> = get("buttons")
    @JvmField public val CARPETS: Tag<Block> = get("carpets")
    @JvmField public val WOODEN_DOORS: Tag<Block> = get("wooden_doors")
    @JvmField public val WOODEN_STAIRS: Tag<Block> = get("wooden_stairs")
    @JvmField public val WOODEN_SLABS: Tag<Block> = get("wooden_slabs")
    @JvmField public val WOODEN_FENCES: Tag<Block> = get("wooden_fences")
    @JvmField public val PRESSURE_PLATES: Tag<Block> = get("pressure_plates")
    @JvmField public val WOODEN_PRESSURE_PLATES: Tag<Block> = get("wooden_pressure_plates")
    @JvmField public val STONE_PRESSURE_PLATES: Tag<Block> = get("stone_pressure_plates")
    @JvmField public val WOODEN_TRAPDOORS: Tag<Block> = get("wooden_trapdoors")
    @JvmField public val DOORS: Tag<Block> = get("doors")
    @JvmField public val SAPLINGS: Tag<Block> = get("saplings")
    @JvmField public val LOGS_THAT_BURN: Tag<Block> = get("logs_that_burn")
    @JvmField public val LOGS: Tag<Block> = get("logs")
    @JvmField public val DARK_OAK_LOGS: Tag<Block> = get("dark_oak_logs")
    @JvmField public val OAK_LOGS: Tag<Block> = get("oak_logs")
    @JvmField public val BIRCH_LOGS: Tag<Block> = get("birch_logs")
    @JvmField public val ACACIA_LOGS: Tag<Block> = get("acacia_logs")
    @JvmField public val JUNGLE_LOGS: Tag<Block> = get("jungle_logs")
    @JvmField public val SPRUCE_LOGS: Tag<Block> = get("spruce_logs")
    @JvmField public val CRIMSON_STEMS: Tag<Block> = get("crimson_stems")
    @JvmField public val WARPED_STEMS: Tag<Block> = get("warped_stems")
    @JvmField public val BANNERS: Tag<Block> = get("banners")
    @JvmField public val SAND: Tag<Block> = get("sand")
    @JvmField public val STAIRS: Tag<Block> = get("stairs")
    @JvmField public val SLABS: Tag<Block> = get("slabs")
    @JvmField public val WALLS: Tag<Block> = get("walls")
    @JvmField public val ANVIL: Tag<Block> = get("anvil")
    @JvmField public val RAILS: Tag<Block> = get("rails")
    @JvmField public val LEAVES: Tag<Block> = get("leaves")
    @JvmField public val TRAPDOORS: Tag<Block> = get("trapdoors")
    @JvmField public val SMALL_FLOWERS: Tag<Block> = get("small_flowers")
    @JvmField public val BEDS: Tag<Block> = get("beds")
    @JvmField public val FENCES: Tag<Block> = get("fences")
    @JvmField public val TALL_FLOWERS: Tag<Block> = get("tall_flowers")
    @JvmField public val FLOWERS: Tag<Block> = get("flowers")
    @JvmField public val PIGLIN_REPELLENTS: Tag<Block> = get("piglin_repellents")
    @JvmField public val GOLD_ORES: Tag<Block> = get("gold_ores")
    @JvmField public val IRON_ORES: Tag<Block> = get("iron_ores")
    @JvmField public val DIAMOND_ORES: Tag<Block> = get("diamond_ores")
    @JvmField public val REDSTONE_ORES: Tag<Block> = get("redstone_ores")
    @JvmField public val LAPIS_ORES: Tag<Block> = get("lapis_ores")
    @JvmField public val COAL_ORES: Tag<Block> = get("coal_ores")
    @JvmField public val EMERALD_ORES: Tag<Block> = get("emerald_ores")
    @JvmField public val COPPER_ORES: Tag<Block> = get("copper_ores")
    @JvmField public val NON_FLAMMABLE_WOOD: Tag<Block> = get("non_flammable_wood")
    @JvmField public val CANDLES: Tag<Block> = get("candles")
    @JvmField public val DIRT: Tag<Block> = get("dirt")
    @JvmField public val FLOWER_POTS: Tag<Block> = get("flower_pots")
    @JvmField public val ENDERMAN_HOLDABLE: Tag<Block> = get("enderman_holdable")
    @JvmField public val ICE: Tag<Block> = get("ice")
    @JvmField public val VALID_SPAWN: Tag<Block> = get("valid_spawn")
    @JvmField public val IMPERMEABLE: Tag<Block> = get("impermeable")
    @JvmField public val UNDERWATER_BONEMEALS: Tag<Block> = get("underwater_bonemeals")
    @JvmField public val CORAL_BLOCKS: Tag<Block> = get("coral_blocks")
    @JvmField public val WALL_CORALS: Tag<Block> = get("wall_corals")
    @JvmField public val CORAL_PLANTS: Tag<Block> = get("coral_plants")
    @JvmField public val CORALS: Tag<Block> = get("corals")
    @JvmField public val BAMBOO_PLANTABLE_ON: Tag<Block> = get("bamboo_plantable_on")
    @JvmField public val STANDING_SIGNS: Tag<Block> = get("standing_signs")
    @JvmField public val WALL_SIGNS: Tag<Block> = get("wall_signs")
    @JvmField public val SIGNS: Tag<Block> = get("signs")
    @JvmField public val DRAGON_IMMUNE: Tag<Block> = get("dragon_immune")
    @JvmField public val WITHER_IMMUNE: Tag<Block> = get("wither_immune")
    @JvmField public val WITHER_SUMMON_BASE_BLOCKS: Tag<Block> = get("wither_summon_base_blocks")
    @JvmField public val BEEHIVES: Tag<Block> = get("beehives")
    @JvmField public val CROPS: Tag<Block> = get("crops")
    @JvmField public val BEE_GROWABLES: Tag<Block> = get("bee_growables")
    @JvmField public val PORTALS: Tag<Block> = get("portals")
    @JvmField public val FIRE: Tag<Block> = get("fire")
    @JvmField public val NYLIUM: Tag<Block> = get("nylium")
    @JvmField public val WART_BLOCKS: Tag<Block> = get("wart_blocks")
    @JvmField public val BEACON_BASE_BLOCKS: Tag<Block> = get("beacon_base_blocks")
    @JvmField public val SOUL_SPEED_BLOCKS: Tag<Block> = get("soul_speed_blocks")
    @JvmField public val WALL_POST_OVERRIDE: Tag<Block> = get("wall_post_override")
    @JvmField public val CLIMBABLE: Tag<Block> = get("climbable")
    @JvmField public val SHULKER_BOXES: Tag<Block> = get("shulker_boxes")
    @JvmField public val HOGLIN_REPELLENTS: Tag<Block> = get("hoglin_repellents")
    @JvmField public val SOUL_FIRE_BASE_BLOCKS: Tag<Block> = get("soul_fire_base_blocks")
    @JvmField public val STRIDER_WARM_BLOCKS: Tag<Block> = get("strider_warm_blocks")
    @JvmField public val CAMPFIRES: Tag<Block> = get("campfires")
    @JvmField public val GUARDED_BY_PIGLINS: Tag<Block> = get("guarded_by_piglins")
    @JvmField public val PREVENT_MOB_SPAWNING_INSIDE: Tag<Block> = get("prevent_mob_spawning_inside")
    @JvmField public val FENCE_GATES: Tag<Block> = get("fence_gates")
    @JvmField public val UNSTABLE_BOTTOM_CENTER: Tag<Block> = get("unstable_bottom_center")
    @JvmField public val MUSHROOM_GROW_BLOCK: Tag<Block> = get("mushroom_grow_block")
    @JvmField public val INFINIBURN_OVERWORLD: Tag<Block> = get("infiniburn_overworld")
    @JvmField public val INFINIBURN_NETHER: Tag<Block> = get("infiniburn_nether")
    @JvmField public val INFINIBURN_END: Tag<Block> = get("infiniburn_end")
    @JvmField public val BASE_STONE_OVERWORLD: Tag<Block> = get("base_stone_overworld")
    @JvmField public val STONE_ORE_REPLACEABLES: Tag<Block> = get("stone_ore_replaceables")
    @JvmField public val DEEPSLATE_ORE_REPLACEABLES: Tag<Block> = get("deepslate_ore_replaceables")
    @JvmField public val BASE_STONE_NETHER: Tag<Block> = get("base_stone_nether")
    @JvmField public val CANDLE_CAKES: Tag<Block> = get("candle_cakes")
    @JvmField public val CAULDRONS: Tag<Block> = get("cauldrons")
    @JvmField public val CRYSTAL_SOUND_BLOCKS: Tag<Block> = get("crystal_sound_blocks")
    @JvmField public val INSIDE_STEP_SOUND_BLOCKS: Tag<Block> = get("inside_step_sound_blocks")
    @JvmField public val OCCLUDES_VIBRATION_SIGNALS: Tag<Block> = get("occludes_vibration_signals")
    @JvmField public val DRIPSTONE_REPLACEABLE: Tag<Block> = get("dripstone_replaceable_blocks")
    @JvmField public val CAVE_VINES: Tag<Block> = get("cave_vines")
    @JvmField public val MOSS_REPLACEABLE: Tag<Block> = get("moss_replaceable")
    @JvmField public val LUSH_GROUND_REPLACEABLE: Tag<Block> = get("lush_ground_replaceable")
    @JvmField public val SMALL_DRIPLEAF_PLACEABLE: Tag<Block> = get("small_dripleaf_placeable")
    @JvmField public val SNOW: Tag<Block> = get("snow")
    @JvmField public val MINEABLE_WITH_AXE: Tag<Block> = get("mineable/axe")
    @JvmField public val MINEABLE_WITH_HOE: Tag<Block> = get("mineable/hoe")
    @JvmField public val MINEABLE_WITH_PICKAXE: Tag<Block> = get("mineable/pickaxe")
    @JvmField public val MINEABLE_WITH_SHOVEL: Tag<Block> = get("mineable/shovel")
    @JvmField public val NEEDS_DIAMOND_TOOL: Tag<Block> = get("needs_diamond_tool")
    @JvmField public val NEEDS_IRON_TOOL: Tag<Block> = get("needs_iron_tool")
    @JvmField public val NEEDS_STONE_TOOL: Tag<Block> = get("needs_stone_tool")
    @JvmField public val FEATURES_CANNOT_REPLACE: Tag<Block> = get("features_cannot_replace")
    @JvmField public val LAVA_POOL_STONE_CANNOT_REPLACE: Tag<Block> = get("lava_pool_stone_replaceables")
    @JvmField public val GEODE_INVALID_BLOCKS: Tag<Block> = get("geode_invalid_blocks")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Tag<Block> = Krypton.tagManager[TagTypes.BLOCKS, "minecraft:$key"]!!
}
