package com.latmod.yabba.client;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.latmod.yabba.api.IIconSet;
import com.latmod.yabba.util.IconSet;
import com.latmod.yabba.util.SpriteSet;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class ModelBarrel extends ModelBarrelBase
{
    private static final IIconSet TEXTURES_BAND = new IconSet("north=yabba:blocks/barrel_band_window,south&east&west=yabba:blocks/barrel_band");

    public ModelBarrel(String v)
    {
        super(v);
        Collection<ResourceLocation> tex = variant.getTextures().getTextures();
        tex.addAll(TEXTURES_BAND.getTextures());
        textures = Collections.unmodifiableCollection(tex);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = IPerspectiveAwareModel.MapWrapper.getTransforms(state);
        List<BakedQuad> quads = new ArrayList<>(78);

        SpriteSet spriteSet = new SpriteSet(variant.getTextures(), bakedTextureGetter);
        ModelRotation rotation = ModelRotation.X0_Y0;

        switch(facing)
        {
            case SOUTH:
                rotation = ModelRotation.X0_Y180;
                break;
            case EAST:
                rotation = ModelRotation.X0_Y90;
                break;
            case WEST:
                rotation = ModelRotation.X0_Y270;
                break;
        }

        TextureAtlasSprite topSprite = spriteSet.get(EnumFacing.UP);
        TextureAtlasSprite bottomSprite = spriteSet.get(EnumFacing.DOWN);
        TextureAtlasSprite frontSprite = spriteSet.get(EnumFacing.NORTH);

        // Band
        YabbaModels.addCube(quads, 0F, 0F, 0F, 16F, 16F, 16F, new SpriteSet(TEXTURES_BAND, bakedTextureGetter), rotation);

        /// Top H
        YabbaModels.addCube(quads, 2F, 15F, 2F, 14F, 16F, 14F, spriteSet.exclude(EnumFacing.DOWN), rotation);
        YabbaModels.addCube(quads, 1F, 13F, 1F, 15F, 15F, 15F, spriteSet.exclude(EnumFacing.DOWN), rotation);
        YabbaModels.addCube(quads, 0F, 12F, 0F, 16F, 13F, 16F, spriteSet.exclude(EnumFacing.DOWN, EnumFacing.UP), rotation);

        // Top V
        YabbaModels.addQuad(quads, 0F, 13F, 0F, 16F, 13F, 1F, EnumFacing.UP, topSprite, rotation);
        YabbaModels.addQuad(quads, 0F, 13F, 15F, 16F, 13F, 16F, EnumFacing.UP, topSprite, rotation);
        YabbaModels.addQuad(quads, 0F, 13F, 1F, 1F, 13F, 15F, EnumFacing.UP, topSprite, rotation);
        YabbaModels.addQuad(quads, 15F, 13F, 1F, 16F, 13F, 15F, EnumFacing.UP, topSprite, rotation);

        // Center block
        YabbaModels.addCube(quads, 0F, 6F, 0F, 16F, 10F, 16F, spriteSet.exclude(EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH), rotation);

        // Front
        YabbaModels.addQuad(quads, 4F, 4F, 1F, 12F, 12F, 1F, EnumFacing.NORTH, frontSprite, rotation);
        YabbaModels.addQuad(quads, 0F, 6F, 0F, 4F, 10F, 0F, EnumFacing.NORTH, frontSprite, rotation);
        YabbaModels.addQuad(quads, 12F, 6F, 0F, 16F, 10F, 0F, EnumFacing.NORTH, frontSprite, rotation);

        // Bottom V
        YabbaModels.addQuad(quads, 0F, 3F, 0F, 16F, 3F, 1F, EnumFacing.DOWN, bottomSprite, rotation);
        YabbaModels.addQuad(quads, 0F, 3F, 15F, 16F, 3F, 16F, EnumFacing.DOWN, bottomSprite, rotation);
        YabbaModels.addQuad(quads, 0F, 3F, 1F, 1F, 3F, 15F, EnumFacing.DOWN, bottomSprite, rotation);
        YabbaModels.addQuad(quads, 15F, 3F, 1F, 16F, 3F, 15F, EnumFacing.DOWN, bottomSprite, rotation);

        // Bottom H
        YabbaModels.addCube(quads, 0F, 3F, 0F, 16F, 4F, 16F, spriteSet.exclude(EnumFacing.DOWN, EnumFacing.UP), rotation);
        YabbaModels.addCube(quads, 1F, 1F, 1F, 15F, 3F, 15F, spriteSet.exclude(EnumFacing.UP), rotation);
        YabbaModels.addCube(quads, 2F, 0F, 2F, 14F, 1F, 14F, spriteSet.exclude(EnumFacing.UP), rotation);

        return new BarrelVariantBakedModel(frontSprite, Collections.unmodifiableList(quads), Maps.immutableEnumMap(transformMap));
    }
}