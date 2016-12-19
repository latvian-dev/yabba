package com.latmod.yabba.client;

import com.google.common.base.Function;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.IconSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class YabbaModels implements ICustomModelLoader
{
    @Override
    public boolean accepts(ResourceLocation modelLocation)
    {
        return modelLocation.getResourceDomain().equals(Yabba.MOD_ID) && (modelLocation.getResourcePath().equals("crate"));
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception
    {
        return new CrateModel();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
    }

    public static class BarrelStatemapper extends StateMapperBase
    {
        private final ModelResourceLocation LOCATION = new ModelResourceLocation(YabbaItems.BARREL.getRegistryName(), "normal");

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            return LOCATION;
        }
    }

    public static class CrateStatemapper extends StateMapperBase
    {
        private final ModelResourceLocation LOCATION = new ModelResourceLocation(YabbaItems.CRATE.getRegistryName(), "normal");

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            return LOCATION;
        }
    }

    // Static //

    private static final FaceBakery BAKERY = new FaceBakery();

    public static class SpriteSet
    {
        private final IconSet iconSet;
        private final TextureAtlasSprite sprites[];

        public SpriteSet(IconSet set, Function<ResourceLocation, TextureAtlasSprite> function)
        {
            iconSet = set;
            sprites = new TextureAtlasSprite[6];

            for(int i = 0; i < 6; i++)
            {
                sprites[i] = function.apply(iconSet.getTexture(i));
            }
        }
    }

    public static void addCube(List<BakedQuad> quads, float fromX, float fromY, float fromZ, float toX, float toY, float toZ, SpriteSet sprites, ModelRotation rotation)
    {
        for(EnumFacing face : EnumFacing.VALUES)
        {
            addQuad(quads, fromX, fromY, fromZ, toX, toY, toZ, face, sprites.sprites[face.ordinal()], rotation);
        }
    }

    public static void addQuad(List<BakedQuad> quads, float fromX, float fromY, float fromZ, float toX, float toY, float toZ, EnumFacing face, TextureAtlasSprite sprite, ModelRotation rotation)
    {
        float[] uv = new float[4];
        switch(face)
        {
            case DOWN:
            case UP:
                uv[0] = fromX;
                uv[1] = fromZ;
                uv[2] = toX;
                uv[3] = toZ;
                break;
            /*
            case NORTH:
            case SOUTH:
                uv[0] = fromX;
                uv[1] = toY;
                uv[2] = toX;
                uv[3] = fromY;
                break;
            case EAST:
            case WEST:
                uv[0] = fromZ;
                uv[1] = toY;
                uv[2] = toZ;
                uv[3] = fromY;
                break;
            */
            case NORTH:
            case SOUTH:
                uv[0] = 0F;
                uv[1] = 0F;
                uv[2] = 16F;
                uv[3] = 16F;
                break;
            case EAST:
            case WEST:
                uv[0] = 0F;
                uv[1] = 0F;
                uv[2] = 16F;
                uv[3] = 16F;
                break;
        }

        quads.add(BAKERY.makeBakedQuad(new Vector3f(fromX, fromY, fromZ), new Vector3f(toX, toY, toZ), new BlockPartFace(face, -1, "", new BlockFaceUV(uv, 0)), sprite, face, rotation, null, false, true));
    }
}