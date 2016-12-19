package com.latmod.yabba.api;

import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public interface IBarrelVariant extends Comparable<IBarrelVariant>
{
    String getName();

    IBlockState getParentState();

    @Nullable
    Object getCraftingItem();

    IconSet getTextures();
}