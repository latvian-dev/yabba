package com.latmod.yabba.util;

import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelModelData;
import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.util.EnumFacing;

import java.util.Map;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class BarrelModelData implements IBarrelModelData
{
    private final IBarrelModel model;
    private final IBarrelSkin skin;
    private final EnumFacing facing;

    public BarrelModelData(IBarrelModel m, IBarrelSkin s, EnumFacing f)
    {
        model = m;
        skin = s;
        facing = f;
    }

    public BarrelModelData(String v)
    {
        Map<String, String> map = YabbaUtils.parse(YabbaUtils.TEMP_MAP, v);
        facing = EnumFacing.byName(map.get("facing"));
        skin = YabbaRegistry.INSTANCE.getSkin(map.get("skin"));
        model = YabbaRegistry.INSTANCE.getModel(map.get("model"));
    }

    @Override
    public IBarrelModel getModel()
    {
        return model;
    }

    @Override
    public IBarrelSkin getSkin()
    {
        return skin;
    }

    @Override
    public EnumFacing getFacing()
    {
        return facing;
    }

    public String toString()
    {
        return "facing=" + facing.getName() + ",model=" + model.getName() + ",skin=" + getSkin().getName();
    }

    public boolean equals(Object o)
    {
        if(o == this)
        {
            return true;
        }
        else if(o instanceof IBarrelModelData)
        {
            IBarrelModelData data = (IBarrelModelData) o;
            return data.getModel().equals(getModel()) && data.getSkin().equals(getSkin()) && data.getFacing() == getFacing();
        }
        return false;
    }

    public int hashCode()
    {
        return (model.hashCode() ^ skin.hashCode()) * (facing.ordinal() + 1);
    }
}