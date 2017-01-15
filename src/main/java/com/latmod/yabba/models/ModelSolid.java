package com.latmod.yabba.models;

import com.google.common.base.Function;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.IIconSet;
import com.latmod.yabba.util.IconSet;
import com.latmod.yabba.util.ModelBuilder;
import com.latmod.yabba.util.SpriteSet;
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
public class ModelSolid extends ModelBase
{
    public static final ModelSolid INSTANCE = new ModelSolid();
    private static final IIconSet TEXTURE_WINDOW = new IconSet("north=yabba:blocks/barrel_solid_window");

    public ModelSolid()
    {
        super("solid");
    }

    @Override
    public Collection<ResourceLocation> getExtraTextures()
    {
        return TEXTURE_WINDOW.getTextures();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<BakedQuad> buildModel(IBarrelModel barrelModel, IBarrelSkin skin, EnumFacing rotation, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
    {
        ModelBuilder model = new ModelBuilder(ModelBuilder.getRotation(rotation));
        SpriteSet spriteSet = new SpriteSet(skin.getTextures(), textureAtlas);

        model.addCube(0F, 0F, 0F, 16F, 16F, 16F, spriteSet.exclude(EnumFacing.NORTH));
        model.addQuad(0F, 0F, 0F, 16F, 16F, 0F, EnumFacing.NORTH, textureAtlas.apply(TEXTURE_WINDOW.getTexture(EnumFacing.NORTH)));

        TextureAtlasSprite frontSprite = spriteSet.get(EnumFacing.NORTH);

        model.addInvertedCube(4F, 4F, 0F, 12F, 12F, 1F, spriteSet);
        model.addQuad(0F, 0F, 0F, 16F, 4F, 0F, EnumFacing.NORTH, frontSprite);
        model.addQuad(0F, 12F, 0F, 16F, 16F, 0F, EnumFacing.NORTH, frontSprite);
        model.addQuad(0F, 4F, 0F, 4F, 12F, 0F, EnumFacing.NORTH, frontSprite);
        model.addQuad(12F, 4F, 0F, 16F, 12F, 0F, EnumFacing.NORTH, frontSprite);

        return model.getQuads();
    }
}