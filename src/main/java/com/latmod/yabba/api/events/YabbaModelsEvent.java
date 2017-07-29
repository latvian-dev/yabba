package com.latmod.yabba.api.events;

import com.latmod.yabba.api.IBarrelModel;

/**
 * @author LatvianModder
 */
public class YabbaModelsEvent extends YabbaEvent
{
	public interface YabbaModelRegistry
	{
		void addModel(IBarrelModel model);
	}

	private final YabbaModelRegistry registry;

	public YabbaModelsEvent(YabbaModelRegistry reg)
	{
		registry = reg;
	}

	public YabbaModelRegistry getRegistry()
	{
		return registry;
	}
}