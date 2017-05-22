package com.latmod.yabba.client;

import com.google.common.base.Function;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class BarrelModel implements IModel
{
    public Collection<ResourceLocation> textures;

    public BarrelModel()
    {
        textures = new HashSet<>();

        for(IBarrelModel model : YabbaRegistry.ALL_MODELS)
        {
            textures.addAll(model.getExtraTextures());
        }

        for(IBarrelSkin skin : YabbaRegistry.ALL_SKINS)
        {
            textures.addAll(skin.getTextures().getTextures());
        }
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
        TextureAtlasSprite particle = bakedTextureGetter.apply(new ResourceLocation("blocks/planks_oak"));
        Map<BarrelModelKey, BarrelModelVariant> map = new HashMap<>();

        for(IBarrelModel model : YabbaRegistry.ALL_MODELS)
        {
            for(IBarrelSkin skin : YabbaRegistry.ALL_SKINS)
            {
                List<List<BakedQuad>> quads = new ArrayList<>(ModelRotation.values().length);

                for(ModelRotation rotation : ModelRotation.values())
                {
                    quads.add(model.buildModel(skin, rotation, bakedTextureGetter));
                }

                List<BakedQuad> itemQuads = model.buildItemModel(skin, bakedTextureGetter);
                map.put(new BarrelModelKey(model, skin), new BarrelModelVariant(quads, new BakedBarrelItemModel(particle, itemQuads == null ? quads.get(0) : itemQuads)));
            }
        }

        return new BakedBarrelBlockModel(particle, map);
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }
}