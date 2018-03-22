package com.latmod.yabba.block;

import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.tile.TileAdvancedBarrelBase;
import com.latmod.yabba.tile.TileBarrelBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BlockCompoundBarrelBase extends BlockYabba
{
	public BlockCompoundBarrelBase(String id)
	{
		super(id, Material.WOOD, MapColor.WOOD);
		setHardness(2F);
	}

	@Override
	public boolean dropSpecial(IBlockState state)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		list.add(createStack(getDefaultState(), Tier.WOOD));
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	public ItemStack createStack(IBlockState state, Tier tier)
	{
		TileBarrelBase tile = (TileBarrelBase) createTileEntity(null, state);
		tile.setTier(tier, false);
		return createStack(state, tile);
	}

	@Override
	@Deprecated
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return createStack(state, Tier.WOOD);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		return !(stack.getItem() instanceof ItemBlock) || !(((ItemBlock) stack.getItem()).getBlock() instanceof BlockCompoundBarrelBase);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileAdvancedBarrelBase && ((TileAdvancedBarrelBase) tile).hasUpgrade(YabbaItems.UPGRADE_OBSIDIAN_SHELL))
		{
			return Float.MAX_VALUE;
		}

		return 8F;
	}
}