package com.latmod.yabba.api;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.EnumTristate;
import com.feed_the_beast.ftbl.lib.config.PropertyList;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.config.PropertyTristate;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.latmod.yabba.YabbaCommon;
import gnu.trove.map.hash.TIntByteHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class Barrel implements ICapabilityProvider, INBTSerializable<NBTTagCompound>, IDeepStorageUnit, IItemHandlerModifiable
{
	public static final int FLAG_LOCKED = 1;
	public static final int FLAG_VOID_ITEMS = 1 << 1;
	public static final int FLAG_DISABLE_ORE_DICTIONARY = 1 << 2;
	public static final int FLAG_IS_CREATIVE = 1 << 3;
	public static final int FLAG_INFINITE_CAPACITY = 1 << 4;
	public static final int FLAG_OBSIDIAN_SHELL = 1 << 5;
	public static final int FLAG_REDSTONE_OUT = 1 << 6;
	public static final int FLAG_HOPPER = 1 << 7;
	public static final int FLAG_HOPPER_ENDER = 1 << 8;
	public static final int FLAG_ALWAYS_DISPLAY_DATA = 1 << 9;
	public static final int FLAG_DISPLAY_BAR = 1 << 10;

	public static final PropertyList ALLOWED_ORE_PREFIXES = new PropertyList(PropertyString.ID);
	public static final PropertyTristate ALWAYS_DISPLAY_DATA = new PropertyTristate(EnumTristate.DEFAULT);
	public static final PropertyTristate DISPLAY_BAR = new PropertyTristate(EnumTristate.DEFAULT);

	static
	{
		ALLOWED_ORE_PREFIXES.add(new PropertyString("ingot"));
		ALLOWED_ORE_PREFIXES.add(new PropertyString("block"));
		ALLOWED_ORE_PREFIXES.add(new PropertyString("nugget"));
		ALLOWED_ORE_PREFIXES.add(new PropertyString("ore"));
		ALLOWED_ORE_PREFIXES.add(new PropertyString("dust"));
		ALLOWED_ORE_PREFIXES.add(new PropertyString("gem"));
	}

	private static final TIntByteHashMap ALLOWED_ORE_NAME_CACHE = new TIntByteHashMap();

	public static void clearCache()
	{
		ALLOWED_ORE_NAME_CACHE.clear();
	}

	private static boolean isOreNameAllowed(int id)
	{
		byte b = ALLOWED_ORE_NAME_CACHE.get(id);

		if (b == 0)
		{
			b = 2;

			String name = OreDictionary.getOreName(id);

			for (IConfigValue v : ALLOWED_ORE_PREFIXES.getList())
			{
				if (name.startsWith(v.getString()))
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
		if (stored.getItem() != stack.getItem() || stored.getMetadata() != stack.getMetadata() || stored.getItemDamage() != stack.getItemDamage())
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

		if (storedIDs.length != 1)
		{
			return false;
		}

		int[] stackIDs = OreDictionary.getOreIDs(stack);
		return !(stackIDs.length != 1 || storedIDs[0] != stackIDs[0] || !isOreNameAllowed(stackIDs[0]));
	}

	private Tier tier = Tier.WOOD;
	private int flags = 0;
	private ItemStack storedItem = ItemStack.EMPTY;
	private int itemCount = 0;
	private NBTTagCompound upgrades = new NBTTagCompound();
	private NBTTagList upgradeNames = new NBTTagList();
	private String model = "";
	private String skin = "";

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Tier", getTier().getName());
		nbt.setInteger("Flags", flags);

		if (!storedItem.isEmpty())
		{
			storedItem.setCount(1);
			nbt.setTag("Item", storedItem.serializeNBT());
			nbt.setInteger("Count", getItemCount());
		}

		if (!upgrades.hasNoTags())
		{
			nbt.setTag("Upgrades", upgrades);
		}

		nbt.setString("Model", getModel());
		nbt.setString("Skin", getSkin());

		if (!upgradeNames.hasNoTags())
		{
			nbt.setTag("UpgradeNames", upgradeNames);
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		tier = Tier.getFromName(nbt.getString("Tier"));
		flags = nbt.getInteger("Flags");
		storedItem = nbt.hasKey("Item") ? new ItemStack(nbt.getCompoundTag("Item")) : ItemStack.EMPTY;
		itemCount = storedItem.isEmpty() ? 0 : nbt.getInteger("Count");
		upgrades = nbt.getCompoundTag("Upgrades");
		model = nbt.getString("Model");
		skin = nbt.getString("Skin");
		upgradeNames = nbt.getTagList("UpgradeNames", Constants.NBT.TAG_STRING);

		if (getFlag(FLAG_IS_CREATIVE))
		{
			itemCount = tier.getMaxItems(this, getStoredItemType()) / 2;
		}

		if (itemCount > 0)
		{
			storedItem.setCount(itemCount);
		}
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

	public Tier getTier()
	{
		return tier;
	}

	public int getFlags()
	{
		return flags;
	}

	public int getItemCount()
	{
		return itemCount;
	}

	public NBTTagCompound getUpgradeNBT()
	{
		if (upgrades == null)
		{
			upgrades = new NBTTagCompound();
		}

		return upgrades;
	}

	public String getSkin()
	{
		return skin;
	}

	public String getModel()
	{
		return model;
	}

	public NBTTagList getUpgradeNames()
	{
		return upgradeNames;
	}

	public void setTier(Tier t)
	{
		tier = t;
	}

	public void setFlags(int f)
	{
		flags = f;
	}

	public void setItemCount(int v)
	{
		itemCount = v;
	}

	public void setSkin(String v)
	{
		skin = v;
	}

	public void setModel(String m)
	{
		model = m;
	}

	@Override
	public ItemStack getStoredItemType()
	{
		if (itemCount > 0)
		{
			storedItem.setCount(itemCount);
		}

		return storedItem;
	}

	@Override
	public void setStoredItemCount(int amount)
	{
		setStoredItemType(storedItem, amount);
	}

	@Override
	public void setStoredItemType(ItemStack type, int amount)
	{
		if (type == null || type.isEmpty())
		{
			storedItem = ItemStack.EMPTY;
			itemCount = 0;
			return;
		}

		if (amount != getItemCount() && !getFlag(FLAG_IS_CREATIVE))
		{
			boolean wasEmpty = getItemCount() == 0;
			setItemCount(amount);
			storedItem = (amount == 0 && !getFlag(FLAG_LOCKED)) ? ItemStack.EMPTY : type;
			markBarrelDirty(wasEmpty != (amount == 0));
		}
	}

	@Override
	public int getMaxStoredCount()
	{
		int i = getTier().getMaxItems(this, getStoredItemType());
		return i + (getFlag(FLAG_VOID_ITEMS) ? 256 : 0);
	}

	public boolean getFlag(int flag)
	{
		if (flag == FLAG_ALWAYS_DISPLAY_DATA)
		{
			return ALWAYS_DISPLAY_DATA.get().get(Bits.getFlag(getFlags(), FLAG_ALWAYS_DISPLAY_DATA));
		}
		else if (flag == FLAG_DISPLAY_BAR)
		{
			return DISPLAY_BAR.get().get(Bits.getFlag(getFlags(), FLAG_DISPLAY_BAR));
		}

		return Bits.getFlag(getFlags(), flag);

	}

	public void setFlag(int flag, boolean v)
	{
		setFlags(Bits.setFlag(getFlags(), flag, v));
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return getStoredItemType();
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		setStoredItemType(stack, stack.getCount());
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
		boolean canInsert = storedIsEmpty || canInsertItem(storedItem, stack, !getFlag(FLAG_DISABLE_ORE_DICTIONARY));
		if (!storedIsEmpty && getFlag(FLAG_IS_CREATIVE))
		{
			return canInsert ? ItemStack.EMPTY : stack;
		}

		Tier tier = getTier();
		int itemCount = getItemCount();
		int capacity;

		if (itemCount > 0)
		{
			capacity = tier.getMaxItems(this, storedItem);

			if (itemCount >= capacity)
			{
				return (canInsert && getFlag(FLAG_VOID_ITEMS)) ? ItemStack.EMPTY : stack;
			}
		}
		else
		{
			capacity = tier.getMaxItems(this, stack);
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
						//TODO: Check me
						setStoredItemType(ItemHandlerHelper.copyStackWithSize(stack, 1), 0);
						markBarrelDirty(true);
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
		if (amount <= 0)
		{
			return ItemStack.EMPTY;
		}

		int itemCount = getItemCount();

		if (itemCount <= 0)
		{
			return ItemStack.EMPTY;
		}

		ItemStack storedItem = getStoredItemType();

		if (storedItem.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		if (getFlag(FLAG_IS_CREATIVE))
		{
			return ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(amount, itemCount));
		}

		ItemStack stack = ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(Math.min(amount, itemCount), storedItem.getMaxStackSize()));

		if (!simulate)
		{
			setItemCount(itemCount - stack.getCount());

			if (itemCount - stack.getCount() <= 0 && !getFlag(FLAG_LOCKED))
			{
				setStoredItemType(ItemStack.EMPTY, 0);
			}
		}

		return stack;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return tier.getMaxItems(this, getStoredItemType());
	}

	public void addUpgradeName(String name)
	{
		upgradeNames.appendTag(new NBTTagString(name));
	}

	public int getFreeSpace()
	{
		return tier.getMaxItems(this, getStoredItemType()) - getItemCount();
	}

	public void copyFrom(Barrel barrel)
	{
		tier = barrel.tier;
		flags = barrel.flags;
		itemCount = barrel.itemCount;
		storedItem = barrel.storedItem.copy();
		model = barrel.model;
		skin = barrel.skin;
		upgrades = barrel.upgrades.copy();
		upgradeNames = barrel.upgradeNames.copy();
	}

	public void markBarrelDirty(boolean majorChange)
	{
	}

	public void clearCachedData()
	{
	}
}
