package com.latmod.yabba.api;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.latmod.yabba.tile.TileBarrelBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author LatvianModder
 */
public class YabbaCreateConfigEvent extends YabbaEvent
{
	private final TileBarrelBase barrel;
	private final IConfigTree settings;
	private final EntityPlayer player;

	public YabbaCreateConfigEvent(TileBarrelBase b, IConfigTree m, EntityPlayer p)
	{
		barrel = b;
		settings = m;
		player = p;
	}

	public TileBarrelBase getBarrel()
	{
		return barrel;
	}

	public EntityPlayer getPlayer()
	{
		return player;
	}

	public IConfigKey add(String group, String id, IConfigValue value)
	{
		ConfigKey key = new ConfigKey(id, value.copy(), group, "barrel_config");
		settings.add(key, value);
		return key;
	}
}