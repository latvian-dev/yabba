package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.ITier;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
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
    public int getMaxItems(IBarrel barrel, ItemStack itemStack)
    {
        if(barrel.getFlag(IBarrel.FLAG_INFINITE_CAPACITY))
        {
            return YabbaConfig.TIER_ITEM_INFINITY.getInt();
        }

        return getMaxStacks() * (itemStack.isEmpty() ? 1 : itemStack.getMaxStackSize());
    }

    @Override
    public Color4I getColor()
    {
        return color;
    }
}