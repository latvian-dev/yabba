package com.latmod.yabba;

import com.latmod.yabba.net.YabbaNetHandler;
import com.latmod.yabba.util.BarrelModelCustomData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class YabbaCommon
{
	public static final Map<String, BarrelModelCustomData> DATA_MAP = new HashMap<>();

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

	public static BarrelModelCustomData getModelData(String id)
	{
		if (id.isEmpty())
		{
			return BarrelModelCustomData.DEFAULT;
		}

		BarrelModelCustomData data = DATA_MAP.get(id);
		return data == null ? BarrelModelCustomData.DEFAULT : data;
	}
}