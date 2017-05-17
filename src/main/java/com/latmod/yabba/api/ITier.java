package com.latmod.yabba.api;

import com.feed_the_beast.ftbl.lib.Color4I;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public interface ITier extends IStringSerializable
{
    int getMaxStacks();

    int getMaxItems(IBarrel barrel, ItemStack itemStack);

    Color4I getColor();
}