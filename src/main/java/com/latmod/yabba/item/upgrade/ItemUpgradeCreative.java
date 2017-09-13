package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.api.BarrelType;
import com.latmod.yabba.block.Tier;
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
		event.setAddUpgrade(false);

		if (event.getBarrel().tier.creative())
		{
			return false;
		}

		switch (event.getBarrel().getType())
		{
			case ITEM:
				if (((TileItemBarrel) event.getBarrel()).itemCount <= 0)
				{
					return false;
				}
				break;
			default:
				return false;
		}

		boolean simulate = event.simulate();
		if (event.getBarrel().setTier(Tier.CREATIVE, simulate))
		{
			if (!simulate)
			{
				event.getBarrel().isLocked.setBoolean(false);

				if (event.getBarrel().getType() == BarrelType.ITEM)
				{
					TileItemBarrel itemBarrel = (TileItemBarrel) event.getBarrel();
					itemBarrel.setStoredItemType(itemBarrel.storedItem, 1);
				}
			}

			return true;
		}

		return false;
	}
}