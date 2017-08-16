package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.events.FTBLibClientRegistryEvent;
import com.latmod.yabba.YabbaConfig;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@EventHandler(Side.CLIENT)
public class YabbaClientEventHandler
{
	@SubscribeEvent
	public static void registerClient(FTBLibClientRegistryEvent event)
	{
		YabbaConfig.initClient(event.getRegistry());
	}
}