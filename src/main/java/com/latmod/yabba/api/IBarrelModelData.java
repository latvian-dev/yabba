package com.latmod.yabba.api;

import net.minecraft.util.EnumFacing;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public interface IBarrelModelData
{
    IBarrelModel getModel();

    IBarrelSkin getSkin();

    EnumFacing getFacing();
}