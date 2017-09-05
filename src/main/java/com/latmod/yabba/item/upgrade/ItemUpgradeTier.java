package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.ApplyUpgradeEvent;
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
		event.setAddUpgrade(false);
		return event.getBarrel().tier == tier.getPrevious() && event.getBarrel().setTier(tier, event.simulate());

	}
}