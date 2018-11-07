package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.feed_the_beast.ftblib.lib.util.InvUtils;
import com.feed_the_beast.ftblib.lib.util.misc.DataStorage;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.YabbaConfigEvent;
import com.latmod.yabba.item.upgrade.ItemUpgradeHopper;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import com.latmod.yabba.util.UpgradeInst;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TileItemBarrel extends TileAdvancedBarrelBase implements ITickable, IItemBarrel
{
	private ItemStack storedItem = ItemStack.EMPTY;
	private int itemCount = 0;
	private String cachedItemName, cachedItemCount;
	private int prevItemCount = -1, prevItemCountForNet = -1;

	@Override
	public void markBarrelDirty(boolean majorChange)
	{
		if (majorChange)
		{
			prevItemCount = -1;
			prevItemCountForNet = -1;
		}

		super.markBarrelDirty(majorChange);
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		cachedItemName = null;
		cachedItemCount = null;
		//prevItemCount = -1;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? (T) this : super.getCapability(capability, facing);
	}

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (itemCount > 0)
		{
			nbt.setInteger("Count", itemCount);
		}

		if (type == EnumSaveType.NET_UPDATE)
		{
			if (prevItemCountForNet != 0)
			{
				nbt.setInteger("PrevCount", prevItemCountForNet);
			}

			if (prevItemCountForNet != -1)
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
		prevItemCount = prevItemCountForNet = -1;

		if (type == EnumSaveType.NET_UPDATE)
		{
			prevItemCount = prevItemCountForNet = nbt.getInteger("PrevCount");
			cachedItemCount = null;

			if (prevItemCount != -1)
			{
				return;
			}
		}

		super.readData(nbt, type);

		storedItem = nbt.hasKey("Item") ? new ItemStack(nbt.getCompoundTag("Item")) : ItemStack.EMPTY;

		if (storedItem.isEmpty())
		{
			storedItem = ItemStack.EMPTY;
		}

		updateContainingBlockInfo();
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
		prevItemCountForNet = prevItemCount;

		if (!world.isRemote && hasUpgrade(YabbaItems.UPGRADE_HOPPER) && (world.getTotalWorldTime() % 8L) == (pos.hashCode() & 7))
		{
			ItemUpgradeHopper.Data data = (ItemUpgradeHopper.Data) getUpgradeData(YabbaItems.UPGRADE_HOPPER);
			boolean ender = false;//TODO: Ender hopper upgrade
			int maxItems = ender ? 64 : 1;

			if (itemCount > 0 && data.down)
			{
				TileEntity tileDown = world.getTileEntity(pos.offset(EnumFacing.DOWN));

				if (tileDown != null && tileDown.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
				{
					InvUtils.transferItems(this, tileDown.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP), Math.min(maxItems, itemCount), InvUtils.NO_FILTER);
				}
			}

			if (data.up)
			{
				TileEntity tileUp = world.getTileEntity(pos.offset(EnumFacing.UP));

				if (tileUp != null && tileUp.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
				{
					InvUtils.transferItems(tileUp.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN), this, Math.min(maxItems, getFreeSpace()), InvUtils.NO_FILTER);
				}
			}

			if (data.collect)
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
			if (world != null)
			{
				if (!world.isRemote)
				{
					updateContainingBlockInfo();
				}

				world.markChunkDirty(pos, this);

				if (!world.isRemote || prevItemCount == -1)
				{
					BlockUtils.notifyBlockUpdate(world, pos, getBlockState());
				}

				if (hasUpgrade(YabbaItems.UPGRADE_REDSTONE_OUT))
				{
					world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
				}
			}

			prevItemCount = itemCount;
		}
	}

	@Override
	public String getItemDisplayName()
	{
		if (cachedItemName == null)
		{
			cachedItemName = storedItem.isEmpty() ? "" : TextFormatting.BOLD + storedItem.getDisplayName();
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

	@Override
	public int getItemCount()
	{
		return itemCount;
	}

	@Override
	public void setRawItemCount(int v)
	{
		itemCount = v;
		cachedItemCount = null;
	}

	@Override
	public ItemStack getStoredItemType()
	{
		return storedItem;
	}

	@Override
	public void setRawItemType(ItemStack type)
	{
		storedItem = type;
		cachedItemName = null;
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
		if (!storedItem.isEmpty() && itemCount == 0 && !isLocked())
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
	public void onConfigSaved(ConfigGroup group, ICommandSender sender)
	{
		setStoredItemType(storedItem, itemCount);
	}

	@Override
	public void createConfig(YabbaConfigEvent event)
	{
		super.createConfig(event);

		if (!tier.creative())
		{
			event.getConfig().addBool("locked", () -> isLocked, v -> isLocked = v, false);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(I18n.format("lang.yabba.tier", I18n.format(tier.getLangKey())));

		if (isLocked())
		{
			tooltip.add(I18n.format("barrel_config.locked"));
		}

		if (!storedItem.isEmpty())
		{
			tooltip.add(I18n.format("lang.yabba.item", storedItem.getDisplayName()));
		}

		if (!tier.creative())
		{
			if (tier.infiniteCapacity())
			{
				tooltip.add(I18n.format("lang.yabba.item_count_inf", itemCount));
			}
			else if (!storedItem.isEmpty())
			{
				tooltip.add(I18n.format("lang.yabba.item_count", itemCount, getMaxItems(storedItem)));
			}
			else
			{
				tooltip.add(I18n.format("lang.yabba.item_count_max", tier.maxItemStacks));
			}
		}

		if (!upgrades.isEmpty())
		{
			tooltip.add(I18n.format("lang.yabba.upgrades"));

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
}