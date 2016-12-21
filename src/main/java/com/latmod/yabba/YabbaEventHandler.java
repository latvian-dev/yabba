package com.latmod.yabba;

import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.YabbaRegistryEvent;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.item.ItemPainter;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.models.ModelCrate;
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
                reg.addSkin(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, type),
                        new ItemStack(Blocks.PLANKS, 1, type.getMetadata()),
                        "all=blocks/planks_" + type.getUnlocalizedName());
            }
        }

        reg.addSkin(Blocks.DIRT.getDefaultState(), "dirt", "all=blocks/dirt");
        reg.addSkin(Blocks.STONEBRICK.getDefaultState(), Blocks.STONEBRICK, "all=blocks/stonebrick");
        reg.addSkin(Blocks.BRICK_BLOCK.getDefaultState(), Blocks.BRICK_BLOCK, "all=blocks/brick");
        reg.addSkin(Blocks.OBSIDIAN.getDefaultState(), "obsidian", "all=blocks/obsidian");
        reg.addSkin(Blocks.END_BRICKS.getDefaultState(), Blocks.END_BRICKS, "all=blocks/end_bricks");
        reg.addSkin(Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK, "all=blocks/nether_brick");
        reg.addSkin(Blocks.field_189879_dh.getDefaultState(), Blocks.field_189879_dh, "all=blocks/red_nether_brick");
        reg.addSkin(Blocks.PRISMARINE.getDefaultState(), "blockPrismarineBrick", "all=blocks/prismarine_bricks");
        reg.addSkin(Blocks.DRAGON_EGG.getDefaultState(), Blocks.DRAGON_EGG, "all=blocks/dragon_egg");
        reg.addSkin(Blocks.MELON_BLOCK.getDefaultState(), Blocks.MELON_BLOCK, "up&down=minecraft:blocks/melon_top,all=minecraft:blocks/melon_side");
        reg.addSkin(Blocks.PUMPKIN.getDefaultState(), Blocks.PUMPKIN, "up&down=blocks/pumpkin_top,all=blocks/pumpkin_side");
        reg.addSkin(Blocks.ICE.getDefaultState(), Blocks.ICE, "all=blocks/ice");
        reg.addSkin(Blocks.GLASS.getDefaultState(), Blocks.GLASS, "all=blocks/glass");
        reg.addSkin(Blocks.GLOWSTONE.getDefaultState(), Blocks.GLOWSTONE, "all=blocks/glowstone");
        reg.addSkin(Blocks.field_189877_df.getDefaultState(), Blocks.field_189877_df, "all=blocks/magma");
        reg.addSkin(Blocks.NOTEBLOCK.getDefaultState(), Blocks.NOTEBLOCK, "all=blocks/jukebox_side");
        reg.addSkin(Blocks.WATER.getDefaultState(), Items.WATER_BUCKET, "all=blocks/water_still");
        reg.addSkin(Blocks.LAVA.getDefaultState(), Items.LAVA_BUCKET, "all=blocks/lava_still");
        reg.addSkin(Blocks.STONE.getDefaultState(), null, "all=blocks/portal");
        reg.addSkin(Blocks.GOLD_BLOCK.getDefaultState(), "blockGold", "all=blocks/gold_block");
        reg.addSkin(Blocks.IRON_BLOCK.getDefaultState(), "blockIron", "all=blocks/iron_block");
        reg.addSkin(Blocks.LAPIS_BLOCK.getDefaultState(), "blockLapis", "all=blocks/lapis_block");
        reg.addSkin(Blocks.DIAMOND_BLOCK.getDefaultState(), "blockDiamond", "all=blocks/diamond_block");
        reg.addSkin(Blocks.REDSTONE_BLOCK.getDefaultState(), "blockRedstone", "all=blocks/redstone_block");
        reg.addSkin(Blocks.EMERALD_BLOCK.getDefaultState(), "blockEmerald", "all=blocks/emerald_block");
        reg.addSkin(Blocks.QUARTZ_BLOCK.getDefaultState(), "blockQuartz", "all=blocks/quartz_block_lines_top");
        reg.addSkin(Blocks.COAL_BLOCK.getDefaultState(), "blockCoal", "all=blocks/coal_block");
        reg.addSkin(Blocks.field_189880_di.getDefaultState(), Blocks.field_189880_di, "up&down=blocks/bone_block_top,all=blocks/bone_block_side");
        reg.addSkin(Blocks.HAY_BLOCK.getDefaultState(), Blocks.HAY_BLOCK, "up&down=blocks/hay_block_top,all=blocks/hay_block_side");

        for(EnumDyeColor dye : EnumDyeColor.values())
        {
            reg.addSkin(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, dye),
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

        reg.addUpgrade(ItemPainter.CapUpgrade.INSTANCE);

        reg.addModel(ModelBarrel.INSTANCE);
        reg.addModel(ModelCrate.INSTANCE);
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