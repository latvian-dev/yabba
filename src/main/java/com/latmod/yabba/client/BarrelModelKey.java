package com.latmod.yabba.client;

import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;

/**
 * @author LatvianModder
 */
public class BarrelModelKey
{
    public final IBarrelModel model;
    public final IBarrelSkin skin;

    public BarrelModelKey(IBarrelModel m, IBarrelSkin s)
    {
        model = m;
        skin = s;
    }

    public BarrelModelKey(String m, String s)
    {
        this(YabbaRegistry.INSTANCE.getModel(m), YabbaRegistry.INSTANCE.getSkin(s));
    }

    public int hashCode()
    {
        return model.hashCode() * 31 + skin.hashCode();
    }

    public boolean equals(Object o)
    {
        if(o == this)
        {
            return true;
        }
        else if(o instanceof BarrelModelKey)
        {
            BarrelModelKey k = (BarrelModelKey) o;
            return k.model == model && k.skin == skin;
        }
        return false;
    }
}