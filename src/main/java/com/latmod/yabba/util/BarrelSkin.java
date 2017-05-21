package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.IconSet;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class BarrelSkin extends FinalIDObject implements IBarrelSkin
{
    private final IBlockState state;
    private final IconSet iconSet;
    private final String unlocalizedName;

    public BarrelSkin(IBlockState s, IconSet is, String u)
    {
        super(LMUtils.getName(s));
        state = s;
        iconSet = is;

        if(u.isEmpty())
        {
            try
            {
                u = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getUnlocalizedName();

                if(u.equals("tile.air"))
                {
                    u = "";
                }
            }
            catch(Exception ex)
            {
            }

            if(u.isEmpty())
            {
                u = state.getBlock().getUnlocalizedName();
            }

            u += ".name";
        }

        unlocalizedName = u;
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
    public String getUnlocalizedName()
    {
        return unlocalizedName;
    }
}