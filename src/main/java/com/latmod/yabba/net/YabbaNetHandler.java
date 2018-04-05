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
		NET.register(1, new MessageSelectModel());
		NET.register(2, new MessageSelectSkin());
		NET.register(3, new MessageAntibarrelUpdate());
		NET.register(4, new MessageAntibarrelClickSlot());
		NET.register(5, new MessageBarrelConnector());
		NET.register(6, new MessageOpenBarrelGui());
	}
}