package com.latmod.yabba.block;

import com.feed_the_beast.ftblib.lib.block.EnumRotation;
import com.feed_the_beast.ftblib.lib.util.misc.UnlistedPropertyString;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.item.IUpgrade;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.tile.TileBarrelBase;
import com.latmod.yabba.util.UpgradeInst;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraft.world.Explosion;
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
public class BlockBarrelBase extends BlockYabba
{
	public static final PropertyEnum<EnumRotation> ROTATION = PropertyEnum.create("rotation", EnumRotation.class);
	public static final IUnlistedProperty<String> MODEL = UnlistedPropertyString.create("model");
	public static final IUnlistedProperty<String> SKIN = UnlistedPropertyString.create("skin");

	public BlockBarrelBase(String id)
	{
		super(id, Material.WOOD, MapColor.WOOD);
		setDefaultState(blockState.getBaseState().withProperty(ROTATION, EnumRotation.NORMAL).withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
		setHardness(2F);
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
	public boolean dropSpecial(IBlockState state)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		list.add(createStack(getDefaultState(), "", "", Tier.WOOD));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {BlockHorizontal.FACING, ROTATION}, new IUnlistedProperty[] {MODEL, SKIN});
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
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	@Deprecated
	public IBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileBarrelBase)
		{
			return ((TileBarrelBase) tile).createState(state);
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
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(BlockHorizontal.FACING)));
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

	public ItemStack createStack(IBlockState state, String model, String skin, Tier tier)
	{
		TileBarrelBase tile = new TileBarrelBase();
		tile.setModel(model, false);
		tile.setSkin(skin, false);
		tile.setTier(tier, false);
		return createStack(state, tile);
	}

	@Override
	@Deprecated
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity instanceof TileBarrelBase)
		{
			TileBarrelBase barrel = (TileBarrelBase) tileEntity;
			return createStack(state, barrel.model, barrel.skin, Tier.WOOD);
		}

		return super.getItem(worldIn, pos, state);
	}

	@Override
	@Deprecated
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return getDefaultState().withProperty(ROTATION, EnumRotation.getRotationFromEntity(pos, placer)).withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		if (worldIn.isRemote || playerIn.capabilities.isCreativeMode)
		{
			return;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity instanceof TileBarrelBase)
		{
			long time = worldIn.getTotalWorldTime();
			TileBarrelBase barrel = (TileBarrelBase) tileEntity;

			if (time - barrel.lastClick < 3)
			{
				return;
			}

			barrel.lastClick = time;

			((TileBarrelBase) tileEntity).removeItem(playerIn, playerIn.isSneaking() == YabbaConfig.general.sneak_left_click_extracts_stack);

			playerIn.inventory.markDirty();

			if (playerIn.openContainer != null)
			{
				playerIn.openContainer.detectAndSendChanges();
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack heldItem = playerIn.getHeldItem(hand);
		if (heldItem.getItem() instanceof ItemBlockBarrel)
		{
			return false;
		}

		if (worldIn.isRemote)
		{
			return true;
		}

		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileBarrelBase)
		{
			TileBarrelBase barrel = (TileBarrelBase) tile;
			ItemStack handItem = playerIn.getHeldItem(hand);

			if (playerIn.isSneaking())
			{
				barrel.displayConfig(playerIn);
			}
			else if (handItem.getItem() instanceof IUpgrade)
			{
				if (!barrel.hasUpgrade(heldItem.getItem()))
				{
					ApplyUpgradeEvent event = new ApplyUpgradeEvent(false, barrel, new UpgradeInst(heldItem.getItem()), playerIn, hand, side);

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
				long time = worldIn.getTotalWorldTime();

				if (time - barrel.lastClick <= 8L)
				{
					barrel.addAllItems(playerIn, hand);
				}
				else if (!heldItem.isEmpty())
				{
					barrel.addItem(playerIn, hand);
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
		return tile instanceof TileBarrelBase && ((TileBarrelBase) tile).canConnectRedstone(side);
	}

	@Override
	@Deprecated
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = blockAccess.getTileEntity(pos);
		return tile instanceof TileBarrelBase ? ((TileBarrelBase) tile).redstoneOutput(side) : 0;
	}

	@Override
	@Deprecated
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = blockAccess.getTileEntity(pos);
		return tile instanceof TileBarrelBase ? ((TileBarrelBase) tile).redstoneOutput(side) : 0;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileBarrelBase)
		{
			return YabbaCommon.getModelData(((TileBarrelBase) tile).model).getAABB(state);
		}

		return FULL_BLOCK_AABB;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileBarrelBase && ((TileBarrelBase) tile).hasUpgrade(YabbaItems.UPGRADE_OBSIDIAN_SHELL))
		{
			return Float.MAX_VALUE;
		}

		return 8F;
	}
}