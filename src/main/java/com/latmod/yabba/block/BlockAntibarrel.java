package com.latmod.yabba.block;

import com.latmod.yabba.YabbaGuiHandler;
import com.latmod.yabba.net.MessageAntibarrelUpdate;
import com.latmod.yabba.tile.TileAntibarrel;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class BlockAntibarrel extends BlockYabba
{
	public BlockAntibarrel(String id)
	{
		super(id, Material.ROCK, MapColor.NETHERRACK);
		setHardness(4F);
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
				new MessageAntibarrelUpdate((TileAntibarrel) tileEntity).sendTo((EntityPlayerMP) player);
				YabbaGuiHandler.ANTIBARREL.open(player, pos);
			}
		}

		return true;
	}
}