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

		boolean simulate = event.simulate();
		if (event.getBarrel().setTier(Tier.CREATIVE, simulate))
		{
			if (!simulate)
			{
				if (event.getBarrel().getType() == BarrelType.ITEM)
				{
					((TileItemBarrel) event.getBarrel()).setItemCount(1000000000);
				}

				event.getBarrel().isLocked.setBoolean(false);
			}

			return true;
		}

		return false;
	}
}