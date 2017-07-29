package com.latmod.yabba.api.events;

import com.latmod.yabba.api.BarrelModelCommonData;

/**
 * @author LatvianModder
 */
public class YabbaModelDataEvent extends YabbaEvent
{
	public interface YabbaModelDataRegistry
	{
		void addModelData(String id, BarrelModelCommonData data);
	}

	private final YabbaModelDataRegistry registry;

	public YabbaModelDataEvent(YabbaModelDataRegistry reg)
	{
		registry = reg;
	}

	public YabbaModelDataRegistry getRegistry()
	{
		return registry;
	}
}