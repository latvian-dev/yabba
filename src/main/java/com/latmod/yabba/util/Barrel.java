package com.latmod.yabba.util;

import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.ITier;
import gnu.trove.map.hash.TIntByteHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public abstract class Barrel implements IBarrelModifiable
{
    private static final String[] ALLOWED_ORE_NAMES = {"ingot", "block", "nugget", "ore", "dust", "gem"};
    private static final TIntByteHashMap ALLOWED_ORE_NAME_CACHE = new TIntByteHashMap();

    private static boolean isOreNameAllowed(int id)
    {
        byte b = ALLOWED_ORE_NAME_CACHE.get(id);

        if(b == 0)
        {
            b = 2;

            String name = OreDictionary.getOreName(id);

            for(String s : ALLOWED_ORE_NAMES)
            {
                if(name.startsWith(s))
                {
                    b = 1;
                    break;
                }
            }

            ALLOWED_ORE_NAME_CACHE.put(id, b);
        }

        return b == 1;
    }

    private static boolean canInsertItem(ItemStack stored, ItemStack stack, boolean checkOreNames)
    {
        if(checkOreNames)
        {
            int[] storedIDs = OreDictionary.getOreIDs(stored);

            if(storedIDs.length != -1)
            {
                return false;
            }

            int[] stackIDs = OreDictionary.getOreIDs(stack);

            if(stackIDs.length != 1 || storedIDs[0] != stackIDs[0] || !isOreNameAllowed(stackIDs[0]))
            {
                return false;
            }
        }

        if(stored.getItem() != stack.getItem() || stored.getMetadata() != stack.getMetadata() || stored.getItemDamage() != stack.getItemDamage())
        {
            return false;
        }

        NBTTagCompound tag1 = stored.getTagCompound();
        NBTTagCompound tag2 = stack.getTagCompound();
        return Objects.equals((tag1 == null || tag1.hasNoTags()) ? null : tag1, (tag2 == null || tag2.hasNoTags()) ? null : tag2) && stored.areCapsCompatible(stack);
    }

    @Override
    public boolean getFlag(int flag)
    {
        return (getFlags() & flag) != 0;
    }

    @Override
    public void setFlag(int flag, boolean v)
    {
        if(v)
        {
            setFlags(getFlags() | flag);
        }
        else
        {
            setFlags(getFlags() & ~flag);
        }
    }

    @Override
    public int getSlots()
    {
        return 1;
    }

    @Override
    @Nullable
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        if(stack == null || stack.stackSize <= 0)
        {
            return null;
        }

        ItemStack storedItem = getStackInSlot(0);

        boolean canInsert = storedItem == null || canInsertItem(storedItem, stack, getFlag(FLAG_CHECK_ORE_NAMES));

        if(storedItem != null && getFlag(FLAG_IS_CREATIVE))
        {
            return canInsert ? null : stack;
        }

        ITier tier = getTier();
        int itemCount = getItemCount();
        int capacity;

        if(itemCount > 0)
        {
            capacity = tier.getMaxItems(this, storedItem);

            if(itemCount >= capacity)
            {
                return (canInsert && getFlag(FLAG_VOID_ITEMS)) ? null : stack;
            }
        }
        else
        {
            capacity = tier.getMaxItems(this, stack);
        }

        if(canInsert)
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
                    markBarrelDirty(full);
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

        if(getFlag(FLAG_IS_CREATIVE))
        {
            return ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(amount, itemCount));
        }

        ItemStack stack = ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(Math.min(amount, itemCount), storedItem.getMaxStackSize()));

        if(!simulate)
        {
            setItemCount(itemCount - stack.stackSize);
            boolean full = false;

            if(itemCount - stack.stackSize <= 0 && !getFlag(FLAG_LOCKED))
            {
                setStackInSlot(0, null);
                full = true;
            }

            markBarrelDirty(full);
        }

        return stack;
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
    public void addUpgradeName(String name)
    {
        NBTTagList list = getUpgradeNames();

        if(list == null)
        {
            list = new NBTTagList();
        }

        list.appendTag(new NBTTagString(name));

        setUpgradeNames(list);
    }

    @Override
    public void copyFrom(IBarrel barrel)
    {
        setTier(barrel.getTier());
        setFlags(barrel.getFlags());
        setItemCount(barrel.getItemCount());
        setStackInSlot(0, barrel.getStackInSlot(0));
        setModel(barrel.getModel());
        setSkin(barrel.getSkin());
        NBTTagCompound upgradeNBT = barrel.getUpgradeNBT();
        setUpgradeNBT(upgradeNBT == null ? null : upgradeNBT.copy());
        NBTTagList upgradeNames = barrel.getUpgradeNames();
        setUpgradeNames(upgradeNames == null ? null : upgradeNames.copy());
    }
}
