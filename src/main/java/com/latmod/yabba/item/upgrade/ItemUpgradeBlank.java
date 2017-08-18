package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.ApplyUpgradeEvent;

/**
 * @author LatvianModder
 */
public class ItemUpgradeBlank extends ItemUpgrade
{
	public ItemUpgradeBlank(String id)
	{
		super(id);
	}

	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		return false;
	}
}