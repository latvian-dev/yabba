package com.latmod.yabba.util;

import com.latmod.yabba.api.ITier;

/**
 * Created by LatvianModder on 17.12.2016.
 */
public class Tier implements ITier
{
    public static final Tier NONE = new Tier("none", 0);

    private final String name;
    private final int maxStacks;

    public Tier(String id, int max)
    {
        name = id;
        maxStacks = max;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public int getMaxStacks()
    {
        return maxStacks;
    }

    public String toString()
    {
        return name;
    }

    public boolean equals(Object o)
    {
        return o == this || o != null && o.toString().equals(name);
    }

    public int hashCode()
    {
        return name.hashCode();
    }
}