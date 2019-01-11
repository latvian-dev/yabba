package com.latmod.yabba.block;

import com.feed_the_beast.ftblib.lib.block.BlockSpecialDrop;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.feed_the_beast.ftblib.lib.util.misc.UnlistedPropertyString;
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
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BlockBarrel extends BlockSpecialDrop
{
	public static final PropertyEnum<EnumFacing> FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<EnumBarrelModel> MODEL = PropertyEnum.create("model", EnumBarrelModel.class);
	public static final IUnlistedProperty<String> SKIN = UnlistedPropertyString.create("skin", s -> true);

	public BlockBarrel()
	{
		super(Material.WOOD, MapColor.WOOD);
		setHardness(2F);
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
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	@Deprecated
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileBarrel)
		{
			EnumBarrelModel model = ((TileBarrel) tileEntity).barrel.getLook().model;

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

		if (tileEntity instanceof TileBarrel)
		{
			BarrelLook look = ((TileBarrel) tileEntity).barrel.getLook();

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
						for (int i = 0; i < barrel.barrel.getUpgradeCount(); i++)
						{
							if (barrel.barrel.getUpgrade(i) == null)
							{
								data.onInserted(barrel.barrel, (EntityPlayerMP) player);
								heldItem.shrink(1);
								barrel.barrel.setUpgrade(i, data);
								barrel.markBarrelDirty(true);
								break;
							}
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
		return tile instanceof TileBarrel && side == state.getValue(FACING) && ((TileBarrel) tile).barrel.hasUpgrade(YabbaItems.UPGRADE_REDSTONE_OUT);
	}

	@Override
	@Deprecated
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileBarrel && side == state.getValue(FACING) ? ((TileBarrel) tile).barrel.content.redstoneOutput() : 0;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileBarrel)
		{
			return ((TileBarrel) tile).getAABB(state);
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
}