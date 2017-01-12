package com.latmod.yabba.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.List;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class BarrelVariantBakedModel implements IPerspectiveAwareModel
{
    private static final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> TRANSFORM_MAP;
    private static final TRSRTransformation flipX = new TRSRTransformation(null, null, new Vector3f(-1, 1, 1), null);

    private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s)
    {
        return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                new Vector3f(tx / 16, ty / 16, tz / 16),
                TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
                new Vector3f(s, s, s),
                null));
    }

    static
    {
        TRSRTransformation thirdperson = get(0, 2.5F, 0, 75, 45, 0, 0.375F);
        ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> builder = ImmutableMap.builder();
        builder.put(ItemCameraTransforms.TransformType.GUI, get(0, 0, 0, 30, 225, 0, 0.625F));
        builder.put(ItemCameraTransforms.TransformType.GROUND, get(0, 3, 0, 0, 0, 0, 0.25F));
        builder.put(ItemCameraTransforms.TransformType.FIXED, get(0, 0, 0, 0, 0, 0, 0.5F));
        builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, thirdperson);
        builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, TRSRTransformation.blockCenterToCorner(flipX.compose(TRSRTransformation.blockCornerToCenter(thirdperson)).compose(flipX)));
        builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 45, 0, 0.4F));
        builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(0, 0, 0, 0, 225, 0, 0.4F));
        TRANSFORM_MAP = Maps.immutableEnumMap(builder.build());
    }

    private final TextureAtlasSprite particle;
    private final List<BakedQuad> quads;

    public BarrelVariantBakedModel(TextureAtlasSprite p, List<BakedQuad> q)
    {
        particle = p;
        quads = q;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, TRANSFORM_MAP, cameraTransformType);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        return quads;
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