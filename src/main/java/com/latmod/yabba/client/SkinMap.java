package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class SkinMap
{
	public static final String[] FACE_NAMES = {"skin_d", "skin_u", "skin_n", "skin_s", "skin_w", "skin_e"};

	public static SkinMap of(String v)
	{
		Map<String, String> textures = new HashMap<>();
		Map<String, String> map = StringUtils.parse(StringUtils.TEMP_MAP, v);

		String s = map.get("all");

		for (String face : FACE_NAMES)
		{
			textures.put(face, s == null ? "blocks/planks_oak" : s);
		}

		for (EnumFacing facing : EnumFacing.VALUES)
		{
			s = map.get(facing.getName());

			if (s != null)
			{
				textures.put(FACE_NAMES[facing.getIndex()], s);
			}
		}

		return new SkinMap(textures);
	}

	public static SkinMap of(JsonElement json)
	{
		return of(json.getAsString());
	}

	public final ImmutableMap<String, String> textures;

	private SkinMap(Map<String, String> map)
	{
		textures = ImmutableMap.<String, String>builder().putAll(map).build();
	}

	public Collection<ResourceLocation> getTextures()
	{
		HashSet<ResourceLocation> set = new HashSet<>();

		for (String s : textures.values())
		{
			set.add(new ResourceLocation(s));
		}

		return set;
	}

	public TextureAtlasSprite get(EnumFacing facing)
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(textures.get(FACE_NAMES[facing.getIndex()]));
	}
}