package com.latmod.yabba.util;

import com.latmod.yabba.api.IBarrelVariant;
import com.latmod.yabba.api.IIconSet;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class BarrelVariant implements IBarrelVariant
{
    private final String name;
    private final IBlockState parentState;
    private final Object craftItem;
    private final IIconSet iconSet;

    public BarrelVariant(String id, IBlockState state, @Nullable Object item, IIconSet is)
    {
        name = id.toLowerCase(Locale.ENGLISH);
        parentState = state;
        craftItem = item;
        iconSet = is;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public IBlockState getParentState()
    {
        return parentState;
    }

    @Override
    @Nullable
    public Object getCraftingItem()
    {
        return craftItem;
    }

    @Override
    public IIconSet getTextures()
    {
        return iconSet;
    }

    @Override
    public int compareTo(IBarrelVariant o)
    {
        return getName().compareTo(o.getName());
    }

    public String toString()
    {
        return name;
    }

    public boolean equals(Object o)
    {
        return o == this || o != null && name.equals(o);
    }

    public int hashCode()
    {
        return name.hashCode();
    }
}