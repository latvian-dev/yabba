package com.latmod.yabba.block;

import com.feed_the_beast.ftblib.lib.util.misc.UnlistedPropertyString;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.item.IUpgrade;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.tile.TileAdvancedBarrelBase;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;
import com.latmod.yabba.util.UpgradeInst;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
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

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BlockAdvancedBarrelBase extends BlockCompoundBarrelBase
{
	public static final PropertyEnum<EnumFacing> FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<EnumBarrelModel> MODEL = PropertyEnum.create("model", EnumBarrelModel.class);
	public static final IUnlistedProperty<String> SKIN = UnlistedPropertyString.create("skin");

	public BlockAdvancedBarrelBase()
	{
		setDefaultState(blockState.getBaseState().withProperty(MODEL, EnumBarrelModel.BARREL).withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {FACING, MODEL}, new IUnlistedProperty[] {SKIN});
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	@Override
	@Deprecated
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileAdvancedBarrelBase)
		{
			EnumBarrelModel model = ((TileAdvancedBarrelBase) tileEntity).getLook().model;

			if (!model.isDefault())
			{
				return state.withProperty(MODEL, model);
			}
		}

		return state;
	}

	@Override
	@Deprecated
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileAdvancedBarrelBase)
		{
			BarrelLook look = ((TileAdvancedBarrelBase) tileEntity).getLook();

			if (!look.isDefault())
			{
				state = state.withProperty(MODEL, look.model);

				if (state instanceof IExtendedBlockState)
				{
					state = ((IExtendedBlockState) state).withProperty(SKIN, look.skin);
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
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	@Deprecated
	public IBlockState withMirror(IBlockState state, Mirror mirror)
	{
		return state.withRotation(mirror.toRotation(state.getValue(FACING)));
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
		return side != EnumFacing.DOWN;
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
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
	{
		if (world.isRemote || player.capabilities.isCreativeMode)
		{
			return;
		}

		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileAdvancedBarrelBase)
		{
			long time = world.getTotalWorldTime();
			TileAdvancedBarrelBase barrel = (TileAdvancedBarrelBase) tileEntity;

			if (time - barrel.lastClick < 3)
			{
				return;
			}

			barrel.lastClick = time;

			((TileAdvancedBarrelBase) tileEntity).removeItem(player, player.isSneaking() == YabbaConfig.general.sneak_left_click_extracts_stack);

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
		if (heldItem.getItem() instanceof ItemBlockBarrel)
		{
			return false;
		}

		if (world.isRemote)
		{
			return true;
		}

		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileAdvancedBarrelBase)
		{
			TileAdvancedBarrelBase barrel = (TileAdvancedBarrelBase) tile;

			if (player.isSneaking())
			{
				barrel.openGui(player);
			}
			else if (heldItem.getItem() instanceof IUpgrade)
			{
				if (!barrel.hasUpgrade(heldItem.getItem()))
				{
					ApplyUpgradeEvent event = new ApplyUpgradeEvent(false, barrel, new UpgradeInst(heldItem.getItem()), player, hand, side);

					if (event.getUpgrade().getUpgrade().applyOn(event))
					{
						if (event.consumeItem())
						{
							heldItem.shrink(1);
						}

						if (event.addUpgrade())
						{
							barrel.upgrades.put(event.getUpgrade().getItem(), event.getUpgrade());
						}

						barrel.markBarrelDirty(true);
					}
				}
			}
			else
			{
				long time = world.getTotalWorldTime();

				if (time - barrel.lastClick <= 8L)
				{
					barrel.addAllItems(player, hand);
				}
				else if (!heldItem.isEmpty())
				{
					barrel.addItem(player, hand);
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
		return tile instanceof TileAdvancedBarrelBase && ((TileAdvancedBarrelBase) tile).canConnectRedstone(side);
	}

	@Override
	@Deprecated
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = blockAccess.getTileEntity(pos);
		return tile instanceof TileAdvancedBarrelBase ? ((TileAdvancedBarrelBase) tile).redstoneOutput(side) : 0;
	}

	@Override
	@Deprecated
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = blockAccess.getTileEntity(pos);
		return tile instanceof TileAdvancedBarrelBase ? ((TileAdvancedBarrelBase) tile).redstoneOutput(side) : 0;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileAdvancedBarrelBase)
		{
			return ((TileAdvancedBarrelBase) tile).getAABB(state);
		}

		return FULL_BLOCK_AABB;
	}
}