package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.RemoveUpgradeEvent;
import com.latmod.yabba.item.YabbaItems;

/**
 * @author LatvianModder
 */
public class ItemUpgradeInfiniteCapacity extends ItemUpgrade
{
	public ItemUpgradeInfiniteCapacity(String id)
	{
		super(id);
	}

	@Override
	public void onRemoved(RemoveUpgradeEvent event)
	{
		event.getBarrel().removeUpgrade(YabbaItems.UPGRADE_CREATIVE, false);
	}
}