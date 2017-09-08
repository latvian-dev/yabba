package com.latmod.yabba.api;

import com.feed_the_beast.ftbl.lib.config.AdvancedConfigKey;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import com.latmod.yabba.tile.TileBarrelBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author LatvianModder
 */
public class YabbaCreateConfigEvent extends YabbaEvent
{
	private final TileBarrelBase barrel;
	private final ConfigTree tree;
	private final EntityPlayer player;

	public YabbaCreateConfigEvent(TileBarrelBase b, ConfigTree t, EntityPlayer p)
	{
		barrel = b;
		tree = t;
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

	public AdvancedConfigKey add(String group, String id, ConfigValue value)
	{
		AdvancedConfigKey key = new AdvancedConfigKey(id, value.copy(), group);
		tree.add(key, value);
		return key;
	}
}