package com.latmod.yabba.item;

import com.latmod.yabba.Yabba;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author LatvianModder
 */
@GameRegistry.ObjectHolder(Yabba.MOD_ID)
public class YabbaItems
{
	public static final Block BARREL = Blocks.AIR;
	public static final Block ANTIBARREL = Blocks.AIR;

	public static final Item UPGRADE = Items.AIR;
	public static final Item PAINTER = Items.AIR;
	public static final Item HAMMER = Items.AIR;
}