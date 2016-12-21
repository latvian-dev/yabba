package com.latmod.yabba;

import com.latmod.yabba.api.IBarrelTier;
import com.latmod.yabba.api.IBarrelVariant;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.util.BarrelTier;
import com.latmod.yabba.util.BarrelVariant;
import com.latmod.yabba.util.IconSet;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public enum YabbaRegistry implements IYabbaRegistry
{
    INSTANCE;

    public static final Map<String, IBarrelVariant> BARRELS = new HashMap<>();
    public static final List<IBarrelVariant> ALL_BARRELS = new ArrayList<>();
    private static final Map<String, IBarrelTier> TIERS = new HashMap<>();
    public static final Map<String, IUpgrade> UPGRADES = new HashMap<>();
    public static final IBarrelVariant DEFAULT_VARIANT = INSTANCE.addVariant("planks_oak", Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.OAK.getMetadata()), "all=blocks/planks_oak");

    @Override
    public IBarrelVariant addVariant(String id, IBlockState parentState, @Nullable Object craftItem, String icons)
    {
        IBarrelVariant c = new BarrelVariant(id, parentState, craftItem, new IconSet(icons));
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