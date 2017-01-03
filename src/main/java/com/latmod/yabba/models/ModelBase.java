package com.latmod.yabba.models;

import com.latmod.yabba.api.IBarrelModel;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public abstract class ModelBase implements IBarrelModel
{
    private final String name;

    public ModelBase(String id)
    {
        name = id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return name;
    }

    public int hashCode()
    {
        return name.hashCode();
    }

    public boolean equals(Object o)
    {
        return o == this || o != null && o.toString().equals(name);
    }

    @Override
    public int compareTo(IBarrelModel o)
    {
        return getName().compareTo(o.getName());
    }
}