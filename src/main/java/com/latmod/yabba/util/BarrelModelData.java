package com.latmod.yabba.util;

/**
 * @author LatvianModder
 */
public class BarrelModelData
{
    public final String model, skin;

    public BarrelModelData(String m, String s)
    {
        model = m;
        skin = s;
    }

    public String toString()
    {
        return "model=" + model + ",skin=" + skin;
    }

    public boolean equals(Object o)
    {
        if(o == this)
        {
            return true;
        }
        else if(o instanceof BarrelModelData)
        {
            BarrelModelData data = (BarrelModelData) o;
            return data.model.equals(model) && data.skin.equals(skin);
        }
        return false;
    }

    public int hashCode()
    {
        return model.hashCode() ^ skin.hashCode();
    }
}