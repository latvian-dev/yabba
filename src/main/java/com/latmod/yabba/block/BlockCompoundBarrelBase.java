package com.latmod.yabba.block;

import com.feed_the_beast.ftblib.lib.block.BlockBase;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.item.ItemHammer;
import com.latmod.yabba.item.ItemPainter;
import com.latmod.yabba.tile.IBarrelBase;
import com.latmod.yabba.tile.TileBarrelBase;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BlockCompoundBarrelBase extends BlockBase
{
	public BlockCompoundBarrelBase()
	{
		super(Material.WOOD, MapColor.WOOD);
		setHardness(2F);
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

	public ItemStack createStack(IBlockState state, BarrelLook look, Tier tier)
	{
		TileEntity tileEntity = createTileEntity(null, state);

		if (tileEntity instanceof IBarrelBase)
		{
			IBarrelBase barrel = (IBarrelBase) tileEntity;
			barrel.setTier(tier, false);
			barrel.setLook(look, false);
		}

		return createStack(state, tileEntity);
	}

	@Override
	@Deprecated
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof IBarrelBase)
		{
			IBarrelBase barrel = (IBarrelBase) tileEntity;
			return createStack(state, barrel.getLook(), barrel.getTier());
		}

		return super.getItem(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() instanceof BlockCompoundBarrelBase || !player.isSneaking())
		{
			return false;
		}
		else if (world.isRemote)
		{
			return true;
		}

		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof IBarrelBase)
		{
			((IBarrelBase) tile).openGui(player);
		}

		return true;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof IBarrelBase && ((IBarrelBase) tile).hasUpgrade(YabbaItems.UPGRADE_OBSIDIAN_SHELL))
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
			TileBarrelBase barrel = (TileBarrelBase) createTileEntity(world, getDefaultState());
			barrel.readFromNBT(BlockUtils.getData(stack));

			if (this instanceof BlockAdvancedBarrelBase)
			{
				BarrelLook look = barrel.getLook();
				tooltip.add(ItemHammer.getModelTooltip(look.model));
				tooltip.add(ItemPainter.getSkinTooltip(look.skin));
			}

			barrel.addInformation(tooltip, flag);
		}
		else if (this instanceof BlockAdvancedBarrelBase)
		{
			tooltip.add(ItemHammer.getModelTooltip(EnumBarrelModel.BARREL));
			tooltip.add(ItemPainter.getSkinTooltip(""));
		}
	}
}