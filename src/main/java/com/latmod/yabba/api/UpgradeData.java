package com.latmod.yabba.api;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.latmod.yabba.tile.Barrel;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class UpgradeData implements ICapabilitySerializable<NBTTagCompound>
{
	@CapabilityInject(UpgradeData.class)
	public static Capability<UpgradeData> CAPABILITY;

	public final ItemStack stack;

	public UpgradeData(ItemStack is)
	{
		stack = is;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == CAPABILITY ? (T) this : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return new NBTTagCompound();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
	}

	public boolean hasData()
	{
		return false;
	}

	public void resetData()
	{
	}

	public void getConfig(Barrel barrel, ConfigGroup config)
	{
	}

	public boolean canInsert(Barrel barrel, EntityPlayerMP player)
	{
		return true;
	}

	public void onInserted(Barrel barrel, EntityPlayerMP player)
	{
	}

	public boolean canRemove(Barrel barrel, EntityPlayerMP player)
	{
		return true;
	}

	public void onRemoved(Barrel barrel, EntityPlayerMP player)
	{
	}

	public void onTick(Barrel barrel)
	{
	}
}