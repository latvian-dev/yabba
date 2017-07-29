package com.latmod.yabba.api;

import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.latmod.yabba.block.BlockBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * @author LatvianModder
 */
public class BarrelModelCommonData
{
	public static final BarrelModelCommonData DEFAULT = new BarrelModelCommonData();

	public static class Panel extends BarrelModelCommonData
	{
		private final AxisAlignedBB[] boxes;

		public Panel(float height)
		{
			boxes = MathUtils.getRotatedBoxes(new AxisAlignedBB(0D, 1D - height, 0D, 1D, 1D, 1D));
		}

		@Override
		public AxisAlignedBB getAABB(IBlockState state, IBlockAccess world, BlockPos pos, Barrel barrel)
		{
			return boxes[BlockBarrel.normalizeFacing(state).getIndex()];
		}
	}

	public AxisAlignedBB getAABB(IBlockState state, IBlockAccess world, BlockPos pos, Barrel barrel)
	{
		return Block.FULL_BLOCK_AABB;
	}
}