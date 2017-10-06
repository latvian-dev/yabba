package com.latmod.yabba.item;

import com.feed_the_beast.ftbl.lib.util.misc.DataStorage;
import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.api.RemoveUpgradeEvent;

/**
 * @author LatvianModder
 */
public interface IUpgrade
{
	boolean applyOn(ApplyUpgradeEvent event);

	default void onRemoved(RemoveUpgradeEvent event)
	{
	}

	default DataStorage createBarrelUpgradeData()
	{
		return DataStorage.EMPTY;
	}
}