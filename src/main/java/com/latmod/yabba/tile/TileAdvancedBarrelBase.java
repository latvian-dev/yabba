package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.config.ConfigBoolean;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.BarrelType;
import com.latmod.yabba.api.YabbaConfigEvent;
import com.latmod.yabba.block.BlockAdvancedBarrelBase;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import com.latmod.yabba.util.BarrelLook;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TileAdvancedBarrelBase extends TileBarrelBase implements IConfigCallback
{
	private BarrelLook look = BarrelLook.DEFAULT;
	public ConfigBoolean alwaysDisplayData = new ConfigBoolean(false);
	public ConfigBoolean displayBar = new ConfigBoolean(false);
	public long lastClick;

	private float cachedRotationX, cachedRotationY;
	private AxisAlignedBB cachedAABB;

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		super.writeData(nbt, type);

		if (!look.model.isEmpty())
		{
			nbt.setString("Model", look.model);
		}

		if (!look.skin.isEmpty())
		{
			nbt.setString("Skin", look.skin);
		}

		if (alwaysDisplayData.getBoolean())
		{
			nbt.setBoolean("AlwaysDisplayData", true);
		}

		if (displayBar.getBoolean())
		{
			nbt.setBoolean("DisplayBar", true);
		}
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		super.readData(nbt, type);
		look = BarrelLook.get(nbt.getString("Model"), nbt.getString("Skin"));
		alwaysDisplayData.setBoolean(nbt.getBoolean("AlwaysDisplayData"));
		displayBar.setBoolean(nbt.getBoolean("DisplayBar"));
	}

	@Override
	public BarrelType getType()
	{
		throw new IllegalStateException("Missing barrel type!");
	}

	@Override
	public IBlockState createState(IBlockState state)
	{
		if (state instanceof IExtendedBlockState)
		{
			return ((IExtendedBlockState) state).withProperty(BlockAdvancedBarrelBase.LOOK, getLook());
		}

		return state;
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		cachedRotationX = -1F;
		cachedRotationY = -1F;
		cachedAABB = null;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		updateContainingBlockInfo();
		return oldState.getBlock() != newSate.getBlock();
	}

	public float getRotationAngleX()
	{
		if (cachedRotationX == -1F)
		{
			IBlockState state = getBlockState();

			if (!(state.getBlock() instanceof BlockAdvancedBarrelBase))
			{
				return 0F;
			}

			cachedRotationX = state.getValue(BlockAdvancedBarrelBase.ROTATION).ordinal() * 90F;
		}

		return cachedRotationX;
	}

	public float getRotationAngleY()
	{
		if (cachedRotationY == -1F)
		{
			IBlockState state = getBlockState();

			if (!(state.getBlock() instanceof BlockAdvancedBarrelBase))
			{
				return 0F;
			}

			cachedRotationY = state.getValue(BlockHorizontal.FACING).getHorizontalAngle() + 180F;
		}

		return cachedRotationY;
	}

	@Override
	public BarrelLook getLook()
	{
		return look;
	}

	@Override
	public boolean setLook(BarrelLook l, boolean simulate)
	{
		if (!look.equals(l))
		{
			if (!simulate)
			{
				look = l;
				markBarrelDirty(true);
				updateContainingBlockInfo();
			}

			return true;
		}

		return false;
	}

	public boolean canConnectRedstone(@Nullable EnumFacing facing)
	{
		return getUpgradeData(YabbaItems.UPGRADE_REDSTONE_OUT) instanceof ItemUpgradeRedstone.Data;
	}

	public int redstoneOutput(EnumFacing facing)
	{
		return 0;
	}

	public String getItemDisplayName()
	{
		return "ERROR";
	}

	public String getItemDisplayCount(boolean isSneaking, boolean isCreative, boolean infinite)
	{
		return "ERROR";
	}

	@Override
	public boolean shouldDrop()
	{
		return !look.skin.isEmpty() || !look.model.isEmpty() || super.shouldDrop();
	}

	public void addItem(EntityPlayer player, EnumHand hand)
	{
	}

	public void addAllItems(EntityPlayer player, EnumHand hand)
	{
	}

	public void removeItem(EntityPlayer player, boolean stack)
	{
	}

	@Override
	public void createConfig(YabbaConfigEvent event)
	{
		super.createConfig(event);

		event.getConfig().add(Yabba.MOD_ID, "always_display_data", alwaysDisplayData).setNameLangKey("yabba_client.general.always_display_data");

		if (!tier.infiniteCapacity())
		{
			event.getConfig().add(Yabba.MOD_ID, "display_bar", displayBar).setNameLangKey("yabba_client.general.display_bar");
		}
	}

	public AxisAlignedBB getAABB(IBlockState state)
	{
		if (cachedAABB == null)
		{
			cachedAABB = getLook().getModelCustomData().getAABB(state);
		}

		return cachedAABB;
	}
}