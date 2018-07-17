package com.latmod.yabba.block;

import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.item.ItemHammer;
import com.latmod.yabba.item.ItemPainter;
import com.latmod.yabba.tile.TileDecorativeBlock;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class BlockDecorativeBlock extends BlockYabba
{
	public BlockDecorativeBlock(String id)
	{
		super(id, Material.WOOD, MapColor.WOOD);
		setHardness(2F);
		setDefaultState(blockState.getBaseState().withProperty(BlockAdvancedBarrelBase.MODEL, EnumBarrelModel.BARREL).withProperty(BlockAdvancedBarrelBase.FACING, EnumFacing.NORTH));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {BlockAdvancedBarrelBase.FACING, BlockAdvancedBarrelBase.MODEL}, new IUnlistedProperty[] {BlockAdvancedBarrelBase.SKIN});
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BlockAdvancedBarrelBase.FACING, EnumFacing.byHorizontalIndex(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockAdvancedBarrelBase.FACING).getHorizontalIndex();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
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
	public TileDecorativeBlock createTileEntity(World world, IBlockState state)
	{
		return new TileDecorativeBlock();
	}

	public ItemStack createStack(IBlockState state, BarrelLook look)
	{
		TileDecorativeBlock tile = new TileDecorativeBlock();
		tile.setLook(look, false);
		return createStack(state, tile);
	}

	@Override
	@Deprecated
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileDecorativeBlock)
		{
			return createStack(state, ((TileDecorativeBlock) tileEntity).getLook());
		}

		return super.getItem(world, pos, state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		list.add(createStack(getDefaultState(), BarrelLook.get(EnumBarrelModel.CRATE, "")));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() instanceof BlockDecorativeBlock)
		{
			return false;
		}
		else if (world.isRemote)
		{
			return true;
		}

		if (stack.getItem() == YabbaItems.HAMMER || stack.getItem() == YabbaItems.PAINTER)
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof TileDecorativeBlock)
			{
				if (stack.getItem() == YabbaItems.HAMMER)
				{
					((TileDecorativeBlock) tileEntity).setModel(ItemHammer.getModel(stack), false);
				}
				else
				{
					((TileDecorativeBlock) tileEntity).setSkin(ItemPainter.getSkin(stack), false);
				}
			}
		}

		return true;
	}

	@Override
	@Deprecated
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileDecorativeBlock)
		{
			EnumBarrelModel model = ((TileDecorativeBlock) tileEntity).getLook().model;

			if (!model.isDefault())
			{
				return state.withProperty(BlockAdvancedBarrelBase.MODEL, model);
			}
		}

		return state;
	}

	@Override
	@Deprecated
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileDecorativeBlock)
		{
			BarrelLook look = ((TileDecorativeBlock) tileEntity).getLook();

			if (!look.isDefault())
			{
				state = state.withProperty(BlockAdvancedBarrelBase.MODEL, look.model);

				if (state instanceof IExtendedBlockState)
				{
					state = ((IExtendedBlockState) state).withProperty(BlockAdvancedBarrelBase.SKIN, look.skin);
				}

				return state;
			}
		}

		return state;
	}

	@Override
	@Deprecated
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(BlockAdvancedBarrelBase.FACING, rot.rotate(state.getValue(BlockAdvancedBarrelBase.FACING)));
	}

	@Override
	@Deprecated
	public IBlockState withMirror(IBlockState state, Mirror mirror)
	{
		return state.withRotation(mirror.toRotation(state.getValue(BlockAdvancedBarrelBase.FACING)));
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
	@SideOnly(Side.CLIENT)
	public boolean hasCustomBreakingProgress(IBlockState state)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	@Deprecated
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return super.shouldSideBeRendered(state, world, pos, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer != BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	@Deprecated
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return getDefaultState().withProperty(BlockAdvancedBarrelBase.FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileDecorativeBlock)
		{
			return ((TileDecorativeBlock) tile).getAABB(state);
		}

		return FULL_BLOCK_AABB;
	}
}