package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.lib.util.DataStorage;
import com.latmod.yabba.item.IUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * @author LatvianModder
 */
public class UpgradeInst
{
	private final ItemStack stack;
	private final DataStorage data;

	public UpgradeInst(ItemStack _stack)
	{
		stack = ItemHandlerHelper.copyStackWithSize(_stack, 1);
		stack.setCount(1);
		data = getUpgrade().createBarrelUpgradeData();
	}

	public ItemStack getStack()
	{
		return stack;
	}

	public DataStorage getData()
	{
		return data;
	}

	public IUpgrade getUpgrade()
	{
		return (IUpgrade) stack.getItem();
	}
}