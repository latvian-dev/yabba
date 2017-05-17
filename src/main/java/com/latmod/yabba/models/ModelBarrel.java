package com.latmod.yabba.models;

import com.feed_the_beast.ftbl.lib.IconSet;
import com.feed_the_beast.ftbl.lib.client.ModelBuilder;
import com.feed_the_beast.ftbl.lib.client.SpriteSet;
import com.google.common.base.Function;
import com.latmod.yabba.api.IBarrel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ModelBarrel extends ModelBase
{
    public static final ModelBarrel INSTANCE = new ModelBarrel();
    private static final IconSet TEXTURES_BAND = new IconSet("north=yabba:blocks/barrel_band_window,south&east&west=yabba:blocks/barrel_band");

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
    public List<BakedQuad> buildModel(IBarrel barrel, ModelRotation rotation, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
    {
        ModelBuilder model = new ModelBuilder(rotation);
        SpriteSet spriteSet = new SpriteSet(barrel.getSkin().getTextures(), textureAtlas);

        TextureAtlasSprite topSprite = spriteSet.get(EnumFacing.UP);
        TextureAtlasSprite bottomSprite = spriteSet.get(EnumFacing.DOWN);
        TextureAtlasSprite frontSprite = spriteSet.get(EnumFacing.NORTH);

        // Band
        model.addCube(0F, 0F, 0F, 16F, 16F, 16F, new SpriteSet(TEXTURES_BAND, textureAtlas));

        /// Top H
        model.addCube(2F, 15F, 2F, 14F, 16F, 14F, spriteSet.exclude(EnumFacing.DOWN));
        model.addCube(1F, 13F, 1F, 15F, 15F, 15F, spriteSet.exclude(EnumFacing.DOWN));
        model.addCube(0F, 12F, 0F, 16F, 13F, 16F, spriteSet.exclude(EnumFacing.DOWN, EnumFacing.UP));

        // Top V
        model.addQuad(0F, 13F, 0F, 16F, 13F, 1F, EnumFacing.UP, topSprite);
        model.addQuad(0F, 13F, 15F, 16F, 13F, 16F, EnumFacing.UP, topSprite);
        model.addQuad(0F, 13F, 1F, 1F, 13F, 15F, EnumFacing.UP, topSprite);
        model.addQuad(15F, 13F, 1F, 16F, 13F, 15F, EnumFacing.UP, topSprite);

        // Center block
        model.addCube(0F, 6F, 0F, 16F, 10F, 16F, spriteSet.exclude(EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH));

        // Inside borders
        model.addInvertedCube(4F, 4F, 0F, 12F, 12F, 1F, spriteSet);

        // Front
        model.addQuad(0F, 6F, 0F, 4F, 10F, 0F, EnumFacing.NORTH, frontSprite);
        model.addQuad(12F, 6F, 0F, 16F, 10F, 0F, EnumFacing.NORTH, frontSprite);

        // Bottom V
        model.addQuad(0F, 3F, 0F, 16F, 3F, 1F, EnumFacing.DOWN, bottomSprite);
        model.addQuad(0F, 3F, 15F, 16F, 3F, 16F, EnumFacing.DOWN, bottomSprite);
        model.addQuad(0F, 3F, 1F, 1F, 3F, 15F, EnumFacing.DOWN, bottomSprite);
        model.addQuad(15F, 3F, 1F, 16F, 3F, 15F, EnumFacing.DOWN, bottomSprite);

        // Bottom H
        model.addCube(0F, 3F, 0F, 16F, 4F, 16F, spriteSet.exclude(EnumFacing.DOWN, EnumFacing.UP));
        model.addCube(1F, 1F, 1F, 15F, 3F, 15F, spriteSet.exclude(EnumFacing.UP));
        model.addCube(2F, 0F, 2F, 14F, 1F, 14F, spriteSet.exclude(EnumFacing.UP));
        return model.getQuads();
    }
}