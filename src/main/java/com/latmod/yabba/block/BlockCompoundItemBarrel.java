package com.latmod.yabba.block;

import com.latmod.yabba.tile.TileCompoundItemBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class BlockCompoundItemBarrel extends BlockCompoundBarrelBase
{
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileCompoundItemBarrel();
	}
}
