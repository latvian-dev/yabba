package com.latmod.yabba.api;

import com.google.common.base.Function;
import com.latmod.yabba.util.SpriteSet;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.List;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public interface IBarrelModel extends IStringSerializable, Comparable<IBarrelModel>
{
    Collection<ResourceLocation> getExtraTextures();

    @SideOnly(Side.CLIENT)
    List<BakedQuad> buildModel(SpriteSet spriteSet, IBarrelModelData data, Function<ResourceLocation, TextureAtlasSprite> textureAtlas);
}