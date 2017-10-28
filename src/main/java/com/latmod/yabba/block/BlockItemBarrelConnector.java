package com.latmod.yabba.block;

import com.latmod.yabba.YabbaLang;
import com.latmod.yabba.tile.TileItemBarrelConnector;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class BlockItemBarrelConnector extends BlockYabba
{
	public BlockItemBarrelConnector()
	{
		super("item_barrel_connector", Material.WOOD, MapColor.WOOD);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileItemBarrelConnector();
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
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		for (int i = 0; i < 10; i++)
		{
			worldIn.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + rand.nextFloat(), pos.getY(), pos.getZ() + rand.nextFloat(), 0D, 0D, 0D);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity instanceof TileItemBarrelConnector)
		{
			TileItemBarrelConnector connector = (TileItemBarrelConnector) tileEntity;
			connector.updateLinks();
			playerIn.sendStatusMessage(YabbaLang.BARREL_CONNECTOR_CONNECTED.textComponent(playerIn, connector.linkedBarrels.size()), true);
		}

		return true;
	}
}