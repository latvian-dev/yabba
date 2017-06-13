package com.latmod.yabba.tile;

import com.feed_the_beast.ftbl.lib.tile.TileBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TileAntibarrel extends TileBase implements IItemHandlerModifiable
{
	public static final int MAX_ITEMS = 32000;

	private final List<ItemStack> items = new ArrayList<>();

	public static boolean isValidItem(ItemStack is)
	{
		return is.getCount() == 1 && !is.isStackable();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, side);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
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

		for (ItemStack is : items)
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

		for (int i = 0; i < list.tagCount(); i++)
		{
			ItemStack is = new ItemStack(list.getCompoundTagAt(i));

			if (!is.isEmpty())
			{
				items.add(is);
			}
		}
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		if (slot != 0)
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
	public ItemStack getStackInSlot(int slot)
	{
		return (slot == 0) ? ItemStack.EMPTY : getItemStack(slot - 1);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		else if (items.size() < MAX_ITEMS && isValidItem(stack))
		{
			if (!simulate)
			{
				setItemStack(-1, stack); //return ?
			}

			return ItemStack.EMPTY;
		}

		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (slot == 0 || amount < 1)
		{
			return ItemStack.EMPTY;
		}

		ItemStack is = getItemStack(slot - 1);

		if (!simulate)
		{
			setItemStack(slot - 1, ItemStack.EMPTY);
		}

		return is;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 1;
	}

	public ItemStack setItemStack(int slot, ItemStack stack)
	{
		ItemStack is;

		if (slot < 0 || slot >= items.size())
		{
			if (stack.getCount() == 1)
			{
				items.add(stack);
			}

			is = null;
		}
		else
		{
			if (stack.isEmpty())
			{
				is = items.remove(slot);
			}
			else
			{
				is = items.set(slot, stack);
			}
		}

		markDirty();
		return is == null ? ItemStack.EMPTY : is;
	}

	public ItemStack getItemStack(int slot)
	{
		ItemStack stack = (slot < 0 || slot >= items.size()) ? null : items.get(slot);
		return stack == null ? ItemStack.EMPTY : stack;
	}

	@Override
	public boolean notifyBlock()
	{
		return false;
	}

	@Override
	public void markDirty()
	{
		sendDirtyUpdate();
	}
}