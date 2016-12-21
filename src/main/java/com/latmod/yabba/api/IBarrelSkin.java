package com.latmod.yabba.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public interface IBarrelSkin extends IStringSerializable, Comparable<IBarrelSkin>
{
    IBlockState getState();

    @Nullable
    Object getCraftingItem();

    IIconSet getTextures();

    String getDisplayName();
}