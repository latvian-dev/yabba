package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.EmptyCapStorage;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.feed_the_beast.ftbl.lib.util.RecipeUtils;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.block.RecipeBarrelUpgrade;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumUpgrade;
import com.latmod.yabba.util.Tier;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
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

    public static final Tier TIER_IRON = new Tier("iron", YabbaConfig.TIER_ITEM_IRON, 0xFFD8D8D8);
    public static final Tier TIER_GOLD = new Tier("gold", YabbaConfig.TIER_ITEM_GOLD, 0xFFFCD803);
    public static final Tier TIER_DMD = new Tier("dmd", YabbaConfig.TIER_ITEM_DIAMOND, 0xFF00FFFF);

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
    }

    public void init()
    {
        ItemStack blankUpgrade = EnumUpgrade.BLANK.item();

        RecipeUtils.addRecipe(ItemHandlerHelper.copyStackWithSize(blankUpgrade, YabbaConfig.CRAFTING_UPGRADE_STACK_SIZE.getInt()),
                "SSS", "ICI", "SSS",
                'I', Blocks.IRON_BARS,
                'C', "chestWood",
                'S', "slabWood");

        if(YabbaConfig.CRAFTING_BARREL_EASY_RECIPE.getBoolean())
        {
            RecipeUtils.addRecipe(YabbaItems.BARREL.createStack(ModelBarrel.INSTANCE, YabbaRegistry.DEFAULT_SKIN, Tier.WOOD),
                    "U", "C",
                    'U', blankUpgrade,
                    'C', "chestWood");
        }
        else
        {
            RecipeUtils.addRecipe(YabbaItems.BARREL.createStack(ModelBarrel.INSTANCE, YabbaRegistry.DEFAULT_SKIN, Tier.WOOD),
                    " U ", "SCS", " P ",
                    'U', blankUpgrade,
                    'C', "chestWood",
                    'S', "slabWood",
                    'P', "plankWood");
        }

        RecipeUtils.addRecipe(new ItemStack(YabbaItems.PAINTER),
                "WWU", " I ", " I ",
                'U', blankUpgrade,
                'I', "ingotIron",
                'W', Blocks.WOOL);

        RecipeUtils.addRecipe(new ItemStack(YabbaItems.HAMMER),
                "WUW", " I ", " I ",
                'U', blankUpgrade,
                'I', "ingotIron",
                'W', Blocks.WOOL);

        RecipeUtils.addRecipe(EnumUpgrade.IRON_UPGRADE.item(),
                "III", "IUI", "III",
                'U', blankUpgrade,
                'I', "ingotIron");

        RecipeUtils.addRecipe(EnumUpgrade.GOLD_UPGRADE.item(),
                "III", "IUI", "III",
                'U', blankUpgrade,
                'I', "ingotGold");

        RecipeUtils.addRecipe(EnumUpgrade.DIAMOND_UPGRADE.item(),
                "IUI",
                'U', blankUpgrade,
                'I', "gemDiamond");

        RecipeUtils.addShapelessRecipe(EnumUpgrade.VOID.item(), blankUpgrade, "dyeBlack");
        RecipeUtils.addShapelessRecipe(EnumUpgrade.NETHER_STAR_UPGRADE.item(), blankUpgrade, Items.NETHER_STAR);

        RecipeUtils.addRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.OBSIDIAN_SHELL.metadata),
                " I ", "IUI", " I ",
                'U', blankUpgrade,
                'I', "obsidian");

        RecipeUtils.addShapelessRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.REDSTONE_OUT.metadata), blankUpgrade, Items.COMPARATOR);
        RecipeUtils.addShapelessRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.HOPPER.metadata), blankUpgrade, Blocks.HOPPER);

        RecipeUtils.addRecipe(new ItemStack(YabbaItems.ANTIBARREL),
                "NQN", "NON", "NCN",
                'N', "ingotBrickNether",
                'Q', "blockQuartz",
                'O', "obsidian",
                'C', "chestWood");
    }

    public void openModelGui()
    {
    }

    public void openSkinGui()
    {
    }
}