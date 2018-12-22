package com.latmod.yabba.tile;

import com.latmod.yabba.api.BarrelContentType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class FluidBarrel extends BarrelContent implements IFluidHandler
{
	public FluidBarrel(Barrel b)
	{
		super(b);
	}

	@Override
	public BarrelContentType getType()
	{
		return BarrelContentType.FLUID;
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
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T) this : null;
	}

	@Override
	public IFluidTankProperties[] getTankProperties()
	{
		return new IFluidTankProperties[0];
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Nullable
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		return null;
	}

	@Nullable
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		return null;
	}
}