package com.latmod.yabba;

import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * Created by LatvianModder on 14.12.2016.
 */
public class YabbaEventHandler
{
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

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event)
    {
        if(event.crafting.getItem() instanceof ItemBlockBarrel)
        {
            for(int i = 0; i < event.craftMatrix.getSizeInventory(); i++)
            {
                ItemStack is = event.craftMatrix.getStackInSlot(i);

                if(is != null && is.getItem() instanceof ItemBlockBarrel)
                {
                    IBarrel barrel0 = (IBarrel) is.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                    IBarrel barrel1 = (IBarrel) event.crafting.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

                    if(barrel0 != null && barrel1 != null)
                    {
                        barrel1.setStackInSlot(0, barrel0.getStackInSlot(0));
                        barrel1.setItemCount(barrel0.getItemCount());
                    }
                }
            }
        }
    }
}