package com.latmod.yabba;

import com.latmod.yabba.api.ITier;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.block.EnumTier;
import com.latmod.yabba.item.EnumUpgrade;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by LatvianModder on 06.12.2016.
 */
public class YabbaCommon
{
    private static void register(String id, Item item)
    {
        item.setRegistryName(Yabba.MOD_ID, id);
        item.setUnlocalizedName(Yabba.MOD_ID + '.' + id);
        item.setCreativeTab(YabbaItems.TAB);
        GameRegistry.register(item);
    }

    private static void register(String id, Block block, ItemBlock itemBlock)
    {
        block.setRegistryName(Yabba.MOD_ID, id);
        block.setUnlocalizedName(Yabba.MOD_ID + '.' + id);
        block.setCreativeTab(YabbaItems.TAB);
        GameRegistry.register(block);
        register(id, itemBlock);
    }

    public void preInit()
    {
        register("upgrade", YabbaItems.UPGRADE);

        for(BlockPlanks.EnumType type : BlockPlanks.EnumType.values())
        {
            BlockBarrel block = new BlockBarrel();
            YabbaItems.BARRELS.put(type, block);
            register("barrel_" + type.getName(), block, new ItemBlockBarrel(block));
        }

        register("antibarrel", YabbaItems.ANTIBARREL, new ItemBlock(YabbaItems.ANTIBARREL));

        GameRegistry.registerTileEntity(TileBarrel.class, Yabba.MOD_ID + ".barrel");
        GameRegistry.registerTileEntity(TileAntibarrel.class, Yabba.MOD_ID + ".antibarrel");
    }

    public void postInit()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(YabbaItems.UPGRADE, 8),
                "ISI", "SCS", "ISI",
                'I', Blocks.IRON_BARS,
                'C', "chestWood",
                'S', "slabWood"));

        ItemStack blankUpgrade = new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.BLANK.metadata);

        for(BlockPlanks.EnumType type : BlockPlanks.EnumType.values())
        {
            BlockBarrel block = YabbaItems.BARRELS.get(type);

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block, 1, 0),
                    " U ", "SGS", " C ",
                    'U', blankUpgrade,
                    'S', new ItemStack(Blocks.WOODEN_SLAB, 1, type.getMetadata()),
                    'G', "paneGlassColorless",
                    'C', "chestWood"));

            for(ITier tier : EnumTier.VALUES)
            {
                if(tier.getNextTier() != null)
                {
                    GameRegistry.addRecipe(new ShapelessOreRecipe(block.createStackWithTier(tier.getNextTier()), block.createStackWithTier(tier), tier.getNextTierUpgradeItem()));
                }
            }
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