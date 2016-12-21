package com.latmod.yabba;

import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.YabbaRegistryEvent;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by LatvianModder on 14.12.2016.
 */
public class YabbaEventHandler
{
    @SubscribeEvent
    public void onRegistryEvent(YabbaRegistryEvent event)
    {
        IYabbaRegistry reg = event.getRegistry();

        for(BlockPlanks.EnumType type : BlockPlanks.EnumType.values())
        {
            if(type != BlockPlanks.EnumType.OAK)
            {
                reg.addVariant("planks_" + type.getName(),
                        Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, type),
                        new ItemStack(Blocks.PLANKS, 1, type.getMetadata()),
                        "all=blocks/planks_" + type.getUnlocalizedName());
            }
        }

        reg.addVariant("dirt", Blocks.DIRT.getDefaultState(), "dirt", "all=blocks/dirt");
        reg.addVariant("stone_bricks", Blocks.STONEBRICK.getDefaultState(), Blocks.STONEBRICK, "all=blocks/stonebrick");
        reg.addVariant("bricks", Blocks.BRICK_BLOCK.getDefaultState(), Blocks.BRICK_BLOCK, "all=blocks/brick");
        reg.addVariant("obsidian", Blocks.OBSIDIAN.getDefaultState(), "obsidian", "all=blocks/obsidian");
        reg.addVariant("end_bricks", Blocks.END_BRICKS.getDefaultState(), Blocks.END_BRICKS, "all=blocks/end_bricks");
        reg.addVariant("nether_bricks", Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK, "all=blocks/nether_brick");
        reg.addVariant("red_nether_bricks", Blocks.field_189879_dh.getDefaultState(), Blocks.field_189879_dh, "all=blocks/red_nether_brick");
        reg.addVariant("prismarine_bricks", Blocks.PRISMARINE.getDefaultState(), "blockPrismarineBrick", "all=blocks/prismarine_bricks");
        reg.addVariant("dragon_egg", Blocks.DRAGON_EGG.getDefaultState(), Blocks.DRAGON_EGG, "all=blocks/dragon_egg");
        reg.addVariant("melon", Blocks.MELON_BLOCK.getDefaultState(), Blocks.MELON_BLOCK, "up&down=minecraft:blocks/melon_top,all=minecraft:blocks/melon_side");
        reg.addVariant("pumpkin", Blocks.PUMPKIN.getDefaultState(), Blocks.PUMPKIN, "up&down=blocks/pumpkin_top,all=blocks/pumpkin_side");
        reg.addVariant("ice", Blocks.ICE.getDefaultState(), Blocks.ICE, "all=blocks/ice");
        reg.addVariant("glass", Blocks.GLASS.getDefaultState(), Blocks.GLASS, "all=blocks/glass");
        reg.addVariant("glowstone", Blocks.GLOWSTONE.getDefaultState(), Blocks.GLOWSTONE, "all=blocks/glowstone");
        reg.addVariant("magma", Blocks.field_189877_df.getDefaultState(), Blocks.field_189877_df, "all=blocks/magma");
        reg.addVariant("noteblock", Blocks.NOTEBLOCK.getDefaultState(), Blocks.NOTEBLOCK, "all=blocks/jukebox_side");
        reg.addVariant("water", Blocks.STONE.getDefaultState(), Items.WATER_BUCKET, "all=blocks/water_still");
        reg.addVariant("lava", Blocks.STONE.getDefaultState(), Items.LAVA_BUCKET, "all=blocks/lava_still");
        reg.addVariant("portal", Blocks.STONE.getDefaultState(), null, "all=blocks/portal");
        reg.addVariant("block_gold", Blocks.GOLD_BLOCK.getDefaultState(), "blockGold", "all=blocks/gold_block");
        reg.addVariant("block_iron", Blocks.IRON_BLOCK.getDefaultState(), "blockIron", "all=blocks/iron_block");
        reg.addVariant("block_lapis", Blocks.LAPIS_BLOCK.getDefaultState(), "blockLapis", "all=blocks/lapis_block");
        reg.addVariant("block_diamond", Blocks.DIAMOND_BLOCK.getDefaultState(), "blockDiamond", "all=blocks/diamond_block");
        reg.addVariant("block_redstone", Blocks.REDSTONE_BLOCK.getDefaultState(), "blockRedstone", "all=blocks/redstone_block");
        reg.addVariant("block_emerald", Blocks.EMERALD_BLOCK.getDefaultState(), "blockEmerald", "all=blocks/emerald_block");
        reg.addVariant("block_quartz", Blocks.QUARTZ_BLOCK.getDefaultState(), "blockQuartz", "all=blocks/quartz_block_lines_top");
        reg.addVariant("block_coal", Blocks.COAL_BLOCK.getDefaultState(), "blockCoal", "all=blocks/coal_block");
        reg.addVariant("block_bone", Blocks.field_189880_di.getDefaultState(), Blocks.field_189880_di, "up&down=blocks/bone_block_top,all=blocks/bone_block_side");
        reg.addVariant("block_hay", Blocks.HAY_BLOCK.getDefaultState(), Blocks.HAY_BLOCK, "up&down=blocks/hay_block_top,all=blocks/hay_block_side");

        for(EnumDyeColor dye : EnumDyeColor.values())
        {
            reg.addVariant("clay_" + dye.getName(),
                    Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, dye),
                    new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, dye.getMetadata()),
                    "all=blocks/hardened_clay_stained_" + dye.getName());
        }

        reg.addTier(YabbaCommon.TIER_DIRT);
        reg.addTier(YabbaCommon.TIER_WOOD);
        reg.addTier(YabbaCommon.TIER_IRON);
        reg.addTier(YabbaCommon.TIER_GOLD);
        reg.addTier(YabbaCommon.TIER_DMD);
        reg.addTier(YabbaCommon.TIER_INF);
        reg.addTier(YabbaCommon.TIER_CREATIVE);

        for(EnumUpgrade upgrade : EnumUpgrade.VALUES)
        {
            reg.addUpgrade(upgrade);
        }
    }

    @SubscribeEvent
    public void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event)
    {
        if(!event.getWorld().isRemote)
        {
            IBlockState state = event.getWorld().getBlockState(event.getPos());

            if(state.getBlock() instanceof BlockBarrel && state.getValue(BlockHorizontal.FACING) == event.getFace())
            {
                TileEntity tile = event.getWorld().getTileEntity(event.getPos());

                if(tile instanceof TileBarrel)
                {
                    if(((TileBarrel) tile).onLeftClick(event.getEntityPlayer(), event.getItemStack()))
                    {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}