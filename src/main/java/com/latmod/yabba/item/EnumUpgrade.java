package com.latmod.yabba.item;

import com.latmod.yabba.api.IUpgrade;

import java.util.Locale;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public enum EnumUpgrade implements IUpgrade
{
    BLANK(0),
    IRON_UPGRADE(1),
    GOLD_UPGRADE(2),
    DIAMOND_UPGRADE(3),
    NETHER_STAR_UPGRADE(4),
    LOCKED(10),
    OBSIDIAN_SHELL(11),
    REDSTONE_OUT(12),
    HOPPER(13),
    ENDER_LINK(14);

    public static final EnumUpgrade[] VALUES = values();

    private final String name;
    private final String uname;
    public final int metadata;

    EnumUpgrade(int meta)
    {
        name = name().toLowerCase(Locale.ENGLISH);
        uname = "item.yabba.upgrade." + name;
        metadata = meta;
    }

    public static EnumUpgrade getFromMeta(int meta)
    {
        for(EnumUpgrade t : VALUES)
        {
            if(t.metadata == meta)
            {
                return t;
            }
        }

        return BLANK;
    }

    @Override
    public String getUpgradeName()
    {
        return name;
    }

    @Override
    public String getUnlocalizedName()
    {
        return uname;
    }
}
