package com.latmod.yabba.api.events;

import com.latmod.yabba.api.IYabbaRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author LatvianModder
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