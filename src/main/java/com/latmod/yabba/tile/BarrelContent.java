package com.latmod.yabba.tile;

import com.latmod.yabba.api.BarrelContentType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class BarrelContent implements ICapabilityProvider
{
	public final Barrel barrel;

	public BarrelContent(Barrel b)
	{
		barrel = b;
	}

	public void clearCache()
	{
	}

	public abstract BarrelContentType getType();

	public abstract boolean isEmpty();

	public abstract void writeData(NBTTagCompound nbt);

	public abstract void readData(NBTTagCompound nbt);

	@Nullable
	public abstract NBTBase writeContentData();

	public abstract void readContentData(@Nullable NBTBase nbt);

	@Override
	public abstract boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing);

	@Nullable
	@Override
	public abstract <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing);

	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip, ITooltipFlag flag)
	{
	}

	public int redstoneOutput()
	{
		return -1;
	}

	public String getItemDisplayName()
	{
		return "ERROR";
	}

	public String getItemDisplayCount(boolean isSneaking, boolean isCreative, boolean infinite)
	{
		return "ERROR";
	}

	public void addItem(EntityPlayerMP player, EnumHand hand)
	{
	}

	public void addAllItems(EntityPlayerMP player, EnumHand hand)
	{
	}

	public void removeItem(EntityPlayerMP player, boolean stack)
	{
	}

	public void update()
	{
	}

	public void onCreativeChange()
	{
	}
}