package com.latmod.yabba;

import com.latmod.yabba.net.YabbaNetHandler;

/**
 * @author LatvianModder
 */
public class YabbaCommon
{
	public void preInit()
	{
		YabbaConfig.sync();
		YabbaNetHandler.init();
	}

	public void postInit()
	{
	}

	public void openModelGui()
	{
	}

	public void openSkinGui()
	{
	}
}