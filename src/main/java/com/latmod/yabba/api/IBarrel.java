package com.latmod.yabba.api;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public interface IBarrel extends IItemHandlerModifiable
{
    ITier getTier();

    void setTier(ITier tier);

    int getItemCount();

    void setItemCount(int v);

    @Nullable
    NBTTagCompound getUpgradeNBT();

    void setUpgradeNBT(@Nullable NBTTagCompound nbt);

    void removeUpgrades();

    @Nullable
    NBTBase getUpgradeData(IUpgrade upgrade);

    void setUpgradeData(IUpgrade upgrade, @Nullable NBTBase v);

    void copyFrom(IBarrel barrel);
}