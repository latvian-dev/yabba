package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.item.IUpgrade;
import com.latmod.yabba.item.ItemYabba;

/**
 * @author LatvianModder
 */
public class ItemUpgrade extends ItemYabba implements IUpgrade
{
	public ItemUpgrade(String id)
	{
		super(id);
	}

	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		return true;
	}
}