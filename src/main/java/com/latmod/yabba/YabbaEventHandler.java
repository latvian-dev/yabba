package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.events.YabbaCreateConfigEvent;
import com.latmod.yabba.api.events.YabbaModelsEvent;
import com.latmod.yabba.api.events.YabbaSkinsEvent;
import com.latmod.yabba.models.ModelCrate;
import com.latmod.yabba.models.ModelPanel;
import com.latmod.yabba.models.ModelSolid;
import com.latmod.yabba.models.ModelSolidBorders;
import com.latmod.yabba.util.EnumRedstoneCompMode;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
public class YabbaEventHandler
{
    @SubscribeEvent
    public static void addModels(YabbaModelsEvent event)
    {
        YabbaModelsEvent.YabbaModelRegistry reg = event.getRegistry();

        reg.addModel(new ModelCrate("crate"));
        reg.addModel(new ModelSolid("solid"));
        reg.addModel(new ModelSolidBorders("solid_borders"));
        reg.addModel(new ModelPanel("cover", 0.125F));
        reg.addModel(new ModelPanel("panel", 0.25F));
        reg.addModel(new ModelPanel("slab", 0.5F));
    }

    @SubscribeEvent
    public static void addSkins(YabbaSkinsEvent event)
    {
        YabbaSkinsEvent.YabbaSkinRegistry reg = event.getRegistry();

        for(BlockPlanks.EnumType type : BlockPlanks.EnumType.values())
        {
            if(type != BlockPlanks.EnumType.OAK)
            {
                reg.addSkin(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, type), "all=blocks/planks_" + type.getUnlocalizedName());
            }

            String woodTex = "blocks/log_" + type.getUnlocalizedName();

            if(type.getMetadata() < 4)
            {
                reg.addSkin(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, type), "up&down=" + woodTex + "_top,all=" + woodTex);
            }
            else
            {
                reg.addSkin(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, type), "up&down=" + woodTex + "_top,all=" + woodTex);
            }
        }

        reg.addSkin(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE), "all=blocks/stone");

        for(BlockStone.EnumType type : BlockStone.EnumType.values())
        {
            if(type != BlockStone.EnumType.STONE)
            {
                reg.addSkin(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, type), "all=blocks/stone_" + type.name().toLowerCase());
            }
        }

        reg.addSkin(Blocks.COBBLESTONE, "all=blocks/cobblestone");
        reg.addSkin(Blocks.MOSSY_COBBLESTONE, "all=blocks/cobblestone_mossy");
        reg.addSkin(Blocks.DIRT, "all=blocks/dirt");
        reg.addSkin(Blocks.STONEBRICK, "all=blocks/stonebrick");
        reg.addSkin(Blocks.BRICK_BLOCK, "all=blocks/brick");
        reg.addSkin(Blocks.OBSIDIAN, "all=blocks/obsidian");
        reg.addSkin(Blocks.END_BRICKS, "all=blocks/end_bricks");
        reg.addSkin(Blocks.NETHER_BRICK, "all=blocks/nether_brick");
        reg.addSkin(Blocks.RED_NETHER_BRICK, "all=blocks/red_nether_brick");
        reg.addSkin(Blocks.PRISMARINE, "all=blocks/prismarine_bricks");
        reg.addSkin(Blocks.DRAGON_EGG, "all=blocks/dragon_egg");
        reg.addSkin(Blocks.MELON_BLOCK, "up&down=minecraft:blocks/melon_top,all=minecraft:blocks/melon_side");
        reg.addSkin(Blocks.PUMPKIN, "up&down=blocks/pumpkin_top,all=blocks/pumpkin_side");
        reg.addSkin(Blocks.ICE, "all=blocks/ice");
        reg.addSkin(Blocks.GLASS, "all=blocks/glass");
        reg.addSkin(Blocks.GLOWSTONE, "all=blocks/glowstone");
        reg.addSkin(Blocks.MAGMA, "all=blocks/magma");
        reg.addSkin(Blocks.NOTEBLOCK, "all=blocks/jukebox_side");
        reg.addSkin(Blocks.WATER, "all=blocks/water_still");
        reg.addSkin(Blocks.LAVA, "all=blocks/lava_still");
        reg.addSkin(Blocks.PORTAL, "all=blocks/portal");
        reg.addSkin(Blocks.GOLD_BLOCK, "all=blocks/gold_block");
        reg.addSkin(Blocks.IRON_BLOCK, "all=blocks/iron_block");
        reg.addSkin(Blocks.LAPIS_BLOCK, "all=blocks/lapis_block");
        reg.addSkin(Blocks.DIAMOND_BLOCK, "all=blocks/diamond_block");
        reg.addSkin(Blocks.REDSTONE_BLOCK, "all=blocks/redstone_block");
        reg.addSkin(Blocks.EMERALD_BLOCK, "all=blocks/emerald_block");
        reg.addSkin(Blocks.QUARTZ_BLOCK, "all=blocks/quartz_block_lines_top");
        reg.addSkin(Blocks.COAL_BLOCK, "all=blocks/coal_block");
        reg.addSkin(Blocks.BONE_BLOCK, "up&down=blocks/bone_block_top,all=blocks/bone_block_side");
        reg.addSkin(Blocks.HAY_BLOCK, "up&down=blocks/hay_block_top,all=blocks/hay_block_side");
        reg.addSkin(Blocks.BOOKSHELF, "up&down=blocks/planks_oak,all=blocks/bookshelf");

        for(EnumDyeColor dye : EnumDyeColor.values())
        {
            reg.addSkin(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, dye), "all=blocks/hardened_clay_stained_" + dye.getName());
            reg.addSkin(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, dye), "all=blocks/wool_colored_" + dye.getName());
        }
    }

    @SubscribeEvent
    public static void createConfig(YabbaCreateConfigEvent event)
    {
        IBarrelModifiable barrel = event.getBarrel();

        String group = Yabba.MOD_ID;
        event.add(group, "disable_ore_items", PropertyBool.create(false, () -> barrel.getFlag(IBarrel.FLAG_DISABLE_ORE_DICTIONARY), v -> barrel.setFlag(IBarrel.FLAG_DISABLE_ORE_DICTIONARY, v)));
        event.add(group, "always_display_data", PropertyBool.create(false, () -> barrel.getFlag(IBarrel.FLAG_ALWAYS_DISPLAY_DATA), v -> barrel.setFlag(IBarrel.FLAG_ALWAYS_DISPLAY_DATA, v)));
        event.add(group, "display_bar", PropertyBool.create(false, () -> barrel.getFlag(IBarrel.FLAG_DISPLAY_BAR), v -> barrel.setFlag(IBarrel.FLAG_DISPLAY_BAR, v)));

        if(barrel.getFlag(IBarrel.FLAG_REDSTONE_OUT))
        {
            group = Yabba.MOD_ID + ".redstone";
            event.add(group, "mode", PropertyEnum.create(EnumRedstoneCompMode.NAME_MAP, EnumRedstoneCompMode.EQUAL, () -> EnumRedstoneCompMode.getMode(barrel.getUpgradeNBT().getByte("RedstoneMode")), v -> barrel.setUpgradeData("RedstoneMode", new NBTTagByte((byte) v.ordinal()))));
            event.add(group, "item_count", PropertyInt.create(0, 0, Integer.MAX_VALUE, () -> barrel.getUpgradeNBT().getInteger("RedstoneItemCount"), v -> barrel.setUpgradeData("RedstoneItemCount", new NBTTagInt(v))));
        }

        if(barrel.getFlag(IBarrel.FLAG_HOPPER))
        {
            group = Yabba.MOD_ID + ".hopper";
            event.add(group, "up", PropertyBool.create(true, () -> barrel.getUpgradeNBT().getBoolean("HopperUp"), v -> barrel.setUpgradeData("HopperUp", new NBTTagByte((byte) (v ? 1 : 0)))));
            event.add(group, "down", PropertyBool.create(true, () -> barrel.getUpgradeNBT().getBoolean("HopperDown"), v -> barrel.setUpgradeData("HopperDown", new NBTTagByte((byte) (v ? 1 : 0)))));
            event.add(group, "collect", PropertyBool.create(false, () -> barrel.getUpgradeNBT().getBoolean("HopperCollect"), v -> barrel.setUpgradeData("HopperCollect", new NBTTagByte((byte) (v ? 1 : 0)))));
        }
    }
}