package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.Tier;
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
 * @author LatvianModder
 */
public abstract class Barrel implements IBarrelModifiable
{
    private static final TIntByteHashMap ALLOWED_ORE_NAME_CACHE = new TIntByteHashMap();

    public static void clearCache()
    {
        ALLOWED_ORE_NAME_CACHE.clear();
    }

    private static boolean isOreNameAllowed(int id)
    {
        byte b = ALLOWED_ORE_NAME_CACHE.get(id);

        if(b == 0)
        {
            b = 2;

            String name = OreDictionary.getOreName(id);

            for(IConfigValue v : YabbaConfig.ALLOWED_ORE_PREFIXES.getList())
            {
                if(name.startsWith(v.getString()))
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
        if(stored.getItem() != stack.getItem() || stored.getMetadata() != stack.getMetadata() || stored.getItemDamage() != stack.getItemDamage())
        {
            return checkOreNames && canInsertOreItem(stored, stack);
        }

        NBTTagCompound tag1 = stored.getTagCompound();
        NBTTagCompound tag2 = stack.getTagCompound();
        return Objects.equals((tag1 == null || tag1.hasNoTags()) ? null : tag1, (tag2 == null || tag2.hasNoTags()) ? null : tag2) && stored.areCapsCompatible(stack) || checkOreNames && canInsertOreItem(stored, stack);

    }

    private static boolean canInsertOreItem(ItemStack stored, ItemStack stack)
    {
        int[] storedIDs = OreDictionary.getOreIDs(stored);

        if(storedIDs.length != 1)
        {
            return false;
        }

        int[] stackIDs = OreDictionary.getOreIDs(stack);
        return !(stackIDs.length != 1 || storedIDs[0] != stackIDs[0] || !isOreNameAllowed(stackIDs[0]));
    }

    @Override
    public boolean getFlag(int flag)
    {
        if(flag == FLAG_ALWAYS_DISPLAY_DATA)
        {
            return YabbaConfig.ALWAYS_DISPLAY_DATA.get().get(Bits.getFlag(getFlags(), FLAG_ALWAYS_DISPLAY_DATA));
        }
        else if(flag == FLAG_DISPLAY_BAR)
        {
            return YabbaConfig.DISPLAY_BAR.get().get(Bits.getFlag(getFlags(), FLAG_DISPLAY_BAR));
        }

        return Bits.getFlag(getFlags(), flag);

    }

    @Override
    public void setFlag(int flag, boolean v)
    {
        setFlags(Bits.setFlag(getFlags(), flag, v));
    }

    @Override
    public int getSlots()
    {
        return 1;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        if(stack.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        ItemStack storedItem = getStackInSlot(0);
        boolean storedIsEmpty = storedItem.isEmpty();
        boolean canInsert = storedIsEmpty || canInsertItem(storedItem, stack, !getFlag(FLAG_DISABLE_ORE_DICTIONARY));
        if(!storedIsEmpty && getFlag(FLAG_IS_CREATIVE))
        {
            return canInsert ? ItemStack.EMPTY : stack;
        }

        Tier tier = getTier();
        int itemCount = getItemCount();
        int capacity;

        if(itemCount > 0)
        {
            capacity = tier.getMaxItems(this, storedItem);

            if(itemCount >= capacity)
            {
                return (canInsert && getFlag(FLAG_VOID_ITEMS)) ? ItemStack.EMPTY : stack;
            }
        }
        else
        {
            capacity = tier.getMaxItems(this, stack);
        }

        if(canInsert)
        {
            int size = Math.min(stack.getCount(), capacity - itemCount);

            if(size > 0)
            {
                if(!simulate)
                {
                    if(storedItem.isEmpty())
                    {
                        setStackInSlot(0, ItemHandlerHelper.copyStackWithSize(stack, 1));
                        setItemCount(0);
                        markBarrelDirty();
                    }

                    setItemCount(itemCount + size);
                }
            }

            return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - size);
        }

        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if(amount <= 0)
        {
            return ItemStack.EMPTY;
        }

        int itemCount = getItemCount();

        if(itemCount <= 0)
        {
            return ItemStack.EMPTY;
        }

        ItemStack storedItem = getStackInSlot(0);

        if(storedItem.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        if(getFlag(FLAG_IS_CREATIVE))
        {
            return ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(amount, itemCount));
        }

        ItemStack stack = ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(Math.min(amount, itemCount), storedItem.getMaxStackSize()));

        if(!simulate)
        {
            setItemCount(itemCount - stack.getCount());

            if(itemCount - stack.getCount() <= 0 && !getFlag(FLAG_LOCKED))
            {
                setStackInSlot(0, ItemStack.EMPTY);
                markBarrelDirty();
            }
        }

        return stack;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return getTier().getMaxItems(this, getStackInSlot(slot));
    }

    @Override
    public void setUpgradeData(String upgrade, @Nullable NBTBase v)
    {
        NBTTagCompound nbt = getUpgradeNBT();

        if(v != null)
        {
            nbt.setTag(upgrade, v);
            setUpgradeNBT(nbt);
        }
        else
        {
            nbt.removeTag(upgrade);
            setUpgradeNBT(nbt);
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
    public int getFreeSpace()
    {
        return getTier().getMaxItems(this, getStackInSlot(0)) - getItemCount();
    }
}
