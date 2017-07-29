package com.latmod.yabba.api.events;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.latmod.yabba.api.Barrel;
import net.minecraft.tileentity.TileEntity;

/**
 * @author LatvianModder
 */
public class YabbaCreateConfigEvent extends YabbaEvent
{
	private final TileEntity tile;
	private final Barrel barrel;
	private final IConfigTree settings;

	public YabbaCreateConfigEvent(TileEntity te, Barrel b, IConfigTree m)
	{
		tile = te;
		barrel = b;
		settings = m;
	}

	public TileEntity getTile()
	{
		return tile;
	}

	public Barrel getBarrel()
	{
		return barrel;
	}

	public IConfigKey add(String group, String id, IConfigValue value)
	{
		ConfigKey key = new ConfigKey(id, value.copy(), group, "barrel_config");
		settings.add(key, value);
		return key;
	}
}