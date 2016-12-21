package com.latmod.yabba.item;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.ITier;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

/**
 * Created by LatvianModder on 13.12.2016.
 */
public class ItemBlockBarrel extends ItemBlock
{
    public ItemBlockBarrel(Block block)
    {
        super(block);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
    {
        return new BarrelItemData(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv)
    {
        IBarrel data = stack.getCapability(YabbaCommon.BARREL_CAPABILITY, null);

        list.add("Skin: " + data.getSkin().getDisplayName());

        ITier tier = data.getTier();
        ItemStack stack1 = data.getStackInSlot(0);

        if(stack1 != null)
        {
            list.add(stack1.getDisplayName());
            list.add(data.getItemCount() + " / " + (stack1.getMaxStackSize() * tier.getMaxStacks()));
        }
        else
        {
            list.add("Max " + tier.getMaxStacks() + " stacks");
        }

        NBTTagCompound upgrades = data.getUpgradeNBT();

        if(upgrades != null && !upgrades.hasNoTags())
        {
            list.add("Upgrades:");

            for(String key : upgrades.getKeySet())
            {
                list.add("> " + key);
            }
        }
    }
}