package com.latmod.yabba.models;

import com.google.common.base.Function;
import com.latmod.yabba.api.IBarrelModelData;
import com.latmod.yabba.api.IIconSet;
import com.latmod.yabba.util.IconSet;
import com.latmod.yabba.util.SpriteSet;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class ModelBarrel extends ModelBase
{
    public static final ModelBarrel INSTANCE = new ModelBarrel();
    private static final IIconSet TEXTURES_BAND = new IconSet("north=yabba:blocks/barrel_band_window,south&east&west=yabba:blocks/barrel_band");

    public ModelBarrel()
    {
        super("barrel");
    }

    @Override
    public Collection<ResourceLocation> getExtraTextures()
    {
        return TEXTURES_BAND.getTextures();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<BakedQuad> buildModel(SpriteSet spriteSet, IBarrelModelData data, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
    {
        List<BakedQuad> quads = new ArrayList<>(52);
        ModelRotation rotation = getRotation(data.getFacing());

        TextureAtlasSprite topSprite = spriteSet.get(EnumFacing.UP);
        TextureAtlasSprite bottomSprite = spriteSet.get(EnumFacing.DOWN);
        TextureAtlasSprite frontSprite = spriteSet.get(EnumFacing.NORTH);

        // Band
        addCube(quads, 0F, 0F, 0F, 16F, 16F, 16F, new SpriteSet(TEXTURES_BAND, textureAtlas), rotation);

        /// Top H
        addCube(quads, 2F, 15F, 2F, 14F, 16F, 14F, spriteSet.exclude(EnumFacing.DOWN), rotation);
        addCube(quads, 1F, 13F, 1F, 15F, 15F, 15F, spriteSet.exclude(EnumFacing.DOWN), rotation);
        addCube(quads, 0F, 12F, 0F, 16F, 13F, 16F, spriteSet.exclude(EnumFacing.DOWN, EnumFacing.UP), rotation);

        // Top V
        addQuad(quads, 0F, 13F, 0F, 16F, 13F, 1F, EnumFacing.UP, topSprite, rotation);
        addQuad(quads, 0F, 13F, 15F, 16F, 13F, 16F, EnumFacing.UP, topSprite, rotation);
        addQuad(quads, 0F, 13F, 1F, 1F, 13F, 15F, EnumFacing.UP, topSprite, rotation);
        addQuad(quads, 15F, 13F, 1F, 16F, 13F, 15F, EnumFacing.UP, topSprite, rotation);

        // Center block
        addCube(quads, 0F, 6F, 0F, 16F, 10F, 16F, spriteSet.exclude(EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH), rotation);

        // Inside borders
        //TODO

        // Front
        addQuad(quads, 4F, 4F, 1F, 12F, 12F, 1F, EnumFacing.NORTH, frontSprite, rotation);
        addQuad(quads, 0F, 6F, 0F, 4F, 10F, 0F, EnumFacing.NORTH, frontSprite, rotation);
        addQuad(quads, 12F, 6F, 0F, 16F, 10F, 0F, EnumFacing.NORTH, frontSprite, rotation);

        // Bottom V
        addQuad(quads, 0F, 3F, 0F, 16F, 3F, 1F, EnumFacing.DOWN, bottomSprite, rotation);
        addQuad(quads, 0F, 3F, 15F, 16F, 3F, 16F, EnumFacing.DOWN, bottomSprite, rotation);
        addQuad(quads, 0F, 3F, 1F, 1F, 3F, 15F, EnumFacing.DOWN, bottomSprite, rotation);
        addQuad(quads, 15F, 3F, 1F, 16F, 3F, 15F, EnumFacing.DOWN, bottomSprite, rotation);

        // Bottom H
        addCube(quads, 0F, 3F, 0F, 16F, 4F, 16F, spriteSet.exclude(EnumFacing.DOWN, EnumFacing.UP), rotation);
        addCube(quads, 1F, 1F, 1F, 15F, 3F, 15F, spriteSet.exclude(EnumFacing.UP), rotation);
        addCube(quads, 2F, 0F, 2F, 14F, 1F, 14F, spriteSet.exclude(EnumFacing.UP), rotation);
        return quads;
    }
}