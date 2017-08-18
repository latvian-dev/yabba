package com.latmod.yabba.tile;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.BasicConfigContainer;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.tile.EnumSaveType;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.DataStorage;
import com.feed_the_beast.ftbl.lib.util.InvUtils;
import com.google.gson.JsonObject;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.api.YabbaCreateConfigEvent;
import com.latmod.yabba.item.IUpgrade;
import com.latmod.yabba.item.YabbaItems;
import com.latmod.yabba.item.upgrade.ItemUpgradeHopper;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import com.latmod.yabba.net.MessageUpdateBarrelItemCount;
import com.latmod.yabba.util.UpgradeInst;
import gnu.trove.map.hash.TIntByteHashMap;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
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

			for (IConfigValue v : YabbaConfig.ALLOWED_ORE_PREFIXES.getList())
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

	public ItemStack storedItem = ItemStack.EMPTY;
	public int itemCount = 0;
	public PropertyBool disableOreItems = new PropertyBool(false);

	public TileItemBarrel()
	{
		clearCachedData();
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

		storedItem = nbt.hasKey("Item") ? new ItemStack(nbt.getCompoundTag("Item")) : ItemStack.EMPTY;
		itemCount = storedItem.isEmpty() ? 0 : nbt.getInteger("Count");

		if (hasUpgrade(YabbaItems.UPGRADE_CREATIVE))
		{
			itemCount = tier.getMaxItems(this, storedItem) / 2;
		}

		if (itemCount > 0)
		{
			storedItem.setCount(itemCount);
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
				new MessageUpdateBarrelItemCount(pos, itemCount).sendToAllAround(world.provider.getDimension(), pos, 300D);
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
			return infinite ? Integer.toString(itemCount) : (itemCount + " / " + tier.getMaxItems(this, storedItem));
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
		boolean wasEmpty = itemCount == 0;

		if (amount <= 0 || type == null || type.isEmpty())
		{
			type = ItemStack.EMPTY;
			amount = 0;
		}

		storedItem = type;
		itemCount = amount;
		markBarrelDirty(wasEmpty != (amount == 0));
	}

	@Override
	public int getMaxStoredCount()
	{
		return tier.getMaxItems(this, storedItem) + (hasUpgrade(YabbaItems.UPGRADE_VOID) ? 256 : 0);
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
		if (!storedIsEmpty && hasUpgrade(YabbaItems.UPGRADE_CREATIVE))
		{
			return canInsert ? ItemStack.EMPTY : stack;
		}

		int capacity;

		if (itemCount > 0)
		{
			capacity = tier.getMaxItems(this, storedItem);

			if (itemCount >= capacity)
			{
				return (canInsert && hasUpgrade(YabbaItems.UPGRADE_VOID)) ? ItemStack.EMPTY : stack;
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

		if (itemCount <= 0 || storedItem.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		if (hasUpgrade(YabbaItems.UPGRADE_CREATIVE))
		{
			return ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(amount, itemCount));
		}

		ItemStack stack = ItemHandlerHelper.copyStackWithSize(storedItem, Math.min(Math.min(amount, itemCount), storedItem.getMaxStackSize()));

		if (!simulate)
		{
			setItemCount(itemCount - stack.getCount());

			if (itemCount - stack.getCount() <= 0 && !isLocked)
			{
				setStoredItemType(ItemStack.EMPTY, 0);
			}
		}

		return stack;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return tier.getMaxItems(this, storedItem);
	}

	public int getFreeSpace()
	{
		return tier.getMaxItems(this, storedItem) - itemCount;
	}

	public void onLeftClick(EntityPlayer playerIn)
	{
		ItemStack storedItem = getStoredItemType();
		if (!storedItem.isEmpty() && itemCount == 0 && !isLocked)
		{
			setStoredItemType(ItemStack.EMPTY, 0);
			markBarrelDirty(true);
			return;
		}

		if (!storedItem.isEmpty() && itemCount > 0)
		{
			int size;

			if (YabbaConfig.SNEAK_LEFT_CLICK_EXTRACTS_STACK.getBoolean() == playerIn.isSneaking())
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
				if (playerIn.inventory.addItemStackToInventory(stack))
				{
					playerIn.inventory.markDirty();

					if (playerIn.openContainer != null)
					{
						playerIn.openContainer.detectAndSendChanges();
					}
				}
				else
				{
					EntityItem ei = new EntityItem(playerIn.world, playerIn.posX, playerIn.posY, playerIn.posZ, stack);
					ei.motionX = ei.motionY = ei.motionZ = 0D;
					ei.setPickupDelay(0);
					playerIn.world.spawnEntity(ei);
				}
			}
			//return !playerIn.isSneaking();
		}
		//return getItemCount() > 0;
	}

	public void onRightClick(EntityPlayer playerIn, IBlockState state, EnumHand hand, float hitX, float hitY, float hitZ, EnumFacing facing, long deltaClickTime)
	{
		if (deltaClickTime <= 8)
		{
			if (!getStoredItemType().isEmpty())
			{
				for (int i = 0; i < playerIn.inventory.mainInventory.size(); i++)
				{
					ItemStack stack0 = playerIn.inventory.mainInventory.get(i);
					ItemStack is = insertItem(0, stack0, false);
					stack0 = playerIn.inventory.mainInventory.get(i);

					if (is != stack0)
					{
						stack0.setCount(is.getCount());

						if (stack0.isEmpty())
						{
							playerIn.inventory.mainInventory.set(i, ItemStack.EMPTY);
						}
					}
				}
			}

			playerIn.inventory.markDirty();

			if (playerIn.openContainer != null)
			{
				playerIn.openContainer.detectAndSendChanges();
			}

			return;
		}

		ItemStack heldItem = playerIn.getHeldItem(hand);
		if (heldItem.isEmpty())
		{
			if (playerIn.isSneaking())
			{
				float x;

				if (facing == EnumFacing.UP || facing == EnumFacing.DOWN)
				{
					x = getX(state.getValue(BlockHorizontal.FACING), hitX, hitZ);
				}
				else
				{
					x = getX(facing, hitX, hitZ);
				}

				if (x < BUTTON_SIZE)
				{
					IConfigTree tree = new ConfigTree();
					new YabbaCreateConfigEvent(this, tree).post();
					FTBLibAPI.API.editServerConfig((EntityPlayerMP) playerIn, null, new BasicConfigContainer(new TextComponentTranslation(getBlockType().getUnlocalizedName() + ".name"), tree)
					{
						@Override
						public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
						{
							super.saveConfig(sender, nbt, json);
							markBarrelDirty(true);
						}
					});
				}
				else if (x > 1D - BUTTON_SIZE && !hasUpgrade(YabbaItems.UPGRADE_CREATIVE))
				{
					isLocked = !isLocked;

					if (!getStoredItemType().isEmpty() && itemCount == 0 && !isLocked)
					{
						setStoredItemType(ItemStack.EMPTY, 0);
					}
				}

				markBarrelDirty(true);
				return;
			}

			playerIn.inventory.markDirty();

			if (playerIn.openContainer != null)
			{
				playerIn.openContainer.detectAndSendChanges();
			}
		}
		else
		{
			if (heldItem.getItem() instanceof IUpgrade)
			{
				if (!hasUpgrade(heldItem.getItem()))
				{
					ApplyUpgradeEvent event = new ApplyUpgradeEvent(false, this, new UpgradeInst(heldItem), playerIn, hand, facing);

					if (event.getUpgrade().getUpgrade().applyOn(event))
					{
						if (event.consumeItem())
						{
							heldItem.shrink(1);
						}

						upgrades.put(heldItem.getItem(), event.getUpgrade());
						markBarrelDirty(true);
					}
				}
			}
			else
			{
				heldItem.setCount(insertItem(0, heldItem, false).getCount());
			}
		}

		markBarrelDirty(true);
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