package com.latmod.yabba.models;

import com.feed_the_beast.ftbl.lib.IconSet;
import com.feed_the_beast.ftbl.lib.client.ModelBuilder;
import com.feed_the_beast.ftbl.lib.client.SpriteSet;
import com.google.common.base.Function;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.List;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class ModelSolidBorders extends ModelBase
{
    public static final ModelSolidBorders INSTANCE = new ModelSolidBorders();
    private static final IconSet TEXTURES_EDGES = new IconSet("north=yabba:blocks/barrel_solid_borders_window,all=yabba:blocks/barrel_solid_borders");

    public ModelSolidBorders()
    {
        super("solid_borders");
    }

    @Override
    public Collection<ResourceLocation> getExtraTextures()
    {
        return TEXTURES_EDGES.getTextures();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<BakedQuad> buildModel(IBarrelModel barrelModel, IBarrelSkin skin, EnumFacing rotation, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
    {
        ModelBuilder model = new ModelBuilder(ModelBuilder.getRotation(rotation));
        SpriteSet spriteSet = new SpriteSet(skin.getTextures(), textureAtlas);

        model.addCube(0F, 0F, 0F, 16F, 16F, 16F, new SpriteSet(TEXTURES_EDGES, textureAtlas));
        model.addQuad(1F, 16F, 1F, 15F, 16F, 15F, EnumFacing.UP, spriteSet.get(EnumFacing.UP));
        model.addQuad(1F, 0F, 1F, 15F, 0F, 15F, EnumFacing.DOWN, spriteSet.get(EnumFacing.DOWN));
        model.addQuad(0F, 1F, 1F, 0F, 15F, 15F, EnumFacing.WEST, spriteSet.get(EnumFacing.WEST));
        model.addQuad(16F, 1F, 1F, 16F, 15F, 15F, EnumFacing.EAST, spriteSet.get(EnumFacing.EAST));
        model.addQuad(1F, 1F, 16F, 15F, 15F, 16F, EnumFacing.SOUTH, spriteSet.get(EnumFacing.SOUTH));

        TextureAtlasSprite frontSprite = spriteSet.get(EnumFacing.NORTH);

        model.addInvertedCube(4F, 4F, 0F, 12F, 12F, 1F, spriteSet);
        model.addQuad(1F, 1F, 0F, 15F, 4F, 0F, EnumFacing.NORTH, frontSprite);
        model.addQuad(1F, 12F, 0F, 15F, 15F, 0F, EnumFacing.NORTH, frontSprite);
        model.addQuad(1F, 4F, 0F, 4F, 12F, 0F, EnumFacing.NORTH, frontSprite);
        model.addQuad(12F, 4F, 0F, 15F, 12F, 0F, EnumFacing.NORTH, frontSprite);

        return model.getQuads();
    }
}