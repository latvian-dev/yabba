package com.latmod.yabba.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface IBarrel extends IItemHandler
{
    int FLAG_LOCKED = 1;
    int FLAG_VOID_ITEMS = 1 << 1;
    int FLAG_DISABLE_ORE_DICTIONARY = 1 << 2;
    int FLAG_IS_CREATIVE = 1 << 3;
    int FLAG_INFINITE_CAPACITY = 1 << 4;
    int FLAG_OBSIDIAN_SHELL = 1 << 5;
    int FLAG_REDSTONE_OUT = 1 << 6;
    int FLAG_HOPPER = 1 << 7;
    int FLAG_HOPPER_ENDER = 1 << 8;
    int FLAG_ALWAYS_DISPLAY_DATA = 1 << 9;
    int FLAG_DISPLAY_BAR = 1 << 10;

    Tier getTier();

    int getFlags();

    boolean getFlag(int flag);

    int getItemCount();

    String getModel();

    String getSkin();

    NBTTagCompound getUpgradeNBT();

    @Nullable
    NBTTagList getUpgradeNames();

    int getFreeSpace();
}