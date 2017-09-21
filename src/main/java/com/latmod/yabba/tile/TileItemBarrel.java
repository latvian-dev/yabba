package com.latmod.yabba.tile;

import com.feed_the_beast.ftbl.lib.config.ConfigBoolean;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.tile.EnumSaveType;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.DataStorage;
import com.feed_the_beast.ftbl.lib.util.InvUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonObject;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.YabbaLang;
import com.latmod.yabba.api.BarrelType;
import com.latmod.yabba.api.YabbaConfigEvent;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.item.upgrade.ItemUpgradeHopper;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import com.latmod.yabba.util.UpgradeInst;
import it.unimi.dsi.fastutil.ints.Int2ByteOpenHashMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class TileItemBarrel extends TileBarrelBase implements IItemHandlerModifiable
{
	private static final Int2ByteOpenHashMap ALLOWED_ORE_NAME_CACHE = new Int2ByteOpenHashMap();

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

			for (String v : YabbaConfig.general.allowed_ore_prefixes)
			{
				if (name.startsWith(v))
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

	public ItemStack storedItem = ItemStack.EMPTY;
	public int itemCount = 0;
	private String cachedItemName, cachedItemCount;
	private int prevItemCount = -1;
	private final ConfigBoolean disableOreItems = new ConfigBoolean(false);
	private int cachedSlotCount = -1;

	@Override
	public BarrelType getType()
	{
		return BarrelType.ITEM;
	}

	@Override
	public void markBarrelDirty(boolean majorChange)
	{
		super.markBarrelDirty(majorChange);
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		cachedItemName = null;
		cachedItemCount = null;
		//prevItemCount = -1;
		cachedSlotCount = -1;
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
		nbt.setInteger("Count", itemCount);

		if (type == EnumSaveType.NET_UPDATE)
		{
			nbt.setInteger("PrevCount", prevItemCount);

			if (prevItemCount != -1)
			{
				return;
			}
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
		setRawItemCount(nbt.getInteger("Count"));

		if (type == EnumSaveType.NET_UPDATE)
		{
			prevItemCount = nbt.getInteger("PrevCount");

			if (prevItemCount != -1)
			{
				return;
			}
		}

		super.readData(nbt, type);

		if (nbt.hasKey("Item"))
		{
			setStoredItemType(new ItemStack(nbt.getCompoundTag("Item")), itemCount);
		}
		else
		{
			setStoredItemType(ItemStack.EMPTY, 0);
		}
	}

	@Override
	public void update()
	{
		if (!world.isRemote && hasUpgrade(YabbaItems.UPGRADE_HOPPER) && (world.getTotalWorldTime() % 8L) == (pos.hashCode() & 7))
		{
			ItemUpgradeHopper.Data data = (ItemUpgradeHopper.Data) getUpgradeData(YabbaItems.UPGRADE_HOPPER);
			boolean ender = false;//TODO: Ender hopper upgrade
			int maxItems = ender ? 64 : 1;

			if (itemCount > 0 && data.down.getBoolean())
			{
				TileEntity tileDown = world.getTileEntity(pos.offset(EnumFacing.DOWN));

				if (tileDown != null && tileDown.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
				{
					InvUtils.transferItems(this, tileDown.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP), Math.min(maxItems, itemCount), CommonUtils.alwaysTruePredicate());
				}
			}

			if (data.up.getBoolean())
			{
				TileEntity tileUp = world.getTileEntity(pos.offset(EnumFacing.UP));

				if (tileUp != null && tileUp.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
				{
					InvUtils.transferItems(tileUp.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN), this, Math.min(maxItems, getFreeSpace()), CommonUtils.alwaysTruePredicate());
				}
			}

			if (data.collect.getBoolean())
			{
				AxisAlignedBB aabb = new AxisAlignedBB(pos.add(0, 1, 0), pos.add(1, 2, 1));

				if (ender)
				{
					aabb = aabb.expand(5D, 5D, 5D);
				}

				for (EntityItem item : world.getEntitiesWithinAABB(EntityItem.class, aabb, null))
				{
					ItemStack stack = insertItem(0, item.getItem().copy(), false);

					if (stack.isEmpty())
					{
						item.setDead();
					}
					else
					{
						item.setItem(stack);
					}
				}
			}
		}

		if (prevItemCount == -1 || prevItemCount != itemCount)
		{
			if (!world.isRemote)
			{
				sendDirtyUpdate();
			}

			if (prevItemCount == -1 || prevItemCount == 0 || itemCount == 0)
			{
				updateContainingBlockInfo();
			}

			if (hasUpgrade(YabbaItems.UPGRADE_REDSTONE_OUT))
			{
				world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
			}

			prevItemCount = itemCount;
		}
	}

	@Override
	public String getItemDisplayName()
	{
		if (cachedItemName == null)
		{
			cachedItemName = storedItem.isEmpty() ? "" : storedItem.getDisplayName();
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
			return infinite ? Integer.toString(itemCount) : (itemCount + " / " + getMaxItems(storedItem));
		}
		else if (cachedItemCount == null)
		{
			int max = storedItem.isEmpty() ? 64 : storedItem.getMaxStackSize();

			if (max == 1 || itemCount <= max)
			{
				cachedItemCount = Integer.toString(itemCount);
			}
			else
			{
				cachedItemCount = (itemCount / max) + "x" + max;
				int extra = itemCount % max;
				if (extra != 0)
				{
					cachedItemCount += "+" + extra;
				}
			}
		}

		return cachedItemCount;
	}

	private void setRawItemCount(int v)
	{
		itemCount = v;
		cachedItemCount = null;
		cachedSlotCount = -1;
	}

	public boolean setItemCount(int v)
	{
		if (itemCount != v)
		{
			setRawItemCount(v);

			if (itemCount <= 0 && !isLocked.getBoolean() && !storedItem.isEmpty())
			{
				setStoredItemType(ItemStack.EMPTY, 0);
			}
			else
			{
				markBarrelDirty(false);
			}

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

		return tier.maxItemStacks * (stack.isEmpty() ? 1 : stack.getMaxStackSize());
	}

	public void setStoredItemType(ItemStack type, int amount)
	{
		prevItemCount = itemCount;
		boolean prevEmpty = storedItem.isEmpty();

		if (type.isEmpty())
		{
			type = ItemStack.EMPTY;
		}

		if (amount <= 0)
		{
			amount = 0;

			if (!isLocked.getBoolean())
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

		if (prevEmpty != storedItem.isEmpty())
		{
			prevItemCount = -1;
		}
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (itemCount <= 0)
		{
			return ItemStack.EMPTY;
		}

		int maxStack = storedItem.getMaxStackSize();

		if (tier.creative())
		{
			return ItemHandlerHelper.copyStackWithSize(storedItem, maxStack);
		}

		int slotCount = getSlots();

		if (slot >= slotCount - 1)
		{
			return ItemStack.EMPTY;
		}
		else if (slot < slotCount - 2)
		{
			return ItemHandlerHelper.copyStackWithSize(storedItem, maxStack);
		}

		int stackSize = itemCount % maxStack;

		if (stackSize == 0)
		{
			stackSize = maxStack;
		}

		return ItemHandlerHelper.copyStackWithSize(storedItem, stackSize);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		if (storedItem.isEmpty())
		{
			setStoredItemType(stack, stack.getCount());
		}
		else
		{
			//TODO: Check me
			setRawItemCount(itemCount + (stack.getCount() - getStackInSlot(slot).getCount()));
			markBarrelDirty(false);
		}
	}

	@Override
	public int getSlots()
	{
		if (cachedSlotCount == -1)
		{
			if (itemCount <= 0)
			{
				cachedSlotCount = 1;
			}
			else
			{
				cachedSlotCount = 1 + itemCount / storedItem.getMaxStackSize();
			}
		}

		return cachedSlotCount;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		boolean storedIsEmpty = storedItem.isEmpty();
		boolean canInsert = storedIsEmpty || canInsertItem(storedItem, stack, !disableOreItems.getBoolean());
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
		return 64;
	}

	public int getFreeSpace()
	{
		return getMaxItems(storedItem) - itemCount;
	}

	@Override
	public void addItem(EntityPlayer player, EnumHand hand)
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
	public void addAllItems(EntityPlayer player, EnumHand hand)
	{
		if (storedItem.isEmpty())
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
	public void removeItem(EntityPlayer player, boolean removeStack)
	{
		if (!storedItem.isEmpty() && itemCount == 0 && !isLocked.getBoolean())
		{
			setStoredItemType(ItemStack.EMPTY, 0);
			markBarrelDirty(true);
			return;
		}

		if (!storedItem.isEmpty() && itemCount > 0)
		{
			int size;

			if (removeStack)
			{
				size = storedItem.getMaxStackSize();
			}
			else
			{
				size = 1;
			}

			ItemStack stack = extractItem(0, size, false);

			if (!stack.isEmpty())
			{
				int slot = player.inventory.currentItem;

				if (!player.inventory.getStackInSlot(slot).isEmpty())
				{
					slot = -1;
				}

				if (player.inventory.add(slot, stack))
				{
					player.inventory.markDirty();

					if (player.openContainer != null)
					{
						player.openContainer.detectAndSendChanges();
					}
				}
				else
				{
					EntityItem entityItem = player.dropItem(stack, false);

					if (entityItem != null)
					{
						entityItem.setNoPickupDelay();
						entityItem.setOwner(player.getName());
					}
				}
			}
			//return !playerIn.isSneaking();
		}
		//return getItemCount() > 0;
	}

	@Override
	public void saveConfig(ConfigGroup group, ICommandSender sender, JsonObject json)
	{
		super.saveConfig(group, sender, json);
		setStoredItemType(storedItem, itemCount);
	}

	@Override
	public void createConfig(YabbaConfigEvent event)
	{
		super.createConfig(event);
		String group = Yabba.MOD_ID;
		event.getConfig().setGroupName(group, new TextComponentString(Yabba.MOD_NAME));
		event.getConfig().add(group, "disable_ore_items", disableOreItems);

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

		if (isLocked.getBoolean())
		{
			tooltip.add(StringUtils.translate("barrel_config.yabba.locked"));
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
	public int redstoneOutput(EnumFacing facing)
	{
		DataStorage data = getUpgradeData(YabbaItems.UPGRADE_REDSTONE_OUT);

		if (data instanceof ItemUpgradeRedstone.Data)
		{
			return ((ItemUpgradeRedstone.Data) data).redstoneOutput(facing, itemCount);
		}

		return 0;
	}

	@Override
	public boolean shouldDrop()
	{
		return super.shouldDrop() || itemCount > 0 || !storedItem.isEmpty() || disableOreItems.getBoolean();
	}
}