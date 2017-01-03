package com.latmod.yabba;

import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.block.RecipeBarrelUpgrade;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumUpgrade;
import com.latmod.yabba.util.Tier;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by LatvianModder on 06.12.2016.
 */
public class YabbaCommon
{
    @CapabilityInject(IBarrel.class)
    public static Capability<IBarrel> BARREL_CAPABILITY;

    @CapabilityInject(IUpgrade.class)
    public static Capability<IUpgrade> UPGRADE_CAPABILITY;

    public static final Tier TIER_DIRT = new Tier("dirt", 1);
    public static final Tier TIER_WOOD = new Tier("wood", 64);
    public static final Tier TIER_IRON = new Tier("iron", 256);
    public static final Tier TIER_GOLD = new Tier("gold", 1024);
    public static final Tier TIER_DMD = new Tier("dmd", 4096);

    public static final YabbaCreativeTab TAB = new YabbaCreativeTab();
    public static final YabbaCreativeTabAllModels TAB_ALL_MODELS = new YabbaCreativeTabAllModels();

    private static void register(String id, Item item)
    {
        item.setRegistryName(Yabba.MOD_ID, id);
        item.setUnlocalizedName(Yabba.MOD_ID + '.' + id);
        item.setCreativeTab(TAB);
        GameRegistry.register(item);
    }

    private static void register(String id, Block block, ItemBlock itemBlock)
    {
        block.setRegistryName(Yabba.MOD_ID, id);
        block.setUnlocalizedName(Yabba.MOD_ID + '.' + id);
        block.setCreativeTab(TAB);
        GameRegistry.register(block);
        register(id, itemBlock);
    }

    public void preInit()
    {
        YabbaRegistry.INSTANCE.sendEvent();

        CapabilityManager.INSTANCE.register(IUpgrade.class, new Capability.IStorage<IUpgrade>()
        {
            @Override
            public NBTBase writeNBT(Capability<IUpgrade> capability, IUpgrade instance, EnumFacing side)
            {
                return null;
            }

            @Override
            public void readNBT(Capability<IUpgrade> capability, IUpgrade instance, EnumFacing side, NBTBase nbt)
            {
            }
        }, () -> EnumUpgrade.BLANK);

        CapabilityManager.INSTANCE.register(IBarrel.class, new Capability.IStorage<IBarrel>()
        {
            @Override
            public NBTBase writeNBT(Capability<IBarrel> capability, IBarrel instance, EnumFacing side)
            {
                return null;
            }

            @Override
            public void readNBT(Capability<IBarrel> capability, IBarrel instance, EnumFacing side, NBTBase nbt)
            {
            }
        }, () -> null);

        register("upgrade", YabbaItems.UPGRADE);
        register("painter", YabbaItems.PAINTER);
        register("barrel", YabbaItems.BARREL, new ItemBlockBarrel(YabbaItems.BARREL));
        register("antibarrel", YabbaItems.ANTIBARREL, new ItemBlock(YabbaItems.ANTIBARREL));

        GameRegistry.registerTileEntity(TileBarrel.class, Yabba.MOD_ID + ".barrel");
        GameRegistry.registerTileEntity(TileAntibarrel.class, Yabba.MOD_ID + ".antibarrel");

        CraftingManager.getInstance().addRecipe(new RecipeBarrelUpgrade());
        RecipeSorter.register(Yabba.MOD_ID + ":barrel_upgrade", RecipeBarrelUpgrade.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
    }

    public void postInit()
    {
        ItemStack blankUpgrade = EnumUpgrade.BLANK.item();

        GameRegistry.addRecipe(new ShapedOreRecipe(ItemHandlerHelper.copyStackWithSize(blankUpgrade, 16),
                "SSS", "ICI", "SSS",
                'I', Blocks.IRON_BARS,
                'C', "chestWood",
                'S', "slabWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(YabbaItems.BARREL.createStack(ModelBarrel.INSTANCE, YabbaRegistry.DEFAULT_SKIN, TIER_WOOD),
                " U ", "SCS", " P ",
                'U', blankUpgrade,
                'C', "chestWood",
                'S', "slabWood",
                'P', "plankWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.PAINTER),
                "WWU", " I ", " I ",
                'U', blankUpgrade,
                'I', "ingotIron",
                'W', Blocks.WOOL));

        GameRegistry.addRecipe(new ShapedOreRecipe(EnumUpgrade.IRON_UPGRADE.item(),
                "III", "IUI", "III",
                'U', blankUpgrade,
                'I', "ingotIron"));

        GameRegistry.addRecipe(new ShapedOreRecipe(EnumUpgrade.GOLD_UPGRADE.item(),
                "III", "IUI", "III",
                'U', blankUpgrade,
                'I', "ingotGold"));

        GameRegistry.addRecipe(new ShapedOreRecipe(EnumUpgrade.DIAMOND_UPGRADE.item(),
                "IUI",
                'U', blankUpgrade,
                'I', "gemDiamond"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(EnumUpgrade.VOID.item(), blankUpgrade, "obsidian"));
        GameRegistry.addShapelessRecipe(EnumUpgrade.NETHER_STAR_UPGRADE.item(), EnumUpgrade.VOID.item(), Items.NETHER_STAR);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.OBSIDIAN_SHELL.metadata),
                " I ", "IUI", " I ",
                'U', blankUpgrade,
                'I', "obsidian"));

        GameRegistry.addShapelessRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.REDSTONE_OUT.metadata), blankUpgrade, Items.COMPARATOR);
        GameRegistry.addShapelessRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.HOPPER.metadata), blankUpgrade, Blocks.HOPPER);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.ANTIBARREL),
                "NQN", "NON", "NCN",
                'N', "ingotBrickNether",
                'Q', "blockQuartz",
                'O', "obsidian",
                'C', "chestWood"));
    }
}