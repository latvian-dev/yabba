package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.events.ApplyUpgradeEvent;
import com.latmod.yabba.block.Tier;

/**
 * @author LatvianModder
 */
public class ItemUpgradeTier extends ItemUpgrade
{
	private final Tier tier;

	public ItemUpgradeTier(String id, Tier t)
	{
		super(id);
		tier = t;
	}

	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		if (event.getBarrel().tier == tier.getPrevious())
		{
			if (!event.simulate())
			{
				event.getBarrel().setTier(tier);
			}

			return true;
		}

		return false;
	}
}