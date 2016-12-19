package com.latmod.yabba;

import com.latmod.yabba.api.IBarrelTier;
import com.latmod.yabba.api.IBarrelVariant;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.IconSet;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public enum YabbaRegistry implements IYabbaRegistry
{
    INSTANCE;

    public static final Map<String, IBarrelVariant> BARRELS = new HashMap<>();
    public static final Collection<IBarrelVariant> BARRELS_VALUES = new ArrayList<>();
    private static final Map<String, IBarrelTier> TIERS = new HashMap<>();
    public static final Map<String, IUpgrade> UPGRADES = new HashMap<>();
    public static IBarrelVariant DEFAULT_VARIANT;

    @Override
    public IBarrelVariant addVariant(String id, IBlockState parentState, @Nullable Object craftItem, IconSet icons)
    {
        IBarrelVariant c = new BarrelVariant(id, parentState, craftItem, icons);
        BARRELS.put(id, c);
        return c;
    }

    @Override
    public void addTier(IBarrelTier tier)
    {
        TIERS.put(tier.getName(), tier);
    }

    @Override
    public void addUpgrade(IUpgrade upgrade)
    {
        UPGRADES.put(upgrade.getUpgradeName(), upgrade);
    }

    public IBarrelTier getTier(String id)
    {
        IBarrelTier tier = TIERS.get(id);
        return tier == null ? BarrelTier.NONE : tier;
    }

    public IBarrelVariant getVariant(String id)
    {
        IBarrelVariant v = BARRELS.get(id);
        return v == null ? DEFAULT_VARIANT : v;
    }
}