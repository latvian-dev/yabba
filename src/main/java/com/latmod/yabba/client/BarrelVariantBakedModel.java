package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.client.ModelBuilder;
import com.latmod.yabba.block.BlockBarrel;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BarrelVariantBakedModel implements IPerspectiveAwareModel
{
    private final TextureAtlasSprite particle;
    private final List<List<BakedQuad>> quads;
    private final List<BakedQuad> noStateQuads;

    public BarrelVariantBakedModel(TextureAtlasSprite p, List<List<BakedQuad>> q, List<BakedQuad> nsq)
    {
        particle = p;
        quads = q;
        noStateQuads = nsq;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, ModelBuilder.TRANSFORM_MAP, cameraTransformType);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        if(state != null)
        {
            return quads.get(state.getValue(BlockBarrel.ROTATION).getModelRotationIndexFromFacing(state.getValue(BlockHorizontal.FACING)));
        }

        return noStateQuads;
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