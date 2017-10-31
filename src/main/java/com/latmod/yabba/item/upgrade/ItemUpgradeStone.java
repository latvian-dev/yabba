package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.block.Tier;

/**
 * @author LatvianModder
 */
public class ItemUpgradeStone extends ItemUpgrade
{
	public ItemUpgradeStone(String id)
	{
		super(id);
	}

	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		event.setAddUpgrade(false);
		return (event.getPlayer().capabilities.isCreativeMode || event.getBarrel().tier == Tier.WOOD) && event.getBarrel().setTier(Tier.STONE, event.simulate());
	}
}