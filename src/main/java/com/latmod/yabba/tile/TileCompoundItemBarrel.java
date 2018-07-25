package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.latmod.yabba.api.YabbaConfigEvent;
import com.latmod.yabba.util.UpgradeInst;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TileCompoundItemBarrel extends TileBarrelBase implements IItemBarrel
{
	private ItemStack storedItem = ItemStack.EMPTY;
	private int itemCount = 0;

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
	public void markDirty()
	{
		sendDirtyUpdate();
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
	}

	@Override
	public void createConfig(YabbaConfigEvent event)
	{
		super.createConfig(event);

		if (!tier.creative())
		{
			event.getConfig().add("locked", isLocked);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(I18n.format("lang.yabba.tier", I18n.format(tier.getLangKey())));

		if (isLocked())
		{
			tooltip.add(I18n.format("barrel_config.yabba.locked"));
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
	public boolean shouldDrop()
	{
		return itemCount > 0 || super.shouldDrop();
	}
}