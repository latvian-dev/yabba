package com.latmod.yabba;

import com.latmod.yabba.block.BlockAntibarrel;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.item.ItemHammer;
import com.latmod.yabba.item.ItemPainter;
import com.latmod.yabba.item.ItemUpgrade;

/**
 * @author LatvianModder
 */
public class YabbaItems
{
	public static final ItemUpgrade UPGRADE = new ItemUpgrade();
	public static final ItemPainter PAINTER = new ItemPainter();
	public static final ItemHammer HAMMER = new ItemHammer();

	public static final BlockBarrel BARREL = new BlockBarrel();
	public static final BlockAntibarrel ANTIBARREL = new BlockAntibarrel();
}