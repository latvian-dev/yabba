package com.latmod.yabba;

import com.latmod.yabba.api.IBarrelVariant;
import com.latmod.yabba.api.IconSet;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class BarrelVariant implements IBarrelVariant
{
    private static final IconSet DEFAULT_TEXTURES = IconSet.all("blocks/planks_oak");

    private final String name;
    private final IBlockState parentState;
    private Object craftItem;
    private IconSet iconSet;

    public BarrelVariant(String id, IBlockState state, @Nullable Object item, IconSet is)
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
    public IconSet getTextures()
    {
        return iconSet;
    }

    @Override
    public int compareTo(IBarrelVariant o)
    {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o)
    {
        return o == this || (o instanceof IBarrelVariant && getName().equals(((IBarrelVariant) o).getName()));
    }
}