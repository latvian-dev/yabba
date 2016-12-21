package com.latmod.yabba.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public interface IIconSet
{
    @Nullable
    ResourceLocation getTexture(EnumFacing face);

    Collection<ResourceLocation> getTextures();
}