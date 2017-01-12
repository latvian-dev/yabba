package com.latmod.yabba.client;

import com.google.common.base.Function;
import com.latmod.yabba.api.IBarrelModelData;
import com.latmod.yabba.util.BarrelModelData;
import com.latmod.yabba.util.SpriteSet;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class BarrelModel implements IModel
{
    public final IBarrelModelData data;
    public Collection<ResourceLocation> textures;

    public BarrelModel(String v)
    {
        data = new BarrelModelData(v);

        Collection<ResourceLocation> tex = data.getSkin().getTextures().getTextures();
        tex.addAll(data.getModel().getExtraTextures());
        textures = Collections.unmodifiableCollection(tex);
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        return textures;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        SpriteSet spriteSet = new SpriteSet(data.getSkin().getTextures(), bakedTextureGetter);
        List<BakedQuad> quads = data.getModel().buildModel(spriteSet, data, bakedTextureGetter);
        return new BarrelVariantBakedModel(spriteSet.get(EnumFacing.NORTH), Collections.unmodifiableList(quads));
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }
}