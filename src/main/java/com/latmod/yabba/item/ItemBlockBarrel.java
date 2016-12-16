package com.latmod.yabba.item;

import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.block.EnumTier;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;

/**
 * Created by LatvianModder on 13.12.2016.
 */
public class ItemBlockBarrel extends ItemBlock
{
    public ItemBlockBarrel(Block block)
    {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return 64;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
    {
        return new BarrelItemData(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv)
    {
        IBarrel data = (IBarrel) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        EnumTier tier = EnumTier.VALUES[stack.getMetadata() & 3];
        ItemStack stack1 = data.getStackInSlot(0);

        if(stack1 != null)
        {
            list.add(stack1.getDisplayName());
            list.add(data.getItemCount() + " / " + tier.getCapacity());
        }
        else
        {
            list.add("0 / " + tier.getCapacity());
        }
    }
}