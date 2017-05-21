package com.latmod.yabba.api.events;

import com.latmod.yabba.api.IBarrelModel;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author LatvianModder
 */
public class YabbaModelsEvent extends Event
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