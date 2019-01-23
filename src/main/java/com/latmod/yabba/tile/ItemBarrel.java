package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.util.InvUtils;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.BarrelContentType;
import com.latmod.yabba.api.UpgradeData;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class ItemBarrel extends BarrelContent implements IItemHandler
{
	public ItemStack type = ItemStack.EMPTY;
	public int count = 0;
	private String cachedItemName, cachedcount;

	public ItemBarrel(Barrel b)
	{
		super(b);
	}

	@Override
	public BarrelContentType getType()
	{
		return BarrelContentType.ITEM;
	}

	@Override
	public boolean isEmpty()
	{
		return type.isEmpty();
	}

	@Override
	public void writeData(NBTTagCompound nbt)
	{
		if (count > 0)
		{
			nbt.setInteger("Count", count);
		}

		if (!type.isEmpty())
		{
			type.setCount(1);
			nbt.setTag("Item", type.serializeNBT());
		}
	}

	@Override
	public void readData(NBTTagCompound nbt)
	{
		count = nbt.getInteger("Count");
		type = nbt.hasKey("Item") ? new ItemStack(nbt.getCompoundTag("Item")) : ItemStack.EMPTY;

		if (type.isEmpty())
		{
			type = ItemStack.EMPTY;
		}
	}

	@Nullable
	@Override
	public NBTBase writeContentData()
	{
		if (count > Short.MAX_VALUE)
		{
			return new NBTTagInt(count);
		}
		else if (count > Byte.MAX_VALUE)
		{
			return new NBTTagShort((short) count);
		}

		return new NBTTagByte((byte) count);
	}

	@Override
	public void readContentData(@Nullable NBTBase nbt)
	{
		if (nbt instanceof NBTPrimitive)
		{
			count = ((NBTPrimitive) nbt).getInt();
		}
		else
		{
			count = 0;
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this : null;
	}

	@Override
	public int getSlots()
	{
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (slot == 0 || count <= 0)
		{
			return ItemStack.EMPTY;
		}

		type.setCount(count);
		return type;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (slot != 0)
		{
			return stack;
		}
		else if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		boolean isItemValid = isItemValid(slot, stack);

		if (barrel.isCreative())
		{
			return isItemValid ? ItemStack.EMPTY : stack;
		}
		else if (!isItemValid)
		{
			return stack;
		}

		int capacity = getMaxItems(stack);

		if (count >= capacity && !type.isEmpty())
		{
			if (!barrel.isCreative() && barrel.getTier().tier >= Tier.STAR.tier)
			{
				onCreativeChange();
				int i = barrel.findFreeUpgradeSlot();
			}

			return barrel.hasUpgrade(YabbaItems.UPGRADE_VOID) ? ItemStack.EMPTY : stack;
		}

		int size = Math.min(stack.getCount(), capacity - count);

		if (size > 0)
		{
			if (!simulate)
			{
				if (type.isEmpty())
				{
					type = stack.copy();
					type.setCount(size);
					count = size;
					barrel.block.markBarrelDirty(true);
				}
				else
				{
					count += size;
					barrel.block.markBarrelDirty(false);
				}
			}
		}

		return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - size);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount <= 0 || slot == 0 || count <= 0 || type.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		else if (barrel.isCreative())
		{
			return ItemHandlerHelper.copyStackWithSize(type, Math.min(amount, type.getMaxStackSize()));
		}

		ItemStack stack = ItemHandlerHelper.copyStackWithSize(type, Math.min(Math.min(amount, count), type.getMaxStackSize()));

		if (!simulate)
		{
			count -= stack.getCount();

			if (count <= 0 && !barrel.isLocked())
			{
				type = ItemStack.EMPTY;
				barrel.block.markBarrelDirty(true);
			}
			else
			{
				barrel.block.markBarrelDirty(false);
			}
		}

		return stack;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return Integer.MAX_VALUE;
	}

	public int getMaxItems(ItemStack stack)
	{
		if (barrel.getTier().infiniteCapacity())
		{
			return Tier.MAX_ITEMS;
		}

		return barrel.getTier().maxItemStacks * (stack.isEmpty() ? 64 : stack.getMaxStackSize());
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack)
	{
		if (slot != 0)
		{
			return false;
		}
		else if (type.isEmpty())
		{
			return true;
		}
		else if (type.getItem() != stack.getItem() || type.getMetadata() != stack.getMetadata() || type.getItemDamage() != stack.getItemDamage())
		{
			return false;
		}

		return Objects.equals(InvUtils.nullIfEmpty(type.getTagCompound()), InvUtils.nullIfEmpty(stack.getTagCompound())) && type.areCapsCompatible(stack);
	}

	public int getFreeSpace()
	{
		return getMaxItems(type) - count;
	}

	@Override
	public String getItemDisplayName()
	{
		if (cachedItemName == null)
		{
			cachedItemName = type.isEmpty() ? "" : TextFormatting.BOLD + TextFormatting.getTextWithoutFormattingCodes(type.getDisplayName());
		}

		return cachedItemName;
	}

	@Override
	public String getItemDisplayCount(boolean sneaking, boolean creative, boolean infinite)
	{
		if (creative)
		{
			return "INF";
		}
		else if (sneaking)
		{
			return infinite ? Integer.toString(count) : (count + " / " + getMaxItems(type));
		}
		else if (cachedcount == null)
		{
			int max = type.isEmpty() ? 64 : type.getMaxStackSize();

			if (max == 1 || count <= max)
			{
				cachedcount = Integer.toString(count);
			}
			else
			{
				cachedcount = (count / max) + "x" + max;
				int extra = count % max;
				if (extra != 0)
				{
					cachedcount += "+" + extra;
				}
			}
		}

		return cachedcount;
	}

	@Override
	public void clearCache()
	{
		cachedItemName = null;
		cachedcount = null;
	}

	@Override
	public void addItem(EntityPlayerMP player, EnumHand hand)
	{
		ItemStack heldItem = player.getHeldItem(hand);
		int c = heldItem.getCount();
		heldItem.setCount(insertItem(0, heldItem, false).getCount());

		if (c != heldItem.getCount())
		{
			player.inventory.markDirty();

			if (player.openContainer != null)
			{
				player.openContainer.detectAndSendChanges();
			}
		}
	}

	@Override
	public void addAllItems(EntityPlayerMP player, EnumHand hand)
	{
		if (type.isEmpty())
		{
			return;
		}

		boolean updateInv = false;

		for (int i = 0; i < player.inventory.mainInventory.size(); i++)
		{
			ItemStack stack0 = player.inventory.mainInventory.get(i);
			ItemStack is = insertItem(0, stack0, false);
			stack0 = player.inventory.mainInventory.get(i);

			if (is != stack0)
			{
				stack0.setCount(is.getCount());

				if (stack0.isEmpty())
				{
					player.inventory.mainInventory.set(i, ItemStack.EMPTY);
					updateInv = true;
				}
			}
		}

		if (updateInv)
		{
			player.inventory.markDirty();

			if (player.openContainer != null)
			{
				player.openContainer.detectAndSendChanges();
			}
		}
	}

	@Override
	public void removeItem(EntityPlayerMP player, boolean removeStack)
	{
		if (!type.isEmpty() && count == 0 && !barrel.isLocked())
		{
			type = ItemStack.EMPTY;
			count = 0;
			barrel.block.markBarrelDirty(true);
			return;
		}

		if (!type.isEmpty() && count > 0)
		{
			int size;

			if (removeStack)
			{
				size = type.getMaxStackSize();
			}
			else
			{
				size = 1;
			}

			ItemStack stack = extractItem(1, size, false);

			if (!stack.isEmpty())
			{
				ItemHandlerHelper.giveItemToPlayer(player, stack, player.inventory.currentItem);
			}
		}
	}

	@Override
	public void onCreativeChange()
	{
		count = 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip, ITooltipFlag flag)
	{
		tooltip.add(I18n.format("yabba.tier") + ": " + I18n.format(barrel.getTier().getLangKey()));

		if (barrel.isLocked())
		{
			tooltip.add(I18n.format("barrel_config.locked"));
		}

		if (!type.isEmpty())
		{
			tooltip.add(I18n.format("lang.yabba.item", type.getDisplayName()));
		}

		if (!barrel.isCreative())
		{
			if (barrel.getTier().infiniteCapacity())
			{
				tooltip.add(I18n.format("lang.yabba.item_count_inf", count));
			}
			else if (!type.isEmpty())
			{
				tooltip.add(I18n.format("lang.yabba.item_count", count, getMaxItems(type)));
			}
			else
			{
				tooltip.add(I18n.format("lang.yabba.item_count_max", barrel.getTier().maxItemStacks));
			}
		}

		boolean firstUpgrade = true;

		for (int i = 0; i < barrel.getUpgradeCount(); i++)
		{
			UpgradeData upgradeData = barrel.getUpgrade(i);
			if (upgradeData != null)
			{
				if (firstUpgrade)
				{
					tooltip.add(I18n.format("lang.yabba.upgrades"));
					firstUpgrade = false;
				}

				tooltip.add("> " + upgradeData.stack.getDisplayName());
			}
		}
	}

	@Override
	public int redstoneOutput()
	{
		UpgradeData data = barrel.getUpgradeData(YabbaItems.UPGRADE_REDSTONE_OUT);

		if (data != null)
		{
			return ((ItemUpgradeRedstone.RedstoneUpgradeData) data).redstoneOutput(count);
		}

		return -1;
	}
}