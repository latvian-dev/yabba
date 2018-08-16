package com.latmod.yabba;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author LatvianModder
 */
@GameRegistry.ObjectHolder(Yabba.MOD_ID)
@Mod.EventBusSubscriber(modid = Yabba.MOD_ID)
public class YabbaItems
{
	public static final Block ITEM_BARREL = Blocks.AIR;
	public static final Block ITEM_BARREL_CONNECTOR = Blocks.AIR;
	public static final Block ANTIBARREL = Blocks.AIR;
	public static final Block COMPOUND_ITEM_BARREL = Blocks.AIR;
	public static final Block DECORATIVE_BLOCK = Blocks.AIR;

	public static final Item UPGRADE_BLANK = Items.AIR;
	public static final Item UPGRADE_STONE_TIER = Items.AIR;
	public static final Item UPGRADE_IRON_TIER = Items.AIR;
	public static final Item UPGRADE_GOLD_TIER = Items.AIR;
	public static final Item UPGRADE_DIAMOND_TIER = Items.AIR;
	public static final Item UPGRADE_STAR_TIER = Items.AIR;
	public static final Item UPGRADE_CREATIVE = Items.AIR;
	public static final Item UPGRADE_OBSIDIAN_SHELL = Items.AIR;
	public static final Item UPGRADE_REDSTONE_OUT = Items.AIR;
	public static final Item UPGRADE_HOPPER = Items.AIR;
	public static final Item UPGRADE_VOID = Items.AIR;
	public static final Item UPGRADE_PICKUP = Items.AIR;

	public static final Item HAMMER = Items.AIR;
	public static final Item PAINTER = Items.AIR;

	@GameRegistry.ObjectHolder(Yabba.MOD_ID + ":item_barrel")
	public static final Item ITEM_BARREL_ITEM = Items.AIR;

	@GameRegistry.ObjectHolder(Yabba.MOD_ID + ":decorative_block")
	public static final Item DECORATIVE_BLOCK_ITEM = Items.AIR;
}