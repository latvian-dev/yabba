package com.latmod.yabba.tile;

import com.feed_the_beast.ftbl.lib.config.ConfigBoolean;
import com.feed_the_beast.ftbl.lib.tile.EnumSaveType;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.DataStorage;
import com.feed_the_beast.ftbl.lib.util.InvUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.api.BarrelType;
import com.latmod.yabba.api.YabbaCreateConfigEvent;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.item.YabbaItems;
import com.latmod.yabba.item.upgrade.ItemUpgradeHopper;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import com.latmod.yabba.net.MessageUpdateItemBarrelCount;
import gnu.trove.map.hash.TIntByteHashMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class TileItemBarrel extends TileBarrelBase implements IDeepStorageUnit, IItemHandlerModifiable
{
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

	@Override
	public BarrelType getType()
	{
		return BarrelType.ITEM;
	}

	@Override
	public void markBarrelDirty(boolean majorChange)
	{
		super.markBarrelDirty(majorChange);
		prevItemCount = -1;
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		cachedItemName = null;
		cachedItemCount = null;
		prevItemCount = -1;
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
		super.writeData(nbt, type);

		if (!storedItem.isEmpty())
		{
			storedItem.setCount(1);
			nbt.setTag("Item", storedItem.serializeNBT());
			nbt.setInteger("Count", itemCount);
		}
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		super.readData(nbt, type);

		if (nbt.hasKey("Item"))
		{
			setStoredItemType(new ItemStack(nbt.getCompoundTag("Item")), nbt.getInteger("Count"));
		}
		else
		{
			setStoredItemType(ItemStack.EMPTY, 0);
		}
	}

	@Override
	public void update()
	{
		if (prevItemCount == -1 || prevItemCount != itemCount)
		{
			if (prevItemCount == -1)
			{
				sendDirtyUpdate();
			}

			if (!world.isRemote)
			{
				new MessageUpdateItemBarrelCount(pos, itemCount).sendToAllAround(world.provider.getDimension(), pos, 300D);
			}

			if (hasUpgrade(YabbaItems.UPGRADE_REDSTONE_OUT))
			{
				world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
			}

			prevItemCount = itemCount;
		}

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

	public boolean setItemCount(int v)
	{
		if (itemCount != v)
		{
			itemCount = v;
			markBarrelDirty(false);
			return true;
		}

		return false;
	}

	@Override
	public ItemStack getStoredItemType()
	{
		return ItemHandlerHelper.copyStackWithSize(storedItem, itemCount);
	}

	public int getMaxItems(ItemStack stack)
	{
		if (tier.infiniteCapacity())
		{
			return Tier.MAX_STACKS;
		}

		return tier.maxItemStacks * (stack.isEmpty() ? 1 : stack.getMaxStackSize());
	}

	@Override
	public void setStoredItemCount(int amount)
	{
		setStoredItemType(storedItem, amount);
	}

	@Override
	public void setStoredItemType(ItemStack type, int amount)
	{
		boolean wasEmpty = itemCount == 0;

		if (amount <= 0 || type == null || type.isEmpty())
		{
			type = ItemStack.EMPTY;
			amount = 0;
		}
		else
		{
			type = ItemHandlerHelper.copyStackWithSize(type, 1);

			if (tier.creative())
			{
				amount = Tier.MAX_STACKS / 2;
			}
		}

		storedItem = type;
		itemCount = amount;
		markBarrelDirty(wasEmpty != (amount == 0));
	}

	@Override
	public int getMaxStoredCount()
	{
		return getMaxItems(storedItem) + (hasUpgrade(YabbaItems.UPGRADE_VOID) ? 256 : 0);
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
						//TODO: Check me
						setStoredItemType(stack, 1);
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

		if (itemCount <= 0 || storedItem.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		if (tier.creative())
		{
			return ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(amount, itemCount));
		}

		ItemStack stack = ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(Math.min(amount, itemCount), storedItem.getMaxStackSize()));

		if (!simulate)
		{
			setItemCount(itemCount - stack.getCount());

			if (itemCount - stack.getCount() <= 0 && !isLocked.getBoolean())
			{
				setStoredItemType(ItemStack.EMPTY, 0);
			}
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
		heldItem.setCount(insertItem(0, heldItem, false).getCount());
	}

	@Override
	public void addAllItems(EntityPlayer player, EnumHand hand)
	{
		if (storedItem.isEmpty())
		{
			return;
		}

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
				}
			}
		}
	}

	@Override
	public void removeItem(EntityPlayer player, boolean removeStack)
	{
		ItemStack storedItem = getStoredItemType();
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
				if (player.inventory.addItemStackToInventory(stack))
				{
					player.inventory.markDirty();

					if (player.openContainer != null)
					{
						player.openContainer.detectAndSendChanges();
					}
				}
				else
				{
					EntityItem ei = new EntityItem(player.world, player.posX, player.posY, player.posZ, stack);
					ei.motionX = ei.motionY = ei.motionZ = 0D;
					ei.setPickupDelay(0);
					player.world.spawnEntity(ei);
				}
			}
			//return !playerIn.isSneaking();
		}
		//return getItemCount() > 0;
	}

	@Override
	public void createConfig(YabbaCreateConfigEvent event)
	{
		super.createConfig(event);
		String group = Yabba.MOD_ID;
		event.add(group, "disable_ore_items", disableOreItems);

		if (!tier.creative())
		{
			event.add(group, "locked", isLocked);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip, ITooltipFlag flagIn)
	{
		if (isLocked.getBoolean())
		{
			tooltip.add(StringUtils.translate("barrel_config.yabba.locked.name"));
		}

		if (!storedItem.isEmpty())
		{
			tooltip.add("Item: " + storedItem.getDisplayName()); //LANG
		}

		if (!tier.creative())
		{
			if (tier.infiniteCapacity())
			{
				tooltip.add(itemCount + " items"); //LANG
			}
			else if (!storedItem.isEmpty())
			{
				tooltip.add(itemCount + " / " + getMaxItems(storedItem));
			}
			else
			{
				tooltip.add("Max " + tier.maxItemStacks + " stacks"); //LANG
			}
		}

		super.addInformation(tooltip, flagIn);
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