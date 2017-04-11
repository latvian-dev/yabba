package com.latmod.yabba.api;

import com.feed_the_beast.ftbl.lib.Color4I;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public interface ITier extends IStringSerializable
{
    int getMaxStacks();

    int getMaxItems(IBarrel barrel, @Nullable ItemStack itemStack);

    Color4I getColor();
}