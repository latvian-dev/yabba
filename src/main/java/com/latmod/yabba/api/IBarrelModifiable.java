package com.latmod.yabba.api;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public interface IBarrelModifiable extends IBarrel, IItemHandlerModifiable
{
    void setTier(BarrelTier tier);

    void setItemCount(int v);

    void setUpgradeNBT(@Nullable NBTTagCompound nbt);

    void setUpgradeData(String upgrade, @Nullable NBTBase v);

    void copyFrom(IBarrel barrel);
}