package com.latmod.yabba.util;

import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.IIconSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class BarrelSkin implements IBarrelSkin
{
    private final String name;
    private final IBlockState state;
    private final Object craftItem;
    private final IIconSet iconSet;
    private String cachedName;

    public BarrelSkin(IBlockState state, @Nullable Object item, IIconSet is)
    {
        name = YabbaUtils.getName(state);
        this.state = state;
        craftItem = item;
        iconSet = is;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public IBlockState getState()
    {
        return state;
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
    public String getDisplayName()
    {
        if(cachedName == null)
        {
            try
            {
                cachedName = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName();
            }
            catch(Exception ex)
            {
                cachedName = I18n.translateToLocal(state.getBlock().getUnlocalizedName());
            }
        }

        return cachedName;
    }

    @Override
    public int compareTo(IBarrelSkin o)
    {
        return getName().compareTo(o.getName());
    }

    public String toString()
    {
        return name;
    }

    public boolean equals(Object o)
    {
        return o == this || o instanceof IBarrelSkin && state.equals(((IBarrelSkin) o).getState());
    }

    public int hashCode()
    {
        return name.hashCode();
    }
}