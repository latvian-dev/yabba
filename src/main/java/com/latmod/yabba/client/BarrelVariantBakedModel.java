package com.latmod.yabba.client;

import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelVariant;
import com.latmod.yabba.block.BlockBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class BarrelVariantBakedModel implements IBakedModel
{
    private final TextureAtlasSprite particle;
    private final Map<IBarrelVariant, List<BakedQuad>> quads;

    public BarrelVariantBakedModel(TextureAtlasSprite p, Map<IBarrelVariant, List<BakedQuad>> q)
    {
        particle = p;
        quads = q;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        IBarrelVariant variant = null;

        if(state != null)
        {
            variant = state.getValue(BlockBarrel.VARIANT);
        }

        List<BakedQuad> l = quads.get(variant == null ? YabbaRegistry.DEFAULT_VARIANT : variant);
        return l == null ? Collections.emptyList() : l;
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return particle;
    }

    @Deprecated
    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.NONE;
    }
}