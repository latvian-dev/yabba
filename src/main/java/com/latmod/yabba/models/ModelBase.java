package com.latmod.yabba.models;

import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.latmod.yabba.api.IBarrelModel;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public abstract class ModelBase extends FinalIDObject implements IBarrelModel
{
    public ModelBase(String id)
    {
        super(id);
    }

    @Override
    public int compareTo(IBarrelModel o)
    {
        return getName().compareTo(o.getName());
    }
}