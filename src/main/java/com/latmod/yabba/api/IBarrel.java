package com.latmod.yabba.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public interface IBarrel extends IItemHandler
{
    int FLAG_LOCKED = 1;
    int FLAG_VOID_ITEMS = 2;
    int FLAG_DISABLE_ORE_DICTIONARY = 4;
    int FLAG_IS_CREATIVE = 8;
    int FLAG_INFINITE_CAPACITY = 16;
    int FLAG_OBSIDIAN_SHELL = 32;
    int FLAG_REDSTONE_OUT = 64;
    int FLAG_HOPPER = 128;
    int FLAG_HOPPER_ENDER = 256;
    int FLAG_ALWAYS_DISPLAY_DATA = 512;

    ITier getTier();

    int getFlags();

    boolean getFlag(int flag);

    int getItemCount();

    IBarrelModel getModel();

    IBarrelSkin getSkin();

    NBTTagCompound getUpgradeNBT();

    @Nullable
    NBTTagList getUpgradeNames();

    int getFreeSpace();
}