package com.latmod.yabba.api;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface IBarrelModifiable extends IBarrel, IItemHandlerModifiable
{
    void setTier(ITier tier);

    void setFlags(int flags);

    void setFlag(int flag, boolean v);

    void setItemCount(int v);

    void setModel(IBarrelModel model);

    void setSkin(IBarrelSkin variant);

    void setUpgradeNBT(@Nullable NBTTagCompound nbt);

    void setUpgradeData(String upgrade, @Nullable NBTBase v);

    void setUpgradeNames(@Nullable NBTTagList nbt);

    void addUpgradeName(String name);

    void markBarrelDirty();

    void clearCachedData();

    void copyFrom(IBarrel barrel);
}