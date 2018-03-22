package com.latmod.yabba.tile;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * @author LatvianModder
 */
public interface IItemBarrel extends IBarrelBase, IItemHandler
{
	int getItemCount();

	ItemStack getStoredItemType();

	void setStoredItemType(ItemStack type, int amount);
}