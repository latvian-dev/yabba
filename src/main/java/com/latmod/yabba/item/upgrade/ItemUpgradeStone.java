package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.block.Tier;

/**
 * @author LatvianModder
 */
public class ItemUpgradeStone extends ItemUpgrade
{
	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		event.setAddUpgrade(false);
		return (event.getPlayer().capabilities.isCreativeMode || event.getBarrel().getTier() == Tier.WOOD) && event.getBarrel().setTier(Tier.STONE, event.simulate());
	}
}