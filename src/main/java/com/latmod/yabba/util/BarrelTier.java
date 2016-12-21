package com.latmod.yabba.util;

import com.latmod.yabba.api.IBarrelTier;

/**
 * Created by LatvianModder on 17.12.2016.
 */
public class BarrelTier implements IBarrelTier
{
    public static final BarrelTier NONE = new BarrelTier("none", 0);

    private final String name;
    private final int maxStacks;

    public BarrelTier(String id, int max)
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