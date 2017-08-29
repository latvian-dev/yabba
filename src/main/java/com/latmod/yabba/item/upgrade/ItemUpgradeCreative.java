package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.item.YabbaItems;
import com.latmod.yabba.tile.TileBarrelBase;
import com.latmod.yabba.tile.TileItemBarrel;

/**
 * @author LatvianModder
 */
public class ItemUpgradeCreative extends ItemUpgrade
{
	public ItemUpgradeCreative(String id)
	{
		super(id);
	}

	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		if (!event.simulate())
		{
			TileBarrelBase barrel = event.getBarrel();

			if (barrel instanceof TileItemBarrel)
			{
				((TileItemBarrel) barrel).setItemCount(1000000000);
			}

			barrel.addUpgrade(YabbaItems.UPGRADE_INFINITE_CAPACITY, false);
			barrel.isLocked.setBoolean(false);
		}

		return true;
	}
}