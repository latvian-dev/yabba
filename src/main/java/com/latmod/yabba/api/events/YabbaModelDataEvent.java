package com.latmod.yabba.api.events;

import com.latmod.yabba.api.BarrelModelCommonData;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author LatvianModder
 */
public class YabbaModelDataEvent extends Event
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