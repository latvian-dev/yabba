package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.EmptyCapStorage;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.block.RecipeBarrelUpgrade;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumUpgrade;
import com.latmod.yabba.util.Tier;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;

/**
 * Created by LatvianModder on 06.12.2016.
 */
public class YabbaCommon
{
    @CapabilityInject(IBarrel.class)
    public static Capability<IBarrel> BARREL_CAPABILITY;

    @CapabilityInject(IUpgrade.class)
    public static Capability<IUpgrade> UPGRADE_CAPABILITY;

    public static final Tier TIER_IRON = new Tier("iron");
    public static final Tier TIER_GOLD = new Tier("gold");
    public static final Tier TIER_DMD = new Tier("dmd");

    public static final YabbaCreativeTab TAB = new YabbaCreativeTab();

    public void preInit()
    {
        YabbaRegistry.INSTANCE.sendEvent();
        CapabilityManager.INSTANCE.register(IUpgrade.class, new EmptyCapStorage<>(), () -> EnumUpgrade.BLANK);
        CapabilityManager.INSTANCE.register(IBarrel.class, new EmptyCapStorage<>(), () -> null);

        LMUtils.register(YabbaItems.UPGRADE);
        LMUtils.register(YabbaItems.PAINTER);
        LMUtils.register(YabbaItems.HAMMER);
        LMUtils.register(YabbaItems.BARREL);
        LMUtils.register(YabbaItems.ANTIBARREL);

        GameRegistry.registerTileEntity(TileBarrel.class, Yabba.MOD_ID + ".barrel");
        GameRegistry.registerTileEntity(TileAntibarrel.class, Yabba.MOD_ID + ".antibarrel");

        CraftingManager.getInstance().addRecipe(new RecipeBarrelUpgrade());
        RecipeSorter.register(Yabba.MOD_ID + ":barrel_upgrade", RecipeBarrelUpgrade.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        Tier.WOOD.setMaxStacks(YabbaConfig.BASE_MAX_STACKS);
        TIER_IRON.setMaxStacks(Tier.WOOD.getMaxStacks() * YabbaConfig.MULTIPLIER);
        TIER_GOLD.setMaxStacks(TIER_IRON.getMaxStacks() * YabbaConfig.MULTIPLIER);
        TIER_DMD.setMaxStacks(TIER_GOLD.getMaxStacks() * YabbaConfig.MULTIPLIER);
    }

    public void openModelGui()
    {
    }

    public void openSkinGui()
    {
    }
}