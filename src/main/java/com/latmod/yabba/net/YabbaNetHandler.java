package com.latmod.yabba.net;

import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
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
	}
}