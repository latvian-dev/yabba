package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.ITier;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 17.12.2016.
 */
public class Tier extends FinalIDObject implements ITier
{
    public static final Tier WOOD = new Tier("wood", YabbaConfig.TIER_ITEM_WOOD, 0xFFC69569);

    private final IConfigValue config;
    private final Color4I color;

    public Tier(String id, IConfigValue p, int col)
    {
        super(id);
        config = p;
        color = new Color4I(false, col);
    }

    @Override
    public int getMaxStacks()
    {
        return config.getInt();
    }

    @Override
    public int getMaxItems(IBarrel barrel, @Nullable ItemStack itemStack)
    {
        if(barrel.getFlag(IBarrel.FLAG_INFINITE_CAPACITY))
        {
            return YabbaConfig.TIER_ITEM_INFINITY.getInt();
        }

        return getMaxStacks() * (ItemStackTools.isEmpty(itemStack) ? 1 : itemStack.getMaxStackSize());
    }

    @Override
    public Color4I getColor()
    {
        return color;
    }
}