package com.latmod.yabba.api.events;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.latmod.yabba.api.IBarrelModifiable;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by LatvianModder on 18.01.2017.
 */
public class YabbaCreateConfigEvent extends Event
{
    private final TileEntity tile;
    private final IBarrelModifiable barrel;
    private final IConfigTree settings;

    public YabbaCreateConfigEvent(TileEntity te, IBarrelModifiable b, IConfigTree m)
    {
        tile = te;
        barrel = b;
        settings = m;
    }

    public TileEntity getTile()
    {
        return tile;
    }

    public IBarrelModifiable getBarrel()
    {
        return barrel;
    }

    public IConfigKey add(String group, String id, IConfigValue value)
    {
        ConfigKey key = new ConfigKey(id, value.copy(), group, "barrel_config");
        settings.add(key, value);
        return key;
    }
}