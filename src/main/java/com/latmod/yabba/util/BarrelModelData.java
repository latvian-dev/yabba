package com.latmod.yabba.util;

import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class BarrelModelData
{
    private final IBarrelModel model;
    private final IBarrelSkin skin;

    public BarrelModelData(IBarrelModel m, IBarrelSkin s)
    {
        model = m;
        skin = s;
    }

    public IBarrelModel getModel()
    {
        return model;
    }

    public IBarrelSkin getSkin()
    {
        return skin;
    }

    public String toString()
    {
        return "model=" + model.getName() + ",skin=" + getSkin().getName();
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