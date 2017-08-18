package com.latmod.yabba.api;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.latmod.yabba.tile.TileBarrelBase;

/**
 * @author LatvianModder
 */
public class YabbaCreateConfigEvent extends YabbaEvent
{
	private final TileBarrelBase barrel;
	private final IConfigTree settings;

	public YabbaCreateConfigEvent(TileBarrelBase b, IConfigTree m)
	{
		barrel = b;
		settings = m;
	}

	public TileBarrelBase getBarrel()
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