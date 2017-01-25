package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.latmod.yabba.api.ITier;

/**
 * Created by LatvianModder on 17.12.2016.
 */
public class Tier extends FinalIDObject implements ITier
{
    public static final Tier WOOD = new Tier("wood");

    private int maxStacks;

    public Tier(String id)
    {
        super(id);
    }

    @Override
    public int getMaxStacks()
    {
        return maxStacks;
    }

    public void setMaxStacks(int m)
    {
        maxStacks = m;
    }
}