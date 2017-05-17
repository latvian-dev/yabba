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
public class ModelSolid extends ModelBase
{
    private static final IconSet TEXTURE_WINDOW = new IconSet("north=yabba:blocks/barrel_solid_window");

    public ModelSolid(String id)
    {
        super(id);
    }

    @Override
    public Collection<ResourceLocation> getExtraTextures()
    {
        return TEXTURE_WINDOW.getTextures();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<BakedQuad> buildModel(IBarrel barrel, ModelRotation rotation, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
    {
        ModelBuilder model = new ModelBuilder(rotation);
        SpriteSet spriteSet = new SpriteSet(barrel.getSkin().getTextures(), textureAtlas);

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