package com.latmod.yabba.block;

import com.feed_the_beast.ftblib.lib.item.ItemEntryWithCount;
import com.feed_the_beast.ftblib.lib.util.InvUtils;
import com.latmod.yabba.YabbaGuiHandler;
import com.latmod.yabba.net.MessageAntibarrelUpdate;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.util.AntibarrelData;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * @author LatvianModder
 */
public class BlockAntibarrel extends BlockYabba
{
	public BlockAntibarrel(String id)
	{
		super(id, Material.ROCK, MapColor.NETHERRACK);
		setHardness(6F);
		setResistance(1000F);
	}

	@Override
	public boolean dropSpecial(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileAntibarrel();
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side != EnumFacing.DOWN;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof TileAntibarrel)
			{
				if (player.isSneaking() && player.getHeldItem(hand).isEmpty())
				{
					ItemStack stack = new ItemStack(this);
					AntibarrelData.get(stack).copyFrom(((TileAntibarrel) tileEntity).contents);
					ItemHandlerHelper.giveItemToPlayer(player, stack, player.inventory.currentItem);
					world.removeTileEntity(pos);
					world.setBlockToAir(pos);
				}
				else
				{
					new MessageAntibarrelUpdate((TileAntibarrel) tileEntity).sendTo((EntityPlayerMP) player);
					YabbaGuiHandler.ANTIBARREL.open(player, pos);
				}
			}
		}

		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileAntibarrel)
		{
			AntibarrelData data = ((TileAntibarrel) tileEntity).contents;

			for (ItemEntryWithCount entry : data.items.values())
			{
				ItemStack stack = entry.getStack(true);
				stack.setCount(1);

				for (int i = 0; i < entry.count; i++)
				{
					InvUtils.dropItem(world, pos, stack.copy(), 20);
				}
			}
		}

		super.breakBlock(world, pos, state);
	}
}