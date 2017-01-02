package com.latmod.yabba.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public interface ITier
{
    String getName();

    int getMaxStacks();

    default int getMaxItems(@Nullable ItemStack itemStack)
    {
        return getMaxStacks() * (itemStack == null ? 1 : itemStack.getMaxStackSize());
    }
}