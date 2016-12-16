package com.latmod.yabba.block;

import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public abstract class Barrel implements IBarrel
{
    protected static final NBTTagCompound EMPTY_TAG = new NBTTagCompound();

    public abstract void updateCounter();

    @Override
    public int getSlots()
    {
        return 1;
    }

    public boolean isEqualToStoredItem(@Nullable ItemStack is)
    {
        ItemStack storedItem = getStackInSlot(0);
        if(storedItem == null || is == null || is.stackSize <= 0)
        {
            return false;
        }

        if(is.getItem() != storedItem.getItem() || is.getMetadata() != storedItem.getMetadata())
        {
            return false;
        }

        NBTTagCompound tag1 = storedItem.getTagCompound();
        NBTTagCompound tag2 = is.getTagCompound();

        if(tag1 != null && tag1.hasNoTags())
        {
            tag1 = null;
        }

        if(tag2 != null && tag2.hasNoTags())
        {
            tag2 = null;
        }

        return Objects.equals(tag1, tag2) && is.areCapsCompatible(storedItem);
    }

    @Override
    @Nullable
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        if(stack == null || stack.stackSize <= 0)
        {
            return null;
        }

        int capacity = getTier().getCapacity();
        int itemCount = getItemCount();

        if(itemCount >= capacity)
        {
            return stack;
        }

        boolean storedItemIsNull = getStackInSlot(0) == null;

        if(storedItemIsNull || isEqualToStoredItem(stack))
        {
            int size = Math.min(stack.stackSize, capacity - itemCount);

            if(size > 0)
            {
                if(!simulate)
                {
                    if(storedItemIsNull)
                    {
                        setStackInSlot(0, ItemHandlerHelper.copyStackWithSize(stack, 1));
                        setItemCount(0);
                    }

                    setItemCount(itemCount + size);
                    updateCounter();
                }
            }

            return ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - size);
        }

        return stack;
    }

    @Override
    @Nullable
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        int itemCount = getItemCount();

        if(amount <= 0 || itemCount <= 0)
        {
            return null;
        }

        ItemStack storedItem = getStackInSlot(0);

        if(storedItem == null)
        {
            return null;
        }

        ItemStack is = ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(Math.min(amount, itemCount), storedItem.getMaxStackSize()));

        if(!simulate)
        {
            setItemCount(itemCount - is.stackSize);

            if(itemCount - is.stackSize <= 0)
            {
                //TODO: Check if barrel is locked
                setStackInSlot(0, null);
            }

            updateCounter();
        }

        return is;
    }

    @Override
    public void removeUpgrades()
    {
        setUpgradeNBT(null);
    }

    @Override
    @Nullable
    public NBTBase getUpgradeData(IUpgrade upgrade)
    {
        NBTTagCompound nbt = getUpgradeNBT();
        return nbt == null ? null : nbt.getTag(upgrade.getUpgradeName());
    }

    @Override
    public void setUpgradeData(IUpgrade upgrade, @Nullable NBTBase v)
    {
        NBTTagCompound nbt = getUpgradeNBT();

        if(v == null)
        {
            nbt.removeTag(upgrade.getUpgradeName());

            if(nbt.hasNoTags())
            {
                nbt = null;
            }
        }
        else
        {
            nbt.setTag(upgrade.getUpgradeName(), v);
        }

        setUpgradeNBT(nbt);
    }

    @Override
    public void copyFrom(IBarrel barrel)
    {
        setStackInSlot(0, barrel.getStackInSlot(0));
        setItemCount(barrel.getItemCount());
        setTier(barrel.getTier());
    }
}
