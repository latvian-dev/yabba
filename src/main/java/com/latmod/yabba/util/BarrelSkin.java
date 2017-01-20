package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.model.IconSet;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class BarrelSkin extends FinalIDObject implements IBarrelSkin
{
    private final IBlockState state;
    private final IconSet iconSet;
    private String cachedName;

    public BarrelSkin(IBlockState s, IconSet is)
    {
        super(LMUtils.getName(s));
        state = s;
        iconSet = is;
    }

    @Override
    public IBlockState getState()
    {
        return state;
    }

    @Override
    public IconSet getTextures()
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
}