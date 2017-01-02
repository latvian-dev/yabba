package com.latmod.yabba.api.events;

import com.latmod.yabba.api.IYabbaRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public class YabbaRegistryEvent extends Event
{
    private final IYabbaRegistry registry;

    public YabbaRegistryEvent(IYabbaRegistry reg)
    {
        registry = reg;
    }

    public IYabbaRegistry getRegistry()
    {
        return registry;
    }
}