package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.block.Tier;

/**
 * @author LatvianModder
 */
public class ItemUpgradeTier extends ItemUpgrade
{
	private final Tier tier;

	public ItemUpgradeTier(Tier t)
	{
		tier = t;
	}

	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		event.setAddUpgrade(false);
		return (event.getPlayer().capabilities.isCreativeMode || event.getBarrel().getTier() == tier.getPrevious() || event.getBarrel().getTier() == Tier.STONE && tier == Tier.WOOD) && event.getBarrel().setTier(tier, event.simulate());
	}
}