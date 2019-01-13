package com.latmod.yabba.client;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BarrelBlockModelVariant
{
	public static class Quads
	{
		public List<BakedQuad> solidQuads = new ArrayList<>();
		public List<BakedQuad> cutoutQuads = new ArrayList<>();
		public List<BakedQuad> translucentQuads = new ArrayList<>();
	}

	public final Quads[][] rotations = new Quads[4][7];
}