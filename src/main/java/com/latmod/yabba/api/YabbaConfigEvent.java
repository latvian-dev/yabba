package com.latmod.yabba.api;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.latmod.yabba.tile.TileBarrelBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author LatvianModder
 */
public class YabbaConfigEvent extends YabbaEvent
{
	private final TileBarrelBase barrel;
	private final ConfigGroup config;
	private final EntityPlayer player;

	public YabbaConfigEvent(TileBarrelBase b, ConfigGroup s, EntityPlayer p)
	{
		barrel = b;
		config = s;
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

	public ConfigGroup getConfig()
	{
		return config;
	}
}