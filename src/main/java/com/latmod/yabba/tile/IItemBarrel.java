package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.util.InvUtils;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.BarrelType;
import com.latmod.yabba.block.Tier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public interface IItemBarrel extends IBarrelBase, IItemHandler
{
	static boolean canInsertItem(ItemStack stored, ItemStack stack)
	{
		if (stored.getItem() != stack.getItem() || stored.getMetadata() != stack.getMetadata() || stored.getItemDamage() != stack.getItemDamage())
		{
			return false;
		}

		return Objects.equals(InvUtils.nullIfEmpty(stored.getTagCompound()), InvUtils.nullIfEmpty(stack.getTagCompound())) && stored.areCapsCompatible(stack);
	}

	@Override
	default BarrelType getType()
	{
		return BarrelType.ITEM;
	}

	int getItemCount();

	void setRawItemCount(int v);

	ItemStack getStoredItemType();

	void setRawItemType(ItemStack type);

	@Override
	default ItemStack getStackInSlot(int slot)
	{
		if (getItemCount() <= 0)
		{
			return ItemStack.EMPTY;
		}

		getStoredItemType().setCount(getTier().creative() ? (Tier.MAX_ITEMS / 2) : getItemCount());
		return getStoredItemType();
	}

	@Override
	default int getSlots()
	{
		return 1;
	}

	@Override
	default ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		boolean storedIsEmpty = getStoredItemType().isEmpty();
		boolean canInsert = storedIsEmpty || canInsertItem(getStoredItemType(), stack);
		if (!storedIsEmpty && getTier().creative())
		{
			return canInsert ? ItemStack.EMPTY : stack;
		}

		int capacity;

		if (getItemCount() > 0)
		{
			capacity = getMaxItems(getStoredItemType());

			if (getItemCount() >= capacity)
			{
				return (canInsert && hasUpgrade(YabbaItems.UPGRADE_VOID)) ? ItemStack.EMPTY : stack;
			}
		}
		else
		{
			capacity = getMaxItems(stack);
		}

		if (canInsert)
		{
			int size = Math.min(stack.getCount(), capacity - getItemCount());

			if (size > 0)
			{
				if (!simulate)
				{
					if (getStoredItemType().isEmpty())
					{
						setStoredItemType(stack, size);
					}
					else
					{
						setItemCount(getItemCount() + size);
					}
				}
			}

			return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - size);
		}

		return stack;
	}

	@Override
	default ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount <= 0)
		{
			return ItemStack.EMPTY;
		}

		if (getItemCount() <= 0 || getStoredItemType().isEmpty())
		{
			return ItemStack.EMPTY;
		}

		if (getTier().creative())
		{
			return ItemHandlerHelper.copyStackWithSize(getStoredItemType(), Math.min(amount, getStoredItemType().getMaxStackSize()));
		}

		ItemStack stack = ItemHandlerHelper.copyStackWithSize(getStoredItemType(), Math.min(Math.min(amount, getItemCount()), getStoredItemType().getMaxStackSize()));

		if (!simulate)
		{
			setItemCount(getItemCount() - stack.getCount());
		}

		return stack;
	}

	@Override
	default int getSlotLimit(int slot)
	{
		return getMaxItems(getStoredItemType());
	}

	default int getFreeSpace()
	{
		return getMaxItems(getStoredItemType()) - getItemCount();
	}

	default void setStoredItemType(ItemStack type, int amount)
	{
		boolean prevEmpty = getStoredItemType().isEmpty();

		if (type.isEmpty())
		{
			type = ItemStack.EMPTY;
		}

		if (amount <= 0)
		{
			amount = 0;

			if (!isLocked())
			{
				type = ItemStack.EMPTY;
			}
		}

		if (!type.isEmpty())
		{
			type = ItemHandlerHelper.copyStackWithSize(type, 1);

			if (getTier().creative())
			{
				amount = Tier.MAX_ITEMS / 2;
			}
		}

		setRawItemType(type);
		setRawItemCount(Math.min(amount, getMaxItems(getStoredItemType())));
		markBarrelDirty(prevEmpty != getStoredItemType().isEmpty());
	}

	default boolean setItemCount(int v)
	{
		if (getItemCount() != v)
		{
			boolean isEmpty = getItemCount() <= 0;
			setRawItemCount(v);

			if (getItemCount() <= 0 && !isLocked() && !getStoredItemType().isEmpty())
			{
				setStoredItemType(ItemStack.EMPTY, 0);
			}

			markBarrelDirty(isEmpty != (getItemCount() <= 0));
			return true;
		}

		return false;
	}

	default int getMaxItems(ItemStack stack)
	{
		if (getTier().infiniteCapacity())
		{
			return Tier.MAX_ITEMS;
		}

		return getTier().maxItemStacks * (stack.isEmpty() ? 64 : stack.getMaxStackSize());
	}
}