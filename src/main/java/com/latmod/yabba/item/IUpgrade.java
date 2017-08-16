package com.latmod.yabba.item;

import com.feed_the_beast.ftbl.lib.util.DataStorage;
import com.latmod.yabba.api.events.ApplyUpgradeEvent;

/**
 * @author LatvianModder
 */
public interface IUpgrade
{
	boolean applyOn(ApplyUpgradeEvent event);

	default DataStorage createBarrelUpgradeData()
	{
		return DataStorage.EMPTY;
	}
}