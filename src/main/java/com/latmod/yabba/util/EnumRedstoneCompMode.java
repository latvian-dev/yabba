package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.lib.NameMap;
import net.minecraft.util.IStringSerializable;

public enum EnumRedstoneCompMode implements IStringSerializable
{
	EQUAL("=="),
	NOT("!="),
	GREATER_THAN(">"),
	GREATER_THAN_OR_EQUAL(">="),
	LESS_THAN("<"),
	LESS_THAN_OR_EQUAL("<=");

	/**
	 * @author LatvianModder
	 */
	public static final NameMap<EnumRedstoneCompMode> NAME_MAP = NameMap.create(GREATER_THAN_OR_EQUAL, values());

	private final String name;

	EnumRedstoneCompMode(String s)
	{
		name = s;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public boolean matchesCount(int items1, int items2)
	{
		switch (this)
		{
			case EQUAL:
				return items1 == items2;
			case NOT:
				return items1 != items2;
			case GREATER_THAN:
				return items1 > items2;
			case GREATER_THAN_OR_EQUAL:
				return items1 >= items2;
			case LESS_THAN:
				return items1 < items2;
			case LESS_THAN_OR_EQUAL:
				return items1 <= items2;
			default:
				return false;
		}
	}
}