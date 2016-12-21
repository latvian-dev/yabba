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
    ITier getTier();

    int getItemCount();

    @Nullable
    NBTTagCompound getUpgradeNBT();

    @Nullable
    NBTBase getUpgradeData(String upgrade);

    IBarrelSkin getSkin();

    IBarrelModel getModel();

    boolean isLocked();
}