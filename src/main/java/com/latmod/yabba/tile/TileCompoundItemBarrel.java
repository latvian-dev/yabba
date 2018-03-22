package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.item.SetStackInSlotException;
import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.feed_the_beast.ftblib.lib.util.InvUtils;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.YabbaLang;
import com.latmod.yabba.api.BarrelType;
import com.latmod.yabba.api.YabbaConfigEvent;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.util.UpgradeInst;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class TileCompoundItemBarrel extends TileBarrelBase implements ITickable, IItemBarrel, IItemHandlerModifiable
{
	private static boolean canInsertItem(ItemStack stored, ItemStack stack)
	{
		if (stored.getItem() != stack.getItem() || stored.getMetadata() != stack.getMetadata() || stored.getItemDamage() != stack.getItemDamage())
		{
			return false;
		}

		return Objects.equals(InvUtils.nullIfEmpty(stored.getTagCompound()), InvUtils.nullIfEmpty(stack.getTagCompound())) && stored.areCapsCompatible(stack);
	}

	private ItemStack storedItem = ItemStack.EMPTY;
	private int itemCount = 0;

	@Override
	public BarrelType getType()
	{
		return BarrelType.ITEM;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? (T) this : super.getCapability(capability, facing);
	}

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (type != EnumSaveType.SAVE)
		{
			return;
		}

		if (itemCount > 0)
		{
			nbt.setInteger("Count", itemCount);
		}

		super.writeData(nbt, type);

		if (!storedItem.isEmpty())
		{
			storedItem.setCount(1);
			nbt.setTag("Item", storedItem.serializeNBT());
		}
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (type != EnumSaveType.SAVE)
		{
			return;
		}

		setRawItemCount(nbt.getInteger("Count"));
		super.readData(nbt, type);

		storedItem = nbt.hasKey("Item") ? new ItemStack(nbt.getCompoundTag("Item")) : ItemStack.EMPTY;

		if (storedItem.isEmpty())
		{
			storedItem = ItemStack.EMPTY;
		}
	}

	@Override
	public void validate()
	{
		super.validate();

		if (world != null && !world.isRemote)
		{
			TileItemBarrelConnector.markAllDirty(pos, world.provider.getDimension());
		}
	}

	@Override
	public void invalidate()
	{
		super.invalidate();

		if (world != null && !world.isRemote)
		{
			TileItemBarrelConnector.markAllDirty(pos, world.provider.getDimension());
		}
	}

	@Override
	public void update()
	{
		checkIfDirty();
	}

	private void setRawItemCount(int v)
	{
		itemCount = v;
	}

	public boolean setItemCount(int v)
	{
		if (itemCount != v)
		{
			boolean isEmpty = itemCount <= 0;
			setRawItemCount(v);

			if (itemCount <= 0 && !isLocked() && !storedItem.isEmpty())
			{
				setStoredItemType(ItemStack.EMPTY, 0);
			}

			markBarrelDirty(isEmpty != (itemCount <= 0));
			return true;
		}

		return false;
	}

	public int getMaxItems(ItemStack stack)
	{
		if (tier.infiniteCapacity())
		{
			return Tier.MAX_ITEMS;
		}

		return tier.maxItemStacks * (stack.isEmpty() ? 64 : stack.getMaxStackSize());
	}

	@Override
	public int getItemCount()
	{
		return itemCount;
	}

	@Override
	public ItemStack getStoredItemType()
	{
		return storedItem;
	}

	@Override
	public void setStoredItemType(ItemStack type, int amount)
	{
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

			if (tier.creative())
			{
				amount = Tier.MAX_ITEMS / 2;
			}
		}

		storedItem = type;
		setRawItemCount(Math.min(amount, getMaxItems(storedItem)));
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (itemCount <= 0)
		{
			return ItemStack.EMPTY;
		}

		storedItem.setCount(tier.creative() ? (Tier.MAX_ITEMS / 2) : itemCount);
		return storedItem;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		if (YabbaConfig.general.crash_on_set_methods)
		{
			throw new SetStackInSlotException(Yabba.MOD_NAME);
		}
	}

	@Override
	public int getSlots()
	{
		return 1;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		boolean storedIsEmpty = storedItem.isEmpty();
		boolean canInsert = storedIsEmpty || canInsertItem(storedItem, stack);
		if (!storedIsEmpty && tier.creative())
		{
			return canInsert ? ItemStack.EMPTY : stack;
		}

		int capacity;

		if (itemCount > 0)
		{
			capacity = getMaxItems(storedItem);

			if (itemCount >= capacity)
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
			int size = Math.min(stack.getCount(), capacity - itemCount);

			if (size > 0)
			{
				if (!simulate)
				{
					if (storedItem.isEmpty())
					{
						setStoredItemType(stack, size);
					}
					else
					{
						setItemCount(itemCount + size);
					}
				}
			}

			return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - size);
		}

		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount <= 0)
		{
			return ItemStack.EMPTY;
		}

		if (itemCount <= 0 || storedItem.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		if (tier.creative())
		{
			return ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(amount, storedItem.getMaxStackSize()));
		}

		ItemStack stack = ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(Math.min(amount, itemCount), storedItem.getMaxStackSize()));

		if (!simulate)
		{
			setItemCount(itemCount - stack.getCount());
		}

		return stack;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return getMaxItems(storedItem);
	}

	@Override
	public void createConfig(YabbaConfigEvent event)
	{
		super.createConfig(event);
		String group = Yabba.MOD_ID;
		event.getConfig().setGroupName(group, new TextComponentString(Yabba.MOD_NAME));

		if (!tier.creative())
		{
			event.getConfig().add(group, "locked", isLocked);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(YabbaLang.TIER.translate(tier.langKey.translate()));

		if (isLocked())
		{
			tooltip.add(YabbaLang.LOCKED.translate());
		}

		if (!storedItem.isEmpty())
		{
			tooltip.add(YabbaLang.ITEM.translate(storedItem.getDisplayName()));
		}

		if (!tier.creative())
		{
			if (tier.infiniteCapacity())
			{
				tooltip.add(YabbaLang.ITEM_COUNT_INF.translate(itemCount));
			}
			else if (!storedItem.isEmpty())
			{
				tooltip.add(YabbaLang.ITEM_COUNT.translate(itemCount, getMaxItems(storedItem)));
			}
			else
			{
				tooltip.add(YabbaLang.ITEM_COUNT_MAX.translate(tier.maxItemStacks));
			}
		}

		if (!upgrades.isEmpty())
		{
			tooltip.add(YabbaLang.UPGRADES.translate());

			for (UpgradeInst upgrade : upgrades.values())
			{
				tooltip.add("> " + upgrade.getStack().getDisplayName());
			}
		}
	}

	@Override
	public boolean shouldDrop()
	{
		return itemCount > 0 || super.shouldDrop();
	}
}