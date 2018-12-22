package com.latmod.yabba.tile;

import com.latmod.yabba.api.BarrelContentType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class DecorativeBarrel extends BarrelContent
{
	public DecorativeBarrel(Barrel b)
	{
		super(b);
	}

	@Override
	public BarrelContentType getType()
	{
		return BarrelContentType.DECORATIVE;
	}

	@Override
	public boolean isEmpty()
	{
		return true;
	}

	@Override
	public void writeData(NBTTagCompound nbt)
	{
	}

	@Override
	public void readData(NBTTagCompound nbt)
	{
	}

	@Nullable
	@Override
	public NBTBase writeContentData()
	{
		return null;
	}

	@Override
	public void readContentData(@Nullable NBTBase nbt)
	{
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return false;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return null;
	}
}