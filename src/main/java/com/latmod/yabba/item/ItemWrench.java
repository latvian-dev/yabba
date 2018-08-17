package com.latmod.yabba.item;

import com.feed_the_beast.ftblib.lib.tile.IItemWritableTile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
	@SuppressWarnings("deprecation")
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (player.isSneaking())
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof IItemWritableTile)
			{
				IBlockState state = world.getBlockState(pos);
				ItemStack drop = state.getBlock().getItem(world, pos, state);

				if (!drop.isEmpty())
				{
					if (!world.isRemote)
					{
						((IItemWritableTile) tileEntity).writeToItem(drop);
						world.setBlockToAir(pos);
						Block.spawnAsEntity(world, pos, drop);
					}

					return EnumActionResult.SUCCESS;
				}
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