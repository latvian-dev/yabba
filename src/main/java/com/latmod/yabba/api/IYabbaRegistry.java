package com.latmod.yabba.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

/**
 * @author LatvianModder
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