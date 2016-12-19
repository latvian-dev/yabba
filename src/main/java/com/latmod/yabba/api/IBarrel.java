package com.latmod.yabba.api;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public interface IBarrel extends IItemHandler
{
    IBarrelTier getTier();

    int getItemCount();

    @Nullable
    NBTTagCompound getUpgradeNBT();

    @Nullable
    NBTBase getUpgradeData(String upgrade);

    IBarrelVariant getVariant();
}