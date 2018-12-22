package com.latmod.yabba.block;

import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class BlockItemBarrel extends BlockBarrel
{
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileItemBarrel();
	}
}