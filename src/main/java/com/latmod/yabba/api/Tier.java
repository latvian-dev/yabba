package com.latmod.yabba.api;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public enum Tier implements IStringSerializable
{
    WOOD("wood", 0xFFC69569, 64),
    IRON("iron", 0xFFD8D8D8, 256),
    GOLD("gold", 0xFFFCD803, 1024),
    DIAMOND("dmd", 0xFF00FFFF, 4096);

    public static final Tier[] VALUES = values();

    private final String name;
    public final PropertyInt maxItemStacks;
    public final Color4I color;

    Tier(String n, int c, int i)
    {
        name = n;
        color = new Color4I(false, c);
        maxItemStacks = new PropertyInt(i, 1, Integer.MAX_VALUE);
    }

    @Override
    public String getName()
    {
        return name;
    }

    public int getMaxItems(IBarrel barrel, ItemStack itemStack)
    {
        if(barrel.getFlag(IBarrel.FLAG_INFINITE_CAPACITY))
        {
            return 2000000000;
        }

        return maxItemStacks.getInt() * (itemStack.isEmpty() ? 1 : itemStack.getMaxStackSize());
    }

    public static Tier getFromName(String id)
    {
        switch(id)
        {
            case "iron":
                return IRON;
            case "gold":
                return GOLD;
            case "dmd":
                return DIAMOND;
            default:
                return WOOD;
        }
    }
}