package com.latmod.yabba.item;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.BarrelTier;
import com.latmod.yabba.block.Barrel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class BarrelItemData extends Barrel implements ICapabilityProvider
{
    private static final String TAG_BARREL_ITEM = "BarrelItem";
    private static final String TAG_BARREL_ITEM_COUNT = "BarrelItemCount";
    private static final String TAG_BARREL_UPGRADES = "BarrelUpgrades";
    private static final String TAG_BARREL_TIER = "BarrelTier";

    public final ItemStack itemStack;

    public BarrelItemData(ItemStack is)
    {
        itemStack = is;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == YabbaCommon.BARREL_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return hasCapability(capability, facing) ? (T) this : null;
    }

    @Override
    public int getItemCount()
    {
        return itemStack.hasTagCompound() ? itemStack.getTagCompound().getInteger(TAG_BARREL_ITEM_COUNT) : 0;
    }

    @Override
    public void setItemCount(int c)
    {
        itemStack.setTagInfo(TAG_BARREL_ITEM_COUNT, new NBTTagInt(c));
    }

    @Override
    @Nullable
    public NBTTagCompound getUpgradeNBT()
    {
        return itemStack.hasTagCompound() ? (NBTTagCompound) itemStack.getTagCompound().getTag(TAG_BARREL_UPGRADES) : null;
    }

    @Override
    public void setUpgradeNBT(@Nullable NBTTagCompound nbt)
    {
        if(nbt != null)
        {
            itemStack.setTagInfo(TAG_BARREL_UPGRADES, nbt);
        }
        else if(itemStack.hasTagCompound())
        {
            itemStack.getTagCompound().removeTag(TAG_BARREL_UPGRADES);

            if(itemStack.getTagCompound().hasNoTags())
            {
                itemStack.setTagCompound(null);
            }
        }
    }

    @Override
    public BarrelTier getTier()
    {
        return itemStack.hasTagCompound() ? YabbaRegistry.INSTANCE.getTier(itemStack.getTagCompound().getString(TAG_BARREL_TIER)) : BarrelTier.NONE;
    }

    @Override
    public void setTier(BarrelTier tier)
    {
        itemStack.setTagInfo(TAG_BARREL_TIER, new NBTTagString(tier.getTierID()));
    }

    @Override
    @Nullable
    public ItemStack getStackInSlot(int slot)
    {
        return (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(TAG_BARREL_ITEM)) ? ItemStack.loadItemStackFromNBT(itemStack.getTagCompound().getCompoundTag(TAG_BARREL_ITEM)) : null;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack)
    {
        if(stack != null)
        {
            itemStack.setTagInfo(TAG_BARREL_ITEM, stack.serializeNBT());
        }
        else if(itemStack.hasTagCompound())
        {
            itemStack.getTagCompound().removeTag(TAG_BARREL_ITEM);

            if(itemStack.getTagCompound().hasNoTags())
            {
                itemStack.setTagCompound(null);
            }
        }
    }

    @Override
    public void updateCounter(boolean full)
    {
    }
}