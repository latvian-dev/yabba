package com.latmod.yabba.api;

import net.minecraft.block.state.IBlockState;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public interface IYabbaRegistry
{
    void addBarrel(String id, Object craftItem, IBlockState parentState);

    void addTier(BarrelTier tier);

    void addUpgrade(IUpgrade upgrade);
}