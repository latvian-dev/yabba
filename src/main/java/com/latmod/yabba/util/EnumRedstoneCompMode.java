package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.lib.EnumNameMap;
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
    public static final EnumRedstoneCompMode[] VALUES = values();
    public static final EnumNameMap<EnumRedstoneCompMode> NAME_MAP = new EnumNameMap<>(VALUES, false);

    public static EnumRedstoneCompMode getMode(int mode)
    {
        return (mode < 0 || mode >= VALUES.length) ? EQUAL : VALUES[mode];
    }

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
        switch(this)
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