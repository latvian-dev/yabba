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
        int id = 0;
        NET.registerMessage(MessageSyncData.class, MessageSyncData.class, ++id, Side.CLIENT);
        NET.registerMessage(MessageUpdateBarrelItemCount.class, MessageUpdateBarrelItemCount.class, ++id, Side.CLIENT);
        NET.registerMessage(MessageUpdateBarrelFull.class, MessageUpdateBarrelFull.class, ++id, Side.CLIENT);
        NET.registerMessage(MessageRequestBarrelUpdate.class, MessageRequestBarrelUpdate.class, ++id, Side.SERVER);
        NET.registerMessage(MessageSelectModel.class, MessageSelectModel.class, ++id, Side.SERVER);
        NET.registerMessage(MessageSelectSkin.class, MessageSelectSkin.class, ++id, Side.SERVER);
    }
}