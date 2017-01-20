package com.latmod.yabba.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public interface ITier extends IStringSerializable
{
    int getMaxStacks();

    default int getMaxItems(IBarrel barrel, @Nullable ItemStack itemStack)
    {
        if(barrel.getFlag(IBarrel.FLAG_INFINITE_CAPACITY))
        {
            return 2000000000;
        }

        return getMaxStacks() * (itemStack == null ? 1 : itemStack.getMaxStackSize());
    }
}