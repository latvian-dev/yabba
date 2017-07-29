package com.latmod.yabba.item;

import com.latmod.yabba.Yabba;
import net.minecraft.item.Item;

/**
 * @author LatvianModder
 */
public class ItemYabba extends Item
{
	public ItemYabba(String id)
	{
		setRegistryName(Yabba.MOD_ID + ':' + id);
		setUnlocalizedName(Yabba.MOD_ID + '.' + id);
		setCreativeTab(Yabba.TAB);
	}
}