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
    void setTier(Tier tier);

    void setFlags(int flags);

    void setFlag(int flag, boolean v);

    void setItemCount(int v);

    void setModel(String model);

    void setSkin(String skin);

    void setUpgradeNBT(@Nullable NBTTagCompound nbt);

    void setUpgradeData(String upgrade, @Nullable NBTBase v);

    void setUpgradeNames(@Nullable NBTTagList nbt);

    void addUpgradeName(String name);

    void markBarrelDirty();

    void clearCachedData();

    default void copyFrom(IBarrel barrel)
    {
        setTier(barrel.getTier());
        setFlags(barrel.getFlags());
        setItemCount(barrel.getItemCount());
        setStackInSlot(0, barrel.getStackInSlot(0));
        setModel(barrel.getModel());
        setSkin(barrel.getSkin());
        setUpgradeNBT(barrel.getUpgradeNBT().copy());
        NBTTagList upgradeNames = barrel.getUpgradeNames();
        setUpgradeNames(upgradeNames == null ? null : upgradeNames.copy());
    }
}