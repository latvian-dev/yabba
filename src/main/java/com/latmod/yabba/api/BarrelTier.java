package com.latmod.yabba.api;

/**
 * Created by LatvianModder on 17.12.2016.
 */
public class BarrelTier
{
    public static final BarrelTier NONE = new BarrelTier("none", 0);

    private final String tierId;
    private final int maxStacks;

    public BarrelTier(String id, int max)
    {
        tierId = id;
        maxStacks = max;
    }

    public String getTierID()
    {
        return tierId;
    }

    public int getMaxStacks()
    {
        return maxStacks;
    }

    public String toString()
    {
        return tierId;
    }

    public boolean equals(Object o)
    {
        return o == this || o != null && o.toString().equals(tierId);
    }

    public int hashCode()
    {
        return tierId.hashCode();
    }
}