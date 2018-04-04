package com.latmod.yabba.block;

import com.feed_the_beast.ftblib.lib.block.EnumRotation;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.item.IUpgrade;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.tile.TileAdvancedBarrelBase;
import com.latmod.yabba.util.BarrelLook;
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
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BlockAdvancedBarrelBase extends BlockCompoundBarrelBase
{
	public static final PropertyEnum<EnumRotation> ROTATION = PropertyEnum.create("rotation", EnumRotation.class);
	public static final IUnlistedProperty<BarrelLook> LOOK = new IUnlistedProperty<BarrelLook>()
	{
		@Override
		public String getName()
		{
			return "look";
		}

		@Override
		public boolean isValid(BarrelLook value)
		{
			return true;
		}

		@Override
		public Class<BarrelLook> getType()
		{
			return BarrelLook.class;
		}

		@Override
		public String valueToString(BarrelLook value)
		{
			return value.toString();
		}
	};

	public BlockAdvancedBarrelBase(String id)
	{
		super(id);
		setDefaultState(blockState.getBaseState().withProperty(ROTATION, EnumRotation.NORMAL).withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
	}

	public static EnumFacing normalizeFacing(IBlockState state)
	{
		EnumRotation rotation = state.getValue(ROTATION);

		if (rotation == EnumRotation.FACING_DOWN)
		{
			return EnumFacing.DOWN;
		}
		else if (rotation == EnumRotation.FACING_UP)
		{
			return EnumFacing.UP;
		}

		return state.getValue(BlockHorizontal.FACING);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {BlockHorizontal.FACING, ROTATION}, new IUnlistedProperty[] {LOOK});
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta)).withProperty(ROTATION, EnumRotation.VALUES[(meta >> 2) & 3]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(ROTATION).ordinal() << 2) | state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	@Override
	@Deprecated
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileAdvancedBarrelBase)
		{
			return ((TileAdvancedBarrelBase) tile).createState(state);
		}

		return state;
	}

	@Override
	@Deprecated
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
	}

	@Override
	@Deprecated
	public IBlockState withMirror(IBlockState state, Mirror mirror)
	{
		return state.withRotation(mirror.toRotation(state.getValue(BlockHorizontal.FACING)));
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
		return true;
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
		return true;
	}

	@Override
	@Deprecated
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return getDefaultState().withProperty(ROTATION, EnumRotation.getRotationFromEntity(pos, placer)).withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
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
				barrel.displayConfig(player);
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