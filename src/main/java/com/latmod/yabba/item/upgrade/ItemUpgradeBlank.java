package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.ApplyUpgradeEvent;

/**
 * @author LatvianModder
 */
public class ItemUpgradeBlank extends ItemUpgrade
{
	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		return false;
	}
}