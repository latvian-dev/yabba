package com.latmod.yabba.api;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public interface IBarrelModel extends IStringSerializable, Comparable<IBarrelModel>
{
    Collection<ResourceLocation> getExtraTextures();

    @SideOnly(Side.CLIENT)
    List<BakedQuad> buildModel(IBarrel barrel, ModelRotation rotation, Function<ResourceLocation, TextureAtlasSprite> textureAtlas);

    @Nullable
    @SideOnly(Side.CLIENT)
    default List<BakedQuad> buildItemModel(IBarrel barrel, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
    {
        return null;
    }

    default float getTextDistance()
    {
        return -0.005F;
    }

    default float getItemDistance()
    {
        return 0.04F;
    }

    default AxisAlignedBB getAABB(IBlockState state, IBlockAccess world, BlockPos pos, IBarrel barrel)
    {
        return Block.FULL_BLOCK_AABB;
    }
}