package com.latmod.yabba.block;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public enum Tier implements IStringSerializable
{
	WOOD("wood", 0, 0xC69569),
	IRON("iron", 1, 0xD8D8D8),
	GOLD("gold", 2, 0xFCD803),
	DIAMOND("diamond", 3, 0x00FFFF),
	STAR("star", 4, 0xAFC9D8);

	public static final int MAX_ITEMS = 2000000000;
	public static final NameMap<Tier> NAME_MAP = NameMap.create(WOOD, values());

	private final String name;
	private final String langKey;
	public int maxItemStacks = MAX_ITEMS / 64;
	public final int tier;
	public final Color4I color;

	Tier(String n, int t, int c)
	{
		name = n;
		langKey = "yabba.tier." + n;
		tier = t;
		color = Color4I.rgb(c);
	}

	@Override
	public String getName()
	{
		return name;
	}

	public String getLangKey()
	{
		return langKey;
	}

	public boolean infiniteCapacity()
	{
		return this == STAR;
	}

	@Nullable
	public Tier getPrevious()
	{
		return this == WOOD ? null : NAME_MAP.getPrevious(this);
	}
}