package com.latmod.yabba.net;

import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.latmod.yabba.Yabba;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class YabbaNetHandler
{
    static final NetworkWrapper NET = NetworkWrapper.newWrapper(Yabba.MOD_ID);

    public static void init()
    {
        int id = 0;
        NET.register(++id, new MessageUpdateBarrelItemCount());
        NET.register(++id, new MessageSelectModel());
        NET.register(++id, new MessageSelectSkin());
    }
}