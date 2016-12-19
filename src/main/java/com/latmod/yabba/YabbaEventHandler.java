package com.latmod.yabba;

import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.IconSet;
import com.latmod.yabba.api.YabbaRegistryEvent;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.item.EnumUpgrade;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
            reg.addVariant("planks_" + type.getName(),
                    Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, type),
                    new ItemStack(Blocks.PLANKS, 1, type.getMetadata()),
                    IconSet.all("blocks/planks_" + type.getUnlocalizedName()));
        }

        reg.addVariant("dirt", Blocks.DIRT.getDefaultState(), "dirt", IconSet.all("blocks/dirt"));
        reg.addVariant("stone_bricks", Blocks.STONEBRICK.getDefaultState(), Blocks.STONEBRICK, IconSet.all("blocks/stonebrick"));
        reg.addVariant("bricks", Blocks.BRICK_BLOCK.getDefaultState(), Blocks.BRICK_BLOCK, IconSet.all("blocks/brick"));
        reg.addVariant("quartz", Blocks.QUARTZ_BLOCK.getDefaultState(), Blocks.QUARTZ_BLOCK, IconSet.all("blocks/quartz_block_chiseled_top"));
        reg.addVariant("end_bricks", Blocks.END_BRICKS.getDefaultState(), Blocks.END_BRICKS, IconSet.all("blocks/end_bricks"));
        reg.addVariant("nether_bricks", Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK, IconSet.all("blocks/nether_brick"));
        reg.addVariant("red_nether_bricks", Blocks.field_189879_dh.getDefaultState(), Blocks.field_189879_dh, IconSet.all("blocks/red_nether_brick"));
        reg.addVariant("prismarine_bricks", Blocks.PRISMARINE.getDefaultState(), "blockPrismarineBrick", IconSet.all("blocks/prismarine_bricks"));
        reg.addVariant("dragon_egg", Blocks.DRAGON_EGG.getDefaultState(), Blocks.DRAGON_EGG, IconSet.all("blocks/dragon_egg"));
        reg.addVariant("melon", Blocks.MELON_BLOCK.getDefaultState(), Blocks.MELON_BLOCK, IconSet.topAndSide("blocks/melon_top", "blocks/melon_side"));
        reg.addVariant("pumpkin", Blocks.PUMPKIN.getDefaultState(), Blocks.PUMPKIN, IconSet.topAndSide("blocks/pumpkin_top", "blocks/pumpkin_side"));
        reg.addVariant("ice", Blocks.ICE.getDefaultState(), Blocks.ICE, IconSet.all("blocks/ice"));
        reg.addVariant("glass", Blocks.GLASS.getDefaultState(), Blocks.GLASS, IconSet.all("blocks/glass"));
        reg.addVariant("glowstone", Blocks.GLOWSTONE.getDefaultState(), Blocks.GLOWSTONE, IconSet.all("blocks/glowstone"));
        reg.addVariant("magma", Blocks.field_189877_df.getDefaultState(), Blocks.field_189877_df, IconSet.all("blocks/magma"));

        for(EnumDyeColor dye : EnumDyeColor.values())
        {
            reg.addVariant("glass_" + dye.getName(),
                    Blocks.STAINED_GLASS.getDefaultState().withProperty(BlockColored.COLOR, dye),
                    new ItemStack(Blocks.STAINED_GLASS, 1, dye.getMetadata()),
                    IconSet.all("blocks/glass_" + dye.getUnlocalizedName()));
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