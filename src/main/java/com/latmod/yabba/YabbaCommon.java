package com.latmod.yabba;

import com.latmod.yabba.api.BarrelTier;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.api.YabbaRegistryEvent;
import com.latmod.yabba.block.RecipeBarrelUpgrade;
import com.latmod.yabba.item.BarrelItemData;
import com.latmod.yabba.item.EnumUpgrade;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by LatvianModder on 06.12.2016.
 */
public class YabbaCommon
{
    private static final CreativeTabs TAB = new CreativeTabs(Yabba.MOD_ID)
    {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return YabbaItems.UPGRADE;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public int getIconItemDamage()
        {
            return EnumUpgrade.BLANK.metadata;
        }
    };

    @CapabilityInject(IBarrel.class)
    public static Capability<IBarrel> BARREL_CAPABILITY;

    @CapabilityInject(IUpgrade.class)
    public static Capability<IUpgrade> UPGRADE_CAPABILITY;

    public static final BarrelTier TIER_DIRT = new BarrelTier("dirt", 1);
    public static final BarrelTier TIER_WOOD = new BarrelTier("wood", 64);
    public static final BarrelTier TIER_IRON = new BarrelTier("iron", 256);
    public static final BarrelTier TIER_GOLD = new BarrelTier("gold", 1024);
    public static final BarrelTier TIER_DMD = new BarrelTier("dmd", 4096);
    public static final BarrelTier TIER_INF = new BarrelTier("inf", 32000000);
    public static final BarrelTier TIER_CREATIVE = new BarrelTier("creative", 1);

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
        MinecraftForge.EVENT_BUS.post(new YabbaRegistryEvent(YabbaRegistry.INSTANCE));

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
        }, () -> new BarrelItemData(new ItemStack(YabbaRegistry.BARRELS.get("oak").block)));

        register("upgrade", YabbaItems.UPGRADE);

        for(YabbaRegistry.BarrelInstance instance : YabbaRegistry.BARRELS.values())
        {
            register("barrel_" + instance.ID, instance.block, new ItemBlockBarrel(instance.block));
        }

        register("antibarrel", YabbaItems.ANTIBARREL, new ItemBlock(YabbaItems.ANTIBARREL));

        GameRegistry.registerTileEntity(TileBarrel.class, Yabba.MOD_ID + ".barrel");
        GameRegistry.registerTileEntity(TileAntibarrel.class, Yabba.MOD_ID + ".antibarrel");

        CraftingManager.getInstance().addRecipe(new RecipeBarrelUpgrade());
        RecipeSorter.register(Yabba.MOD_ID + ":barrel_upgrade", RecipeBarrelUpgrade.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
    }

    public void postInit()
    {
        ItemStack blankUpgrade = new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.BLANK.metadata);

        GameRegistry.addRecipe(new ShapedOreRecipe(ItemHandlerHelper.copyStackWithSize(blankUpgrade, 16),
                "SSS", "ICI", "SSS",
                'I', Blocks.IRON_BARS,
                'C', "chestWood",
                'S', "slabWood"));

        for(YabbaRegistry.BarrelInstance instance : YabbaRegistry.BARRELS.values())
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(instance.block.createStackWithTier(TIER_WOOD),
                    " U ", "IGI", " C ",
                    'U', blankUpgrade,
                    'I', instance.craftItem,
                    'G', "paneGlassColorless",
                    'C', "chestWood"));
        }

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.IRON_UPGRADE.metadata),
                "III", "IUI", "III",
                'U', blankUpgrade,
                'I', "ingotIron"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.GOLD_UPGRADE.metadata),
                "III", "IUI", "III",
                'U', blankUpgrade,
                'I', "ingotGold"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.DIAMOND_UPGRADE.metadata),
                "IUI",
                'U', blankUpgrade,
                'I', "gemDiamond"));

        GameRegistry.addShapelessRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.NETHER_STAR_UPGRADE.metadata), blankUpgrade, Items.NETHER_STAR);
        GameRegistry.addShapelessRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.LOCKED.metadata), blankUpgrade, Items.NAME_TAG);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.OBSIDIAN_SHELL.metadata),
                " I ", "IUI", " I ",
                'U', blankUpgrade,
                'I', "obsidian"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.REDSTONE_OUT.metadata),
                " I ", "IUI", " I ",
                'U', blankUpgrade,
                'I', "dustRedstone"));

        GameRegistry.addShapelessRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.HOPPER.metadata), blankUpgrade, Blocks.HOPPER);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.ENDER_LINK.metadata),
                " G ", "IUI", " G ",
                'U', blankUpgrade,
                'I', "enderpearl",
                'G', "dustGlowstone"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.ANTIBARREL),
                "NQN", "NON", "NCN",
                'N', "ingotBrickNether",
                'Q', "blockQuartz",
                'O', "obsidian",
                'C', "chestWood"));
    }
}