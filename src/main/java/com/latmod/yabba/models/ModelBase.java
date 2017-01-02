package com.latmod.yabba.models;

import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.util.SpriteSet;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public abstract class ModelBase implements IBarrelModel
{
    private final String name;

    public ModelBase(String id)
    {
        name = id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return name;
    }

    public int hashCode()
    {
        return name.hashCode();
    }

    public boolean equals(Object o)
    {
        return o == this || o != null && o.toString().equals(name);
    }

    @Override
    public int compareTo(IBarrelModel o)
    {
        return getName().compareTo(o.getName());
    }

    // Static //

    @SideOnly(Side.CLIENT)
    private static final FaceBakery BAKERY = new FaceBakery();

    public static ModelRotation getRotation(EnumFacing facing)
    {
        switch(facing)
        {
            case SOUTH:
                return ModelRotation.X0_Y180;
            case EAST:
                return ModelRotation.X0_Y90;
            case WEST:
                return ModelRotation.X0_Y270;
            default:
                return ModelRotation.X0_Y0;
        }
    }

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