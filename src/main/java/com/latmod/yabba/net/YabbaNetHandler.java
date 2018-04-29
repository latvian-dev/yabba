package com.latmod.yabba.net;

import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.latmod.yabba.Yabba;

/**
 * @author LatvianModder
 */
public class YabbaNetHandler
{
	static final NetworkWrapper NET = NetworkWrapper.newWrapper(Yabba.MOD_ID);

	public static void init()
	{
		NET.register(new MessageSelectModel());
		NET.register(new MessageSelectSkin());
		NET.register(new MessageAntibarrelUpdate());
		NET.register(new MessageAntibarrelClickSlot());
		NET.register(new MessageBarrelConnector());
		NET.register(new MessageOpenBarrelGui());
	}
}