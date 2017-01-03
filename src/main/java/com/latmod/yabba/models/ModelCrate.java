package com.latmod.yabba.models;

import com.google.common.base.Function;
import com.latmod.yabba.api.IBarrelModelData;
import com.latmod.yabba.util.ModelBuilder;
import com.latmod.yabba.util.SpriteSet;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class ModelCrate extends ModelBase
{
    public static final ModelCrate INSTANCE = new ModelCrate();

    public ModelCrate()
    {
        super("crate");
    }

    @Override
    public Collection<ResourceLocation> getExtraTextures()
    {
        return Collections.emptyList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<BakedQuad> buildModel(SpriteSet spriteSet, IBarrelModelData data, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
    {
        ModelBuilder model = new ModelBuilder(ModelBuilder.getRotation(data.getFacing()));

        model.addCube(1F, 1F, 1F, 15F, 15F, 15F, spriteSet);

        model.addCube(0F, 0F, 0F, 1F, 16F, 1F, spriteSet);
        model.addCube(15F, 0F, 0F, 16F, 16F, 1F, spriteSet);
        model.addCube(0F, 0F, 15F, 1F, 16F, 16F, spriteSet);
        model.addCube(15F, 0F, 15F, 16F, 16F, 16F, spriteSet);

        model.addCube(1F, 0F, 0F, 15F, 1F, 1F, spriteSet.exclude(EnumFacing.WEST, EnumFacing.EAST));
        model.addCube(1F, 0F, 15F, 15F, 1F, 16F, spriteSet.exclude(EnumFacing.WEST, EnumFacing.EAST));
        model.addCube(0F, 0F, 1F, 1F, 1F, 15F, spriteSet.exclude(EnumFacing.NORTH, EnumFacing.SOUTH));
        model.addCube(15F, 0F, 1F, 16F, 1F, 15F, spriteSet.exclude(EnumFacing.NORTH, EnumFacing.SOUTH));

        model.addCube(1F, 15F, 0F, 15F, 16F, 1F, spriteSet.exclude(EnumFacing.WEST, EnumFacing.EAST));
        model.addCube(1F, 15F, 15F, 15F, 16F, 16F, spriteSet.exclude(EnumFacing.WEST, EnumFacing.EAST));
        model.addCube(0F, 15F, 1F, 1F, 16F, 15F, spriteSet.exclude(EnumFacing.NORTH, EnumFacing.SOUTH));
        model.addCube(15F, 15F, 1F, 16F, 16F, 15F, spriteSet.exclude(EnumFacing.NORTH, EnumFacing.SOUTH));
        return model.getQuads();
    }
}