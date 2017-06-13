package com.latmod.yabba;

import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class YabbaCreativeTab extends CreativeTabs
{
	public YabbaCreativeTab()
	{
		super(Yabba.MOD_ID);
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.BLANK.metadata);
	}
}