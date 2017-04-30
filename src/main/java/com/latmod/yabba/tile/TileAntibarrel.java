package com.latmod.yabba.tile;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 16.12.2016.
 */
public class TileAntibarrel extends TileEntity implements IItemHandlerModifiable
{
    public static final int MAX_ITEMS = 32000;

    private final List<ItemStack> items = new ArrayList<>();

    public static boolean isValidItem(ItemStack is)
    {
        return ItemStackTools.getStackSize(is) == 1 && !is.isStackable();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing side)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, side);

    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return (T) this;
        }

        return super.getCapability(capability, side);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        NBTTagList list = new NBTTagList();

        for(ItemStack is : items)
        {
            list.appendTag(is.serializeNBT());
        }

        nbt.setTag("Inv", list);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        items.clear();

        NBTTagList list = nbt.getTagList("Inv", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < list.tagCount(); i++)
        {
            ItemStack is = ItemStackTools.loadFromNBT(list.getCompoundTagAt(i));

            if(!ItemStackTools.isEmpty(is))
            {
                items.add(is);
            }
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack)
    {
        if(slot != 0)
        {
            setItemStack(slot - 1, stack);
        }
    }

    @Override
    public int getSlots()
    {
        return items.size() + 1;
    }

    @Override
    @Nullable
    public ItemStack getStackInSlot(int slot)
    {
        return (slot == 0) ? ItemStackTools.getEmptyStack() : getItemStack(slot - 1);
    }

    @Override
    @Nullable
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        if(ItemStackTools.isEmpty(stack))
        {
            return ItemStackTools.getEmptyStack();
        }
        else if(items.size() < MAX_ITEMS && isValidItem(stack))
        {
            if(!simulate)
            {
                setItemStack(-1, stack);
            }

            return null;
        }

        return stack;
    }

    @Override
    @Nullable
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if(slot == 0 || amount < 1)
        {
            return ItemStackTools.getEmptyStack();
        }

        ItemStack is = getItemStack(slot - 1);

        if(!simulate)
        {
            setItemStack(slot - 1, ItemStackTools.getEmptyStack());
        }

        return is;
    }

    @Nullable
    public ItemStack setItemStack(int slot, @Nullable ItemStack stack)
    {
        ItemStack is;

        if(slot < 0 || slot >= items.size())
        {
            if(ItemStackTools.getStackSize(stack) == 1)
            {
                items.add(stack);
            }

            is = null;
        }
        else
        {
            if(ItemStackTools.isEmpty(stack))
            {
                is = items.remove(slot);
            }
            else
            {
                is = items.set(slot, stack);
            }
        }

        markDirty();
        return is == null ? ItemStackTools.getEmptyStack() : is;
    }

    @Nullable
    public ItemStack getItemStack(int slot)
    {
        ItemStack stack = (slot < 0 || slot >= items.size()) ? null : items.get(slot);
        return stack == null ? ItemStackTools.getEmptyStack() : stack;
    }
}