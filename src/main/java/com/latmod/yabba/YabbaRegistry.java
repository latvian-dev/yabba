package com.latmod.yabba;

import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.ITier;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.YabbaRegistryEvent;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.util.BarrelSkin;
import com.latmod.yabba.util.EnumUpgrade;
import com.latmod.yabba.util.IconSet;
import com.latmod.yabba.util.Tier;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

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

    private static final Map<String, IBarrelSkin> SKINS = new HashMap<>();
    private static final Map<String, ITier> TIERS = new HashMap<>();
    private static final Map<String, IUpgrade> UPGRADES = new HashMap<>();
    private static final Map<String, IBarrelModel> MODELS = new HashMap<>();

    public static final List<IBarrelSkin> ALL_SKINS = new ArrayList<>();
    public static final List<IBarrelModel> ALL_MODELS = new ArrayList<>();

    public static final IBarrelSkin DEFAULT_SKIN = INSTANCE.addSkin(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.OAK.getMetadata()), "all=blocks/planks_oak");

    public void sendEvent()
    {
        MinecraftForge.EVENT_BUS.post(new YabbaRegistryEvent(this));
        YabbaRegistry.ALL_SKINS.addAll(YabbaRegistry.SKINS.values());
        YabbaRegistry.ALL_MODELS.addAll(YabbaRegistry.MODELS.values());
    }

    @Override
    public IBarrelSkin addSkin(IBlockState parentState, @Nullable Object craftItem, String icons)
    {
        IBarrelSkin c = new BarrelSkin(parentState, craftItem, new IconSet(icons));
        SKINS.put(c.getName(), c);
        return c;
    }

    @Override
    public void addTier(ITier tier)
    {
        TIERS.put(tier.getName(), tier);
    }

    @Override
    public void addUpgrade(IUpgrade upgrade)
    {
        UPGRADES.put(upgrade.getUpgradeName(), upgrade);
    }

    @Override
    public void addModel(IBarrelModel model)
    {
        MODELS.put(model.getName(), model);
    }

    public IBarrelSkin getSkin(String id)
    {
        IBarrelSkin skin = SKINS.get(id);
        return skin == null ? DEFAULT_SKIN : skin;
    }

    public ITier getTier(String id)
    {
        ITier tier = TIERS.get(id);
        return tier == null ? Tier.NONE : tier;
    }

    public boolean hasSkin(String id)
    {
        return SKINS.containsKey(id);
    }

    public IUpgrade getUpgrade(String id)
    {
        IUpgrade upgrade = UPGRADES.get(id);
        return upgrade == null ? EnumUpgrade.BLANK : upgrade;
    }

    public boolean hasModel(String id)
    {
        return MODELS.containsKey(id);
    }

    public IBarrelModel getModel(String id)
    {
        IBarrelModel model = MODELS.get(id);
        return model == null ? ModelBarrel.INSTANCE : model;
    }
}