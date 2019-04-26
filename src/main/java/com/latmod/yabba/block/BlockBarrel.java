package com.latmod.yabba.block;

import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.UpgradeData;
import com.latmod.yabba.item.ItemHammer;
import com.latmod.yabba.item.ItemPainter;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * @author LatvianModder
 */
public class BlockBarrel extends BlockDecorativeBlock
{
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileBarrel();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		list.add(new ItemStack(this));
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
	{
		if (world.isRemote || player.capabilities.isCreativeMode)
		{
			return;
		}

		double dist = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		Vec3d start = player.getPositionEyes(1F);
		Vec3d look = player.getLookVec();
		Vec3d end = start.add(look.x * dist, look.y * dist, look.z * dist);
		IBlockState state = world.getBlockState(pos);
		RayTraceResult ray = collisionRayTrace(state, world, pos, start, end);

		if (ray == null || ray.sideHit != state.getValue(BlockHorizontal.FACING))
		{
			return;
		}

		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileBarrel)
		{
			long time = world.getTotalWorldTime();
			TileBarrel barrel = (TileBarrel) tileEntity;

			if (time - barrel.lastClick < 3)
			{
				return;
			}

			barrel.lastClick = time;

			((TileBarrel) tileEntity).barrel.content.removeItem((EntityPlayerMP) player, player.isSneaking() == YabbaConfig.general.sneak_left_click_extracts_stack);

			player.inventory.markDirty();

			if (player.openContainer != null)
			{
				player.openContainer.detectAndSendChanges();
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack heldItem = player.getHeldItem(hand);
		if (Block.getBlockFromItem(heldItem.getItem()) instanceof BlockBarrel)
		{
			return false;
		}

		if (world.isRemote)
		{
			return true;
		}

		if (heldItem.getItem() == YabbaItems.HAMMER || heldItem.getItem() == YabbaItems.PAINTER)
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof TileBarrel)
			{
				TileBarrel barrel = (TileBarrel) tileEntity;

				if (heldItem.getItem() == YabbaItems.HAMMER)
				{
					barrel.barrel.setLook(BarrelLook.get(ItemHammer.getModel(heldItem), barrel.barrel.getLook().skin));
				}
				else
				{
					barrel.barrel.setLook(BarrelLook.get(barrel.barrel.getLook().model, ItemPainter.getSkin(heldItem)));
				}
			}

			return true;
		}

		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileBarrel)
		{
			TileBarrel barrel = (TileBarrel) tile;

			if (player.isSneaking())
			{
				barrel.barrel.openGui((EntityPlayerMP) player);
			}
			else if (heldItem.hasCapability(UpgradeData.CAPABILITY, null))
			{
				if (!barrel.barrel.hasUpgrade(heldItem.getItem()))
				{
					ItemStack upgradeStack = ItemHandlerHelper.copyStackWithSize(heldItem, 1);
					UpgradeData data = upgradeStack.getCapability(UpgradeData.CAPABILITY, null);

					if (data != null && data.canInsert(barrel.barrel, (EntityPlayerMP) player))
					{
						int i = barrel.barrel.findFreeUpgradeSlot();

						if (i != -1)
						{
							data.onInserted(barrel.barrel, (EntityPlayerMP) player);
							heldItem.shrink(1);
							barrel.barrel.setUpgrade(i, data);
							barrel.markBarrelDirty(true);
						}
					}
				}
			}
			else
			{
				long time = world.getTotalWorldTime();

				if (time - barrel.lastClick <= 8L)
				{
					barrel.barrel.content.addAllItems((EntityPlayerMP) player, hand);
				}
				else if (!heldItem.isEmpty())
				{
					barrel.barrel.content.addItem((EntityPlayerMP) player, hand);
				}

				barrel.lastClick = time;
			}
		}

		return true;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileBarrel && side == state.getValue(BlockHorizontal.FACING) && ((TileBarrel) tile).barrel.hasUpgrade(YabbaItems.UPGRADE_REDSTONE_OUT);
	}

	@Override
	@Deprecated
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileBarrel && side == state.getValue(BlockHorizontal.FACING) ? ((TileBarrel) tile).barrel.content.redstoneOutput() : 0;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileBarrel)
		{
			return ((TileBarrel) tile).getAABB();
		}

		return FULL_BLOCK_AABB;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileBarrel && ((TileBarrel) tile).barrel.hasUpgrade(YabbaItems.UPGRADE_OBSIDIAN_SHELL))
		{
			return Float.MAX_VALUE;
		}

		return 8F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (world != null && BlockUtils.hasData(stack))
		{
			TileBarrel barrel = (TileBarrel) createTileEntity(world, getDefaultState());
			barrel.readFromItem(stack);
			BarrelLook look = barrel.barrel.getLook();
			tooltip.add(ItemHammer.getModelTooltip(look.model));
			tooltip.add(ItemPainter.getSkinTooltip(look.skin));
			barrel.barrel.content.addInformation(tooltip, flag);
		}
		else
		{
			tooltip.add(ItemHammer.getModelTooltip(EnumBarrelModel.BARREL));
			tooltip.add(ItemPainter.getSkinTooltip(""));
		}
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random r)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileBarrel && ((TileBarrel) tile).barrel.hasUpgrade(YabbaItems.UPGRADE_SMELTING))
		{
			AxisAlignedBB aabb = ((TileBarrel) tile).barrel.getLook().model.getAABB(state.getValue(BlockHorizontal.FACING)).offset(pos);
			double w = aabb.maxX - aabb.minX;
			double h = aabb.maxY - aabb.minY;
			double d = aabb.maxZ - aabb.minZ;

			for (int i = 0; i < 2; i++)
			{
				world.spawnParticle(EnumParticleTypes.FLAME, aabb.minX + r.nextFloat() * w, aabb.minY + r.nextFloat() * h, aabb.minZ, 0D, 0D, 0D);
				world.spawnParticle(EnumParticleTypes.FLAME, aabb.minX + r.nextFloat() * w, aabb.minY + r.nextFloat() * h, aabb.maxZ, 0D, 0D, 0D);
				world.spawnParticle(EnumParticleTypes.FLAME, aabb.minX, aabb.minY + r.nextFloat() * h, aabb.minZ + r.nextFloat() * d, 0D, 0D, 0D);
				world.spawnParticle(EnumParticleTypes.FLAME, aabb.maxX, aabb.minY + r.nextFloat() * h, aabb.minZ + r.nextFloat() * d, 0D, 0D, 0D);
				world.spawnParticle(EnumParticleTypes.FLAME, aabb.minX + r.nextFloat() * w, aabb.maxY + r.nextFloat() * 0.2D, aabb.minZ + r.nextFloat() * d, 0D, 0D, 0D);

				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, aabb.minX + r.nextFloat() * w, aabb.minY + r.nextFloat() * h * 0.3D, aabb.minZ, 0D, 0D, 0D);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, aabb.minX + r.nextFloat() * w, aabb.minY + r.nextFloat() * h * 0.3D, aabb.maxZ, 0D, 0D, 0D);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, aabb.minX, aabb.minY + r.nextFloat() * h * 0.3D, aabb.minZ + r.nextFloat() * d, 0D, 0D, 0D);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, aabb.maxX, aabb.minY + r.nextFloat() * h * 0.3D, aabb.minZ + r.nextFloat() * d, 0D, 0D, 0D);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, aabb.minX + r.nextFloat() * w, aabb.maxY, aabb.minZ + r.nextFloat() * d, 0D, 0D, 0D);
			}
		}
	}
}