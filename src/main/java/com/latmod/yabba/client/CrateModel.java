package com.latmod.yabba.client;

import com.google.common.base.Function;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelVariant;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class CrateModel implements IModel
{
    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        return Collections.emptyList();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        Map<IBarrelVariant, List<BakedQuad>> map = new HashMap<>();

        for(IBarrelVariant variant : YabbaRegistry.BARRELS_VALUES)
        {
            List<BakedQuad> quads = new ArrayList<>(6);
            YabbaModels.addCube(quads, 0F, 0F, 0F, 16F, 16F, 16F, new YabbaModels.SpriteSet(variant.getTextures(), bakedTextureGetter), ModelRotation.X0_Y0);
            map.put(variant, Collections.unmodifiableList(quads));
        }

        return new BarrelVariantBakedModel(bakedTextureGetter.apply(new ResourceLocation("blocks/planks_oak")), Collections.unmodifiableMap(map));
    }

    @Override
    public IModelState getDefaultState()
    {
        return null;
    }
}