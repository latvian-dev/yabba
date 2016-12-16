package com.latmod.yabba.net;

import com.latmod.yabba.Yabba;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class YabbaNetHandler
{
    public static final SimpleNetworkWrapper NET = new SimpleNetworkWrapper(Yabba.MOD_ID);

    public static void init()
    {
        NET.registerMessage(MessageUpdateBarrel.class, MessageUpdateBarrel.class, 1, Side.CLIENT);
        NET.registerMessage(MessageRequestBarrelUpdate.class, MessageRequestBarrelUpdate.class, 2, Side.SERVER);
    }
}