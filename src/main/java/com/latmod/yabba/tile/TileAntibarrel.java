package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.feed_the_beast.ftblib.lib.tile.TileBase;
import com.latmod.yabba.util.AntibarrelData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TileAntibarrel extends TileBase
{
	public int totalChanges = 0;
	public final AntibarrelData contents = new AntibarrelData(this);

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side)
	{
		return contents.hasCapability(capability, side) || super.hasCapability(capability, side);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side)
	{
		T cap = contents.getCapability(capability, side);
		return cap != null ? cap : super.getCapability(capability, side);
	}

	@Override
	public void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (type == EnumSaveType.SAVE)
		{
			NBTTagCompound nbt1 = contents.serializeNBT();

			for (String key : nbt1.getKeySet())
			{
				nbt.setTag(key, nbt1.getTag(key));
			}
		}
	}

	@Override
	public void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (type == EnumSaveType.SAVE)
		{
			contents.deserializeNBT(nbt);
		}
	}

	@Override
	public boolean notifyBlock()
	{
		return false;
	}

	@Override
	public void markDirty()
	{
		sendDirtyUpdate();
	}

	@Override
	public boolean shouldDrop()
	{
		return !contents.items.isEmpty();
	}

	@Override
	public void onContentsChanged(boolean majorChange)
	{
		if (!world.isRemote)
		{
			totalChanges++;
			markDirty();
		}
	}

	@Override
	public void writeToItem(ItemStack stack)
	{
	}

	@Override
	public void readFromItem(ItemStack stack)
	{
		contents.copyFrom(AntibarrelData.get(stack));
	}
}