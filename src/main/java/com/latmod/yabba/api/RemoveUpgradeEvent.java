package com.latmod.yabba.api;

import com.latmod.yabba.tile.TileBarrelBase;
import com.latmod.yabba.util.UpgradeInst;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class RemoveUpgradeEvent
{
	private final TileBarrelBase barrel;
	private final UpgradeInst upgrade;

	public RemoveUpgradeEvent(TileBarrelBase b, UpgradeInst u)
	{
		barrel = b;
		upgrade = u;
	}

	public World getWorld()
	{
		return getBarrel().getWorld();
	}

	public TileBarrelBase getBarrel()
	{
		return barrel;
	}

	public UpgradeInst getUpgrade()
	{
		return upgrade;
	}
}