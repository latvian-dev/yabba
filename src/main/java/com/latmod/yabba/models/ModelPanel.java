package com.latmod.yabba.models;

import com.feed_the_beast.ftbl.lib.client.ModelBuilder;
import com.feed_the_beast.ftbl.lib.client.SpriteSet;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.google.common.base.Function;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.block.BlockBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class ModelPanel extends ModelBase
{
    private final float height;
    private final AxisAlignedBB[] boxes;

    public ModelPanel(String id, float h)
    {
        super(id);
        height = h;
        boxes = MathUtils.getRotatedBoxes(new AxisAlignedBB(0D, 1D - height, 0D, 1D, 1D, 1D));
    }

    @Override
    public Collection<ResourceLocation> getExtraTextures()
    {
        return Collections.emptyList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<BakedQuad> buildModel(IBarrel barrel, ModelRotation rotation, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
    {
        ModelBuilder model = new ModelBuilder(rotation);
        SpriteSet spriteSet = new SpriteSet(barrel.getSkin().getTextures(), textureAtlas);
        model.addCube(0F, 0F, 16F - height * 16F, 16F, 16F, 16F, spriteSet);
        return model.getQuads();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<BakedQuad> buildItemModel(IBarrel barrel, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
    {
        ModelBuilder model = new ModelBuilder(ModelRotation.X0_Y0);
        SpriteSet spriteSet = new SpriteSet(barrel.getSkin().getTextures(), textureAtlas);
        model.addCube(0F, 0F, 8F - height * 8F, 16F, 16F, 8F + height * 8F, spriteSet);
        return model.getQuads();
    }

    @Override
    public float getTextDistance()
    {
        return 1F - height - 0.005F;
    }

    @Override
    public float getItemDistance()
    {
        return 1F - height - 0.01F;
    }

    @Override
    public AxisAlignedBB getAABB(IBlockState state, IBlockAccess world, BlockPos pos, IBarrel barrel)
    {
        return boxes[BlockBarrel.normalizeFacing(state).getIndex()];
    }
}