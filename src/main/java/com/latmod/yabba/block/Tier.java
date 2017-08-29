package com.latmod.yabba.block;

import com.feed_the_beast.ftbl.api.IWithMetadata;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.NameMap;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public enum Tier implements IStringSerializable, IWithMetadata
{
	WOOD("wood", 0xC69569, 64),
	IRON("iron", 0xD8D8D8, 256),
	GOLD("gold", 0xFCD803, 1024),
	DIAMOND("dmd", 0x00FFFF, 4096);

	public static final NameMap<Tier> NAME_MAP = NameMap.create(WOOD, values());

	private final String name;
	public final PropertyInt maxItemStacks;
	public final Color4I color;

	Tier(String n, int c, int i)
	{
		name = n;
		color = Color4I.rgb(c);
		maxItemStacks = new PropertyInt(i, 1, Integer.MAX_VALUE);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public int getMetadata()
	{
		return ordinal();
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
			default:
				return null;
		}
	}
}