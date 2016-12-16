package com.latmod.yabba.block;

import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.ITier;
import com.latmod.yabba.item.EnumUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Created by LatvianModder on 12.12.2016.
 */
public enum EnumTier implements ITier, IStringSerializable
{
    TIER_0(32 * 64),
    TIER_1(128 * 64),
    TIER_2(512 * 64),
    TIER_3(2048 * 64),
    TIER_INF(Integer.MAX_VALUE);

    public static final EnumTier[] VALUES = values();

    private final String name;
    private final int capacity;

    EnumTier(int cap)
    {
        name = name().toLowerCase(Locale.ENGLISH);
        capacity = cap;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public byte getTierID()
    {
        return (byte) ordinal();
    }

    @Override
    public int getCapacity()
    {
        return capacity;
    }

    @Override
    @Nullable
    public Object getNextTierUpgradeItem()
    {
        switch(this)
        {
            case TIER_0:
                return new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.IRON_UPGRADE.metadata);
            case TIER_1:
                return new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.GOLD_UPGRADE.metadata);
            case TIER_2:
                return new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.DIAMOND_UPGRADE.metadata);
            case TIER_3:
                return new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.NETHER_STAR_UPGRADE.metadata);
        }

        return null;
    }

    @Override
    public ITier getNextTier()
    {
        return this == TIER_INF ? null : VALUES[ordinal() + 1];
    }
}