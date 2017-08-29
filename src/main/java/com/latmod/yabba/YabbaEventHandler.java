package com.latmod.yabba;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.events.ConfigLoadedEvent;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@EventHandler
public class YabbaEventHandler
{
	@SubscribeEvent
	public static void configLoaded(ConfigLoadedEvent event)
	{
		TileItemBarrel.clearCache();
	}
}