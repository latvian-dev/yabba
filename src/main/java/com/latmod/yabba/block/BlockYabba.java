package com.latmod.yabba.block;

import com.feed_the_beast.ftblib.lib.block.BlockBase;
import com.latmod.yabba.Yabba;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * @author LatvianModder
 */
public class BlockYabba extends BlockBase
{
	public BlockYabba(String id, Material material, MapColor color)
	{
		super(Yabba.MOD_ID, id, material, color);
		setCreativeTab(Yabba.TAB);
	}
}