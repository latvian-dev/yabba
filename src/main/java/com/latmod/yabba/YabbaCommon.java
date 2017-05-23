package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.EmptyCapStorage;
import com.feed_the_beast.ftbl.lib.item.ODItems;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.feed_the_beast.ftbl.lib.util.RecipeUtils;
import com.latmod.yabba.api.BarrelModelCommonData;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.api.Tier;
import com.latmod.yabba.api.events.YabbaModelDataEvent;
import com.latmod.yabba.block.RecipeBarrelUpgrade;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.RecipeSorter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class YabbaCommon implements YabbaModelDataEvent.YabbaModelDataRegistry
{
    @CapabilityInject(IBarrel.class)
    public static Capability<IBarrel> BARREL_CAPABILITY;

    @CapabilityInject(IUpgrade.class)
    public static Capability<IUpgrade> UPGRADE_CAPABILITY;

    public static final YabbaCreativeTab TAB = new YabbaCreativeTab();

    private static final Map<String, BarrelModelCommonData> DATA_MAP = new HashMap<>();
    public static final String DEFAULT_MODEL_ID = "barrel";
    public static final IBlockState DEFAULT_SKIN_STATE = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
    public static final String DEFAULT_SKIN_ID = LMUtils.getName(DEFAULT_SKIN_STATE);

    public void preInit()
    {
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
                'C', ODItems.CHEST_WOOD,
                'S', ODItems.SLAB);

        if(YabbaConfig.CRAFTING_BARREL_EASY_RECIPE.getBoolean())
        {
            RecipeUtils.addRecipe(YabbaItems.BARREL.createStack(DEFAULT_MODEL_ID, DEFAULT_SKIN_ID, Tier.WOOD),
                    "U", "C",
                    'U', blankUpgrade,
                    'C', ODItems.CHEST_WOOD);
        }
        else
        {
            RecipeUtils.addRecipe(YabbaItems.BARREL.createStack(DEFAULT_MODEL_ID, DEFAULT_SKIN_ID, Tier.WOOD),
                    " U ", "SCS", " P ",
                    'U', blankUpgrade,
                    'C', ODItems.CHEST_WOOD,
                    'S', ODItems.SLAB,
                    'P', ODItems.PLANKS);
        }

        RecipeUtils.addRecipe(new ItemStack(YabbaItems.PAINTER),
                "WWU", " I ", " I ",
                'U', blankUpgrade,
                'I', ODItems.IRON,
                'W', Blocks.WOOL);

        RecipeUtils.addRecipe(new ItemStack(YabbaItems.HAMMER),
                "WUW", " I ", " I ",
                'U', blankUpgrade,
                'I', ODItems.IRON,
                'W', Blocks.WOOL);

        RecipeUtils.addRecipe(EnumUpgrade.IRON_UPGRADE.item(),
                "III", "IUI", "III",
                'U', blankUpgrade,
                'I', ODItems.IRON);

        RecipeUtils.addRecipe(EnumUpgrade.GOLD_UPGRADE.item(),
                "III", "IUI", "III",
                'U', blankUpgrade,
                'I', ODItems.GOLD);

        RecipeUtils.addRecipe(EnumUpgrade.DIAMOND_UPGRADE.item(),
                "IUI",
                'U', blankUpgrade,
                'I', ODItems.DIAMOND);

        RecipeUtils.addShapelessRecipe(EnumUpgrade.VOID.item(), blankUpgrade, "dyeBlack");
        RecipeUtils.addShapelessRecipe(EnumUpgrade.NETHER_STAR_UPGRADE.item(), blankUpgrade, ODItems.NETHERSTAR);

        RecipeUtils.addRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.OBSIDIAN_SHELL.metadata),
                " I ", "IUI", " I ",
                'U', blankUpgrade,
                'I', ODItems.OBSIDIAN);

        RecipeUtils.addShapelessRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.REDSTONE_OUT.metadata), blankUpgrade, Items.COMPARATOR);
        RecipeUtils.addShapelessRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.HOPPER.metadata), blankUpgrade, Blocks.HOPPER);

        RecipeUtils.addRecipe(new ItemStack(YabbaItems.ANTIBARREL),
                "NQN", "NON", "NCN",
                'N', ODItems.NETHER_INGOT,
                'Q', ODItems.QUARTZ_BLOCK,
                'O', ODItems.OBSIDIAN,
                'C', ODItems.CHEST_WOOD);
    }

    public void postInit()
    {
        MinecraftForge.EVENT_BUS.post(new YabbaModelDataEvent(this));
    }

    public void openModelGui()
    {
    }

    public void openSkinGui()
    {
    }

    @Override
    public void addModelData(String id, BarrelModelCommonData data)
    {
        DATA_MAP.put(id, data);
    }

    public static BarrelModelCommonData getModelData(String id)
    {
        BarrelModelCommonData data = DATA_MAP.get(id);
        return data == null ? BarrelModelCommonData.DEFAULT : data;
    }
}