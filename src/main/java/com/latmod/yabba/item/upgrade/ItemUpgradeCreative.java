package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.api.BarrelType;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.tile.IItemBarrel;

/**
 * @author LatvianModder
 */
public class ItemUpgradeCreative extends ItemUpgrade
{
	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		event.setAddUpgrade(false);

		if (event.getBarrel().getTier().creative())
		{
			return false;
		}

		switch (event.getBarrel().getType())
		{
			case ITEM:
				if (((IItemBarrel) event.getBarrel()).getItemCount() <= 0)
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
				event.getBarrel().setLocked(false);

				if (event.getBarrel().getType() == BarrelType.ITEM)
				{
					IItemBarrel itemBarrel = (IItemBarrel) event.getBarrel();
					itemBarrel.setStoredItemType(itemBarrel.getStoredItemType(), 1);
				}
			}

			return true;
		}

		return false;
	}
}