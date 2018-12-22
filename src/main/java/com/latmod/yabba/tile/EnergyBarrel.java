package com.latmod.yabba.tile;

import com.latmod.yabba.api.BarrelContentType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class EnergyBarrel extends BarrelContent implements IEnergyStorage
{
	public EnergyBarrel(Barrel b)
	{
		super(b);
	}

	@Override
	public BarrelContentType getType()
	{
		return BarrelContentType.ENERGY;
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
		return capability == CapabilityEnergy.ENERGY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityEnergy.ENERGY ? (T) this : null;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return 0;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return 0;
	}

	@Override
	public int getEnergyStored()
	{
		return 0;
	}

	@Override
	public int getMaxEnergyStored()
	{
		return 0;
	}

	@Override
	public boolean canExtract()
	{
		return false;
	}

	@Override
	public boolean canReceive()
	{
		return false;
	}
}