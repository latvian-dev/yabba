package com.latmod.yabba.block;

import com.feed_the_beast.ftbl.lib.util.LangKey;
import com.feed_the_beast.ftbl.lib.util.misc.Color4I;
import com.feed_the_beast.ftbl.lib.util.misc.NameMap;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public enum Tier implements IStringSerializable
{
	WOOD("wood", 0xC69569),
	IRON("iron", 0xD8D8D8),
	GOLD("gold", 0xFCD803),
	DIAMOND("diamond", 0x00FFFF),
	STAR("star", 0xAFC9D8),
	CREATIVE("creative", 0xFF00FF);

	public static final int MAX_ITEMS = 2000000000;
	public static final NameMap<Tier> NAME_MAP = NameMap.create(WOOD, values());

	private final String name;
	public final LangKey langKey;
	public int maxItemStacks = MAX_ITEMS / 64;
	public final Color4I color;

	Tier(String n, int c)
	{
		name = n;
		langKey = LangKey.of("lang.yabba.tier." + n);
		color = Color4I.rgb(c);
	}

	@Override
	public String getName()
	{
		return name;
	}

	public boolean creative()
	{
		return this == CREATIVE;
	}

	public boolean infiniteCapacity()
	{
		return this == STAR || this == CREATIVE;
	}

	@Nullable
	public Tier getPrevious()
	{
		switch (this)
		{
			case IRON:
				return WOOD;
			case GOLD:
				return IRON;
			case DIAMOND:
				return GOLD;
			case STAR:
				return DIAMOND;
			case CREATIVE:
				return STAR;
			default:
				return null;
		}
	}
}