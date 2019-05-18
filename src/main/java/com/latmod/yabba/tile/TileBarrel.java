package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.feed_the_beast.ftblib.lib.tile.TileBase;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.latmod.yabba.api.BarrelContentType;
import com.latmod.yabba.api.UpgradeData;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.net.MessageUpdateBarrelContent;
import com.latmod.yabba.util.BarrelLook;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TileBarrel extends TileBase implements IBarrelBlock, ITickable
{
	public final Barrel barrel;
	public long lastClick;
	private float cachedRotationY;
	private AxisAlignedBB cachedAABB;
	private int updateLevel = 0;
	private int redstoneOut = -1;

	public TileBarrel()
	{
		barrel = new Barrel(this, getContentType());
		updateContainingBlockInfo();
	}

	public BarrelContentType getContentType()
	{
		return BarrelContentType.DECORATIVE;
	}

	@Override
	public final TileEntity getBarrelTileEntity()
	{
		return this;
	}

	@Override
	public final void clearCache()
	{
		updateContainingBlockInfo();
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		barrel.clearCache();
		cachedRotationY = -1F;
		cachedAABB = null;
	}

	@Override
	public final Barrel getBarrel()
	{
		return barrel;
	}

	@Override
	public void markBarrelDirty(boolean majorChange)
	{
		clearCache();
		updateLevel = Math.max(updateLevel, majorChange ? 2 : 1);
	}

	@Override
	public void onUpdatePacket(EnumSaveType type)
	{
		markBarrelDirty(true);
	}

	@Override
	public void invalidate()
	{
		BarrelNetwork net = BarrelNetwork.get(getWorld());

		if (net != null)
		{
			net.barrelUpdated(getContentType());
		}

		super.invalidate();
	}

	@Override
	public void setWorld(World world)
	{
		super.setWorld(world);
		BarrelNetwork net = BarrelNetwork.get(getWorld());

		if (net != null)
		{
			net.barrelUpdated(getContentType());
		}
	}

	@Override
	public void markDirty()
	{
		markBarrelDirty(false);
	}

	@Override
	public final boolean isBarrelInvalid()
	{
		return isInvalid();
	}

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		barrel.writeData(nbt);

		if (redstoneOut >= 0)
		{
			nbt.setInteger("RedstoneOut", redstoneOut);
		}
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		updateContainingBlockInfo();
		barrel.readData(nbt);
		redstoneOut = nbt.hasKey("RedstoneOut") ? nbt.getInteger("RedstoneOut") : -1;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return barrel.content.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		T t = barrel.content.getCapability(capability, facing);
		return t == null ? super.getCapability(capability, facing) : t;
	}

	@Override
	public void update()
	{
		if (!world.isRemote)
		{
			barrel.content.update();

			for (int i = 0; i < barrel.getUpgradeCount(); i++)
			{
				UpgradeData data = barrel.getUpgrade(i);

				if (data != null && data.isTicking(world))
				{
					data.onTick(barrel);
				}
			}
		}

		if (updateLevel > 0)
		{
			if (!world.isRemote)
			{
				new MessageUpdateBarrelContent(this).sendToAllTracking(world.provider.getDimension(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
			}

			if (updateLevel >= 2)
			{
				sendDirtyUpdate();
			}

			updateLevel = 0;
		}

		int prev = redstoneOut;
		redstoneOut = barrel.content.redstoneOutput();

		if (prev != redstoneOut)
		{
			world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
		}
	}

	public float getRotationAngleY()
	{
		if (cachedRotationY == -1F)
		{
			IBlockState state = getBlockState();

			if (!(state.getBlock() instanceof BlockBarrel))
			{
				return 0F;
			}

			cachedRotationY = state.getValue(BlockHorizontal.FACING).getHorizontalAngle() + 180F;
		}

		return cachedRotationY;
	}

	public AxisAlignedBB getAABB()
	{
		if (cachedAABB == null)
		{
			cachedAABB = barrel.getLook().model.getAABB(getBarrelRotation());
		}

		return cachedAABB;
	}

	@Override
	public void writeToPickBlock(ItemStack stack)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		BarrelLook look = barrel.getLook();

		if (!look.model.isDefault())
		{
			nbt.setString("Model", look.model.getNBTName());
		}

		if (!look.skin.isEmpty())
		{
			nbt.setString("Skin", look.skin);
		}

		if (!nbt.isEmpty())
		{
			stack.setTagInfo(BlockUtils.DATA_TAG, nbt);
		}
	}

	@Override
	public EnumFacing getBarrelRotation()
	{
		return getBlockState().getValue(BlockHorizontal.FACING);
	}
}