package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.client.ModelBuilder;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.block.BlockBarrel;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class BakedBarrelBlockModel implements IPerspectiveAwareModel
{
    private final TextureAtlasSprite particle;
    private final Map<BarrelModelKey, BarrelModelVariant> map;
    private final ItemOverrideList itemOverrideList = new ItemOverrideList(new ArrayList<>())
    {
        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
        {
            IBarrel barrel = stack.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
            BarrelModelVariant v = map.get(new BarrelModelKey(barrel.getModel(), barrel.getSkin()));
            return v == null ? originalModel : v.itemModel;
        }
    };

    public BakedBarrelBlockModel(TextureAtlasSprite p, Map<BarrelModelKey, BarrelModelVariant> m)
    {
        particle = p;
        map = m;
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
            BarrelModelVariant value = map.get(new BarrelModelKey(state.getValue(BlockBarrel.MODEL), state.getValue(BlockBarrel.SKIN)));

            if(value != null)
            {
                return value.getQuads(state.getValue(BlockBarrel.ROTATION).getModelRotationIndexFromFacing(state.getValue(BlockHorizontal.FACING)));
            }
        }

        return Collections.emptyList();
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
        return itemOverrideList;
    }
}