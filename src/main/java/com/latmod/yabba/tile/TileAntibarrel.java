package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.item.ItemEntry;
import com.feed_the_beast.ftblib.lib.item.ItemEntryWithCount;
import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.feed_the_beast.ftblib.lib.tile.TileBase;
import com.latmod.yabba.YabbaConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class TileAntibarrel extends TileBase implements IItemHandler
{
	public final Map<ItemEntry, ItemEntryWithCount> items = new LinkedHashMap<>();
	private ItemEntryWithCount[] itemsArray = null;

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
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (type.full && !items.isEmpty())
		{
			NBTTagList list = new NBTTagList();

			for (ItemEntryWithCount entry : items.values())
			{
				if (!entry.isEmpty())
				{
					list.appendTag(entry.serializeNBT());
				}
			}

			nbt.setTag("Inv", list);
		}
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		items.clear();
		itemsArray = null;

		NBTTagList list = nbt.getTagList("Inv", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < list.tagCount(); i++)
		{
			ItemEntryWithCount entryc = new ItemEntryWithCount(list.getCompoundTagAt(i));

			if (!entryc.isEmpty())
			{
				items.put(entryc.entry, entryc);
			}
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
		return slot <= 0 || slot > items.size() ? ItemStack.EMPTY : getItemArray()[slot - 1].getStack(false);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		else if (slot >= 0 && slot <= items.size() && items.size() < YabbaConfig.general.antibarrel_capacity && isValidItem(stack))
		{
			if (!simulate)
			{
				ItemEntry entry = ItemEntry.get(stack);

				if (slot == 0)
				{
					ItemEntryWithCount entryc = items.get(entry);

					if (entryc == null)
					{
						entryc = new ItemEntryWithCount(entry, 0);
						items.put(entry, entryc);
						itemsArray = null;
					}

					entryc.count += stack.getCount();
					return ItemStack.EMPTY;
				}
				else
				{
					ItemEntryWithCount entryc = getItemArray()[slot];

					if (entryc.entry.equalsEntry(entry))
					{
						entryc.count += stack.getCount();
						return ItemStack.EMPTY;
					}
				}
			}
		}

		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (slot <= 0 || slot > items.size() || amount < 1)
		{
			return ItemStack.EMPTY;
		}

		ItemEntryWithCount entryc = getItemArray()[slot - 1];

		if (entryc.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		int extracted = Math.min(amount, entryc.count);

		ItemStack is = entryc.entry.getStack(extracted, true);

		if (!simulate)
		{
			entryc.count -= extracted;
		}

		return is;
	}

	public ItemEntryWithCount[] getItemArray()
	{
		if (itemsArray == null)
		{
			itemsArray = items.values().toArray(new ItemEntryWithCount[0]);
		}

		return itemsArray;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return Integer.MAX_VALUE;
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

	@Override
	public boolean shouldDrop()
	{
		return !items.isEmpty();
	}
}