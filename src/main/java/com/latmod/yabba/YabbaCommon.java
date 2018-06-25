package com.latmod.yabba;

import com.latmod.yabba.net.YabbaNetHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * @author LatvianModder
 */
public class YabbaCommon
{
	public void preInit()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Yabba.MOD, new YabbaGuiHandler());
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