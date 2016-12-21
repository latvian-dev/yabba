package com.latmod.yabba.util;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IBarrelTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public abstract class Barrel implements IBarrelModifiable
{
    private static String[] ALLOWED_ORE_NAMES = {"ingot", "block", "nugget", "ore", "dust", "gem"};
    private static HashMap<String, Boolean> ALLOWED_ORE_NAME_CACHE = new HashMap<>();

    private static boolean isOreNameAllowed(String name)
    {
        Boolean b = ALLOWED_ORE_NAME_CACHE.get(name);

        if(b == null)
        {
            b = false;

            for(String s : ALLOWED_ORE_NAMES)
            {
                if(name.startsWith(s))
                {
                    b = true;
                    break;
                }
            }

            ALLOWED_ORE_NAME_CACHE.put(name, b);
        }

        return b;
    }

    public abstract void updateCounter(boolean fullUpdate);

    @Override
    public int getSlots()
    {
        return 1;
    }

    private static boolean areItemsEqual(ItemStack is1, ItemStack is2, boolean checkOreNames)
    {
        if(checkOreNames)
        {
            //TODO
        }

        if(is1.getItem() != is2.getItem() || is1.getMetadata() != is2.getMetadata() || is1.getItemDamage() != is2.getItemDamage())
        {
            return false;
        }

        NBTTagCompound tag1 = is1.getTagCompound();
        NBTTagCompound tag2 = is2.getTagCompound();

        if(tag1 != null && tag1.hasNoTags())
        {
            tag1 = null;
        }

        if(tag2 != null && tag2.hasNoTags())
        {
            tag2 = null;
        }

        return Objects.equals(tag1, tag2) && is1.areCapsCompatible(is2);
    }

    @Override
    @Nullable
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        if(stack == null || stack.stackSize <= 0)
        {
            return null;
        }

        IBarrelTier tier = getTier();

        if(tier.equals(YabbaCommon.TIER_CREATIVE))
        {
            return stack;
        }

        int itemCount = getItemCount();
        ItemStack storedItem = getStackInSlot(0);
        int capacity;

        if(itemCount > 0)
        {
            capacity = storedItem.getMaxStackSize() * tier.getMaxStacks();

            if(itemCount >= capacity)
            {
                return stack;
            }
        }
        else
        {
            capacity = stack.getMaxStackSize() * tier.getMaxStacks();
        }

        if(storedItem == null || areItemsEqual(storedItem, stack, getUpgradeData("OreConverter") != null))
        {
            int size = Math.min(stack.stackSize, capacity - itemCount);

            if(size > 0)
            {
                if(!simulate)
                {
                    boolean full = false;
                    if(storedItem == null)
                    {
                        setStackInSlot(0, ItemHandlerHelper.copyStackWithSize(stack, 1));
                        setItemCount(0);
                        full = true;
                    }

                    setItemCount(itemCount + size);
                    updateCounter(full);
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

        if(getTier().equals(YabbaCommon.TIER_CREATIVE))
        {
            return ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(amount, itemCount));
        }

        ItemStack stack = ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(Math.min(amount, itemCount), storedItem.getMaxStackSize()));

        if(!simulate)
        {
            setItemCount(itemCount - stack.stackSize);
            boolean full = false;

            if(itemCount - stack.stackSize <= 0 && getUpgradeData("Locked") == null)
            {
                setStackInSlot(0, null);
                full = true;
            }

            updateCounter(full);
        }

        return stack;
    }

    @Override
    @Nullable
    public NBTBase getUpgradeData(String upgrade)
    {
        NBTTagCompound nbt = getUpgradeNBT();
        return nbt == null ? null : nbt.getTag(upgrade);
    }

    @Override
    public void setUpgradeData(String upgrade, @Nullable NBTBase v)
    {
        NBTTagCompound nbt = getUpgradeNBT();

        if(v != null)
        {
            if(nbt == null)
            {
                nbt = new NBTTagCompound();
                setUpgradeNBT(nbt);
            }

            nbt.setTag(upgrade, v);
        }
        else if(nbt != null)
        {
            nbt.removeTag(upgrade);

            if(nbt.hasNoTags())
            {
                setUpgradeNBT(null);
            }
        }
    }

    @Override
    public void copyFrom(IBarrel barrel)
    {
        setStackInSlot(0, barrel.getStackInSlot(0));
        setItemCount(barrel.getItemCount());
        setTier(barrel.getTier());
        setUpgradeNBT(barrel.getUpgradeNBT());
        setVariant(barrel.getVariant());
    }
}
