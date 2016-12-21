package com.latmod.yabba.client;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.util.SpriteSet;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class YabbaModels implements ICustomModelLoader
{
    @Override
    public boolean accepts(ResourceLocation modelLocation)
    {
        return modelLocation instanceof ModelResourceLocation && modelLocation.getResourceDomain().equals(Yabba.MOD_ID) && (modelLocation.getResourcePath().equals("barrel") || modelLocation.getResourcePath().equals("crate"));
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception
    {
        String v = ((ModelResourceLocation) modelLocation).getVariant();
        switch(modelLocation.getResourcePath())
        {
            case "barrel":
                return new ModelBarrel(v);
            case "crate":
                return new ModelCrate(v);
        }

        return null;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
    }

    // Static //

    private static final FaceBakery BAKERY = new FaceBakery();

    public static void addCube(List<BakedQuad> quads, float fromX, float fromY, float fromZ, float toX, float toY, float toZ, SpriteSet sprites, ModelRotation rotation)
    {
        for(EnumFacing facing : EnumFacing.VALUES)
        {
            addQuad(quads, fromX, fromY, fromZ, toX, toY, toZ, facing, sprites.get(facing), rotation);
        }
    }

    public static void addQuad(List<BakedQuad> quads, float fromX, float fromY, float fromZ, float toX, float toY, float toZ, EnumFacing face, @Nullable TextureAtlasSprite sprite, ModelRotation rotation)
    {
        if(sprite == null)
        {
            return;
        }

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
            case NORTH:
            case SOUTH:
                uv[0] = fromX;
                uv[1] = fromY;
                uv[2] = toX;
                uv[3] = toY;
                break;
            case EAST:
            case WEST:
                uv[0] = fromZ;
                uv[1] = fromY;
                uv[2] = toZ;
                uv[3] = toY;
                break;
        }

        quads.add(BAKERY.makeBakedQuad(new Vector3f(fromX, fromY, fromZ), new Vector3f(toX, toY, toZ), new BlockPartFace(face, -1, "", new BlockFaceUV(uv, 0)), sprite, face, rotation, null, false, true));
    }
}