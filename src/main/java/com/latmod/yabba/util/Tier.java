package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.ITier;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 17.12.2016.
 */
public class Tier extends FinalIDObject implements ITier
{
    public static final Tier WOOD = new Tier("wood", YabbaConfig.TIER_ITEM_WOOD);

    private final IConfigValue config;

    public Tier(String id, IConfigValue p)
    {
        super(id);
        config = p;
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

        return getMaxStacks() * (itemStack == null ? 1 : itemStack.getMaxStackSize());
    }
}