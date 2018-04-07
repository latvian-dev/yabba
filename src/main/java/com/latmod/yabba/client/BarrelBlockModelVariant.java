package com.latmod.yabba.client;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.List;

/**
 * @author LatvianModder
 */
public class BarrelBlockModelVariant
{
	public final List<BakedQuad> solidQuads;
	public final List<BakedQuad> cutoutQuads;
	public final List<BakedQuad> translucentQuads;

	public BarrelBlockModelVariant(List<BakedQuad> s, List<BakedQuad> c, List<BakedQuad> t)
	{
		solidQuads = s;
		cutoutQuads = c;
		translucentQuads = t;
	}
}