package com.latmod.yabba.item;

import com.feed_the_beast.ftbl.lib.item.ItemBase;
import com.latmod.yabba.Yabba;

/**
 * @author LatvianModder
 */
public class ItemYabba extends ItemBase
{
	public ItemYabba(String id)
	{
		super(Yabba.MOD_ID, id);
		setCreativeTab(Yabba.TAB);
	}
}