package com.latmod.yabba.api;

import com.feed_the_beast.ftbl.lib.IconSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public interface IBarrelSkin extends IStringSerializable
{
    IBlockState getState();

    IconSet getTextures();

    String getUnlocalizedName();
}