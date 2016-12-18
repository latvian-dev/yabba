package com.latmod.yabba;

import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.YabbaRegistryEvent;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.item.EnumUpgrade;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
            reg.addBarrel(type.getName(), new ItemStack(Blocks.PLANKS, 1, type.getMetadata()), Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, type));
        }

        reg.addBarrel("dirt", "dirt", Blocks.DIRT.getDefaultState());

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