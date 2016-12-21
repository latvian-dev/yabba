package com.latmod.yabba.api;

import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public interface IYabbaRegistry
{
    IBarrelVariant addVariant(String id, IBlockState parentState, @Nullable Object craftItem, String icons);

    void addTier(IBarrelTier tier);

    void addUpgrade(IUpgrade upgrade);
}