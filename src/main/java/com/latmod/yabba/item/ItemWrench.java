package com.latmod.yabba.item;

import com.latmod.yabba.block.BlockDecorativeBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class ItemWrench extends Item
{
	public ItemWrench()
	{
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (player.isSneaking())
		{
			if (world.getBlockState(pos).getBlock() instanceof BlockDecorativeBlock)
			{
				if (!world.isRemote)
				{
					world.setBlockToAir(pos);
				}

				return EnumActionResult.SUCCESS;
			}
		}
		else
		{
			world.getBlockState(pos).getBlock().rotateBlock(world, pos, facing);
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}
}