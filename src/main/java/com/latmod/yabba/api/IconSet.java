package com.latmod.yabba.api;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 20.12.2016.
 */
public final class IconSet
{
    public static IconSet pf(String down, String up, String north, String south, String west, String east)
    {
        return new IconSet(new ResourceLocation(down), new ResourceLocation(up), new ResourceLocation(north), new ResourceLocation(south), new ResourceLocation(west), new ResourceLocation(east));
    }

    public static IconSet all(String tex)
    {
        ResourceLocation texRL = new ResourceLocation(tex);
        return new IconSet(texRL, texRL, texRL, texRL, texRL, texRL);
    }

    public static IconSet topAndSide(String top, String side)
    {
        ResourceLocation topRL = new ResourceLocation(top);
        ResourceLocation sideRL = new ResourceLocation(side);
        return new IconSet(topRL, topRL, sideRL, sideRL, sideRL, sideRL);
    }

    private final ResourceLocation[] textures;

    private IconSet(ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation west, ResourceLocation east)
    {
        textures = new ResourceLocation[6];
        textures[0] = down;
        textures[1] = up;
        textures[2] = north;
        textures[3] = south;
        textures[4] = west;
        textures[5] = east;
    }

    public ResourceLocation getTexture(int face)
    {
        return textures[face];
    }
}