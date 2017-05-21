package com.latmod.yabba.item;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.Tier;
import com.latmod.yabba.util.Barrel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BarrelItemData extends Barrel implements ICapabilityProvider
{
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

    private NBTTagCompound getBarrelNBT()
    {
        NBTTagCompound nbt = null;

        if(itemStack.hasTagCompound())
        {
            nbt = (NBTTagCompound) itemStack.getTagCompound().getTag("Barrel");
        }

        if(nbt == null)
        {
            nbt = new NBTTagCompound();
            itemStack.setTagInfo("Barrel", nbt);
        }

        return nbt;
    }

    @Override
    public String getModel()
    {
        return getBarrelNBT().getString("Model");
    }

    @Override
    public void setModel(String model)
    {
        getBarrelNBT().setString("Model", model);
    }

    @Override
    public String getSkin()
    {
        return getBarrelNBT().getString("Skin");
    }

    @Override
    public void setSkin(String skin)
    {
        getBarrelNBT().setString("Skin", skin);
    }

    @Override
    public Tier getTier()
    {
        return Tier.getFromName(getBarrelNBT().getString("Tier"));
    }

    @Override
    public void setTier(Tier tier)
    {
        getBarrelNBT().setString("Tier", tier.getName());
    }

    @Override
    public int getFlags()
    {
        return getBarrelNBT().getInteger("Flags");
    }

    @Override
    public void setFlags(int f)
    {
        getBarrelNBT().setInteger("Flags", f);
    }

    @Override
    public int getItemCount()
    {
        return getBarrelNBT().getInteger("Count");
    }

    @Override
    public void setItemCount(int c)
    {
        getBarrelNBT().setInteger("Count", c);
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        NBTTagCompound nbt = (NBTTagCompound) getBarrelNBT().getTag("Item");
        return nbt == null ? ItemStack.EMPTY : new ItemStack(nbt);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack)
    {
        if(stack.isEmpty())
        {
            getBarrelNBT().removeTag("Item");
        }
        else
        {
            getBarrelNBT().setTag("Item", stack.serializeNBT());
        }
    }

    @Override
    public NBTTagCompound getUpgradeNBT()
    {
        return getBarrelNBT().getCompoundTag("Upgrades");
    }

    @Override
    public void setUpgradeNBT(@Nullable NBTTagCompound nbt)
    {
        if(nbt == null || nbt.hasNoTags())
        {
            getBarrelNBT().removeTag("Upgrades");
        }
        else
        {
            getBarrelNBT().setTag("Upgrades", nbt);
        }
    }

    @Nullable
    @Override
    public NBTTagList getUpgradeNames()
    {
        return (NBTTagList) getBarrelNBT().getTag("UpgradeNames");
    }

    @Override
    public void setUpgradeNames(@Nullable NBTTagList nbt)
    {
        if(nbt == null || nbt.hasNoTags())
        {
            getBarrelNBT().removeTag("UpgradeNames");
        }
        else
        {
            getBarrelNBT().setTag("UpgradeNames", nbt);
        }
    }

    @Override
    public void markBarrelDirty()
    {
    }

    @Override
    public void clearCachedData()
    {
    }
}