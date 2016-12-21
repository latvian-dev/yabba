package com.latmod.yabba.client;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
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
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class ModelCrate extends ModelBarrelBase
{
    public ModelCrate(String v)
    {
        super(v);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = IPerspectiveAwareModel.MapWrapper.getTransforms(state);
        List<BakedQuad> quads = new ArrayList<>(78);

        SpriteSet spriteSet = new SpriteSet(variant.getTextures(), bakedTextureGetter);
        ModelRotation rotation = ModelRotation.X0_Y0;

        YabbaModels.addCube(quads, 1F, 1F, 1F, 15F, 15F, 15F, spriteSet, rotation);

        YabbaModels.addCube(quads, 0F, 0F, 0F, 1F, 16F, 1F, spriteSet, rotation);
        YabbaModels.addCube(quads, 15F, 0F, 0F, 16F, 16F, 1F, spriteSet, rotation);
        YabbaModels.addCube(quads, 0F, 0F, 15F, 1F, 16F, 16F, spriteSet, rotation);
        YabbaModels.addCube(quads, 15F, 0F, 15F, 16F, 16F, 16F, spriteSet, rotation);

        YabbaModels.addCube(quads, 1F, 0F, 0F, 15F, 1F, 1F, spriteSet.exclude(EnumFacing.WEST, EnumFacing.EAST), rotation);
        YabbaModels.addCube(quads, 1F, 0F, 15F, 15F, 1F, 16F, spriteSet.exclude(EnumFacing.WEST, EnumFacing.EAST), rotation);
        YabbaModels.addCube(quads, 0F, 0F, 1F, 1F, 1F, 15F, spriteSet.exclude(EnumFacing.NORTH, EnumFacing.SOUTH), rotation);
        YabbaModels.addCube(quads, 15F, 0F, 1F, 16F, 1F, 15F, spriteSet.exclude(EnumFacing.NORTH, EnumFacing.SOUTH), rotation);

        YabbaModels.addCube(quads, 1F, 15F, 0F, 15F, 16F, 1F, spriteSet.exclude(EnumFacing.WEST, EnumFacing.EAST), rotation);
        YabbaModels.addCube(quads, 1F, 15F, 15F, 15F, 16F, 16F, spriteSet.exclude(EnumFacing.WEST, EnumFacing.EAST), rotation);
        YabbaModels.addCube(quads, 0F, 15F, 1F, 1F, 16F, 15F, spriteSet.exclude(EnumFacing.NORTH, EnumFacing.SOUTH), rotation);
        YabbaModels.addCube(quads, 15F, 15F, 1F, 16F, 16F, 15F, spriteSet.exclude(EnumFacing.NORTH, EnumFacing.SOUTH), rotation);

        return new BarrelVariantBakedModel(spriteSet.get(EnumFacing.NORTH), Collections.unmodifiableList(quads), Maps.immutableEnumMap(transformMap));
    }
}