package com.latmod.yabba;

import com.latmod.yabba.api.BarrelTier;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.block.BlockBarrel;
import net.minecraft.block.state.IBlockState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public enum YabbaRegistry implements IYabbaRegistry
{
    INSTANCE;

    public static class BarrelInstance
    {
        public final String ID;
        public final Object craftItem;
        public final BlockBarrel block;

        public BarrelInstance(String id, Object item, IBlockState parentState)
        {
            ID = id;
            craftItem = item;
            block = new BlockBarrel(parentState);
        }
    }

    public static final Map<String, BarrelInstance> BARRELS = new HashMap<>();
    private static final Map<String, BarrelTier> TIERS = new HashMap<>();
    public static final Map<String, IUpgrade> UPGRADES = new HashMap<>();

    @Override
    public void addBarrel(String id, Object craftItem, IBlockState parentState)
    {
        BARRELS.put(id, new BarrelInstance(id, craftItem, parentState));
    }

    @Override
    public void addTier(BarrelTier tier)
    {
        TIERS.put(tier.getTierID(), tier);
    }

    @Override
    public void addUpgrade(IUpgrade upgrade)
    {
        UPGRADES.put(upgrade.getUpgradeName(), upgrade);
    }

    public BarrelTier getTier(String id)
    {
        BarrelTier tier = TIERS.get(id);
        return tier == null ? BarrelTier.NONE : tier;
    }
}