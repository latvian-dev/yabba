package com.latmod.yabba.item;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.ITier;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv)
    {
        IBarrel barrel = stack.getCapability(YabbaCommon.BARREL_CAPABILITY, null);

        list.add("Model: " + barrel.getModel().getName());
        list.add("Skin: " + barrel.getSkin().getDisplayName());

        ITier tier = barrel.getTier();
        ItemStack stack1 = barrel.getStackInSlot(0);

        if(stack1 != null)
        {
            list.add("Item: " + stack1.getDisplayName());
        }

        if(!barrel.getFlag(IBarrel.FLAG_IS_CREATIVE))
        {
            if(stack1 != null)
            {
                list.add(barrel.getItemCount() + " / " + tier.getMaxItems(stack1));
            }
            else
            {
                list.add("Max " + tier.getMaxStacks() + " stacks");
            }
        }

        NBTTagList upgrades = barrel.getUpgradeNames();

        if(upgrades != null && !upgrades.hasNoTags())
        {
            list.add("Upgrades:");

            for(int i = 0; i < upgrades.tagCount(); i++)
            {
                list.add("> " + I18n.format(upgrades.getStringTagAt(i)));
            }
        }
    }
}