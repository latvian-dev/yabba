package com.latmod.yabba.block;

import com.latmod.yabba.net.MessageBarrelConnector;
import com.latmod.yabba.tile.ItemBarrel;
import com.latmod.yabba.tile.TileItemBarrelConnector;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Random;

/**
 * @author LatvianModder
 */
public class BlockItemBarrelConnector extends Block
{
	public BlockItemBarrelConnector()
	{
		super(Material.WOOD, MapColor.WOOD);
		setHardness(1F);
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
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		for (int i = 0; i < 10; i++)
		{
			world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + rand.nextFloat(), pos.getY(), pos.getZ() + rand.nextFloat(), 0D, 0D, 0D);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}

		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileItemBarrelConnector)
		{
			TileItemBarrelConnector connector = (TileItemBarrelConnector) tileEntity;
			connector.getSlots();
			HashSet<BlockPos> list = new HashSet<>();

			for (ItemBarrel barrel : connector.linkedBarrels)
			{
				if (barrel.barrel.block.getBarrelTileEntity() != null)
				{
					list.add(barrel.barrel.block.getBarrelTileEntity().getPos());
				}
			}

			new MessageBarrelConnector(new TextComponentTranslation("tile.yabba.item_barrel_connector.name"), list).sendTo((EntityPlayerMP) player);
		}

		return true;
	}
}