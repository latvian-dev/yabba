package com.latmod.yabba.api;

import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public interface IYabbaRegistry
{
    IBarrelSkin addSkin(IBlockState parentState, @Nullable Object craftItem, String icons);

    void addTier(ITier tier);

    void addModel(IBarrelModel model);

    IBarrelSkin getSkin(String id);

    IBarrelSkin getSkin(IBlockState id);

    ITier getTier(String id);

    boolean hasSkin(String id);

    boolean hasModel(String id);

    IBarrelModel getModel(String id);
}