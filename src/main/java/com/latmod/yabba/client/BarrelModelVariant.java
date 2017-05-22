package com.latmod.yabba.client;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;

import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BarrelModelVariant
{
    public final List<List<BakedQuad>> quads;
    public final IBakedModel itemModel;

    public BarrelModelVariant(List<List<BakedQuad>> q, IBakedModel i)
    {
        quads = q;
        itemModel = i;
    }

    public List<BakedQuad> getQuads(int rotation)
    {
        List<BakedQuad> list = rotation < 0 || rotation >= quads.size() ? null : quads.get(rotation);
        return list == null ? Collections.emptyList() : list;
    }
}