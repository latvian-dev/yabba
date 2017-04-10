package com.latmod.yabba.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public interface IYabbaRegistry
{
    void addSkin(IBarrelSkin skin);

    IBarrelSkin addSkin(IBlockState parentState, String icons);

    default IBarrelSkin addSkin(Block block, String icons)
    {
        return addSkin(block.getDefaultState(), icons);
    }

    void addTier(ITier tier);

    void addModel(IBarrelModel model);
}