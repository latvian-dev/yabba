package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.lib.util.DataStorage;
import com.latmod.yabba.item.IUpgrade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class UpgradeInst
{
	private final Item item;
	private final DataStorage data;
	private final ItemStack stack;

	public UpgradeInst(Item _item)
	{
		item = _item;
		data = getUpgrade().createBarrelUpgradeData();
		stack = new ItemStack(item);
	}

	public Item getItem()
	{
		return item;
	}

	public DataStorage getData()
	{
		return data;
	}

	public IUpgrade getUpgrade()
	{
		return (IUpgrade) item;
	}

	public ItemStack getStack()
	{
		return stack;
	}
}