package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.google.common.base.Function;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class BarrelModel implements IModel
{
    public final IBarrelModel model;
    public final IBarrelSkin skin;
    public Collection<ResourceLocation> textures;

    public BarrelModel(String v)
    {
        Map<String, String> map = LMStringUtils.parse(LMStringUtils.TEMP_MAP, v);
        skin = YabbaRegistry.INSTANCE.getSkin(map.get("skin"));
        model = YabbaRegistry.INSTANCE.getModel(map.get("model"));

        Collection<ResourceLocation> tex = skin.getTextures().getTextures();
        tex.addAll(model.getExtraTextures());
        textures = Collections.unmodifiableCollection(new ArrayList<>(tex));
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
        List<BakedQuad> quadsN, quadsS, quadsW, quadsE;
        quadsN = new ArrayList<>(model.buildModel(model, skin, EnumFacing.NORTH, bakedTextureGetter));
        quadsS = new ArrayList<>(model.buildModel(model, skin, EnumFacing.SOUTH, bakedTextureGetter));
        quadsW = new ArrayList<>(model.buildModel(model, skin, EnumFacing.WEST, bakedTextureGetter));
        quadsE = new ArrayList<>(model.buildModel(model, skin, EnumFacing.EAST, bakedTextureGetter));
        return new BarrelVariantBakedModel(bakedTextureGetter.apply(skin.getTextures().getTexture(EnumFacing.NORTH)), quadsN, quadsS, quadsW, quadsE);
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }
}