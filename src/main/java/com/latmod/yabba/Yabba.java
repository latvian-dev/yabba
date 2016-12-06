package com.latmod.yabba;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by LatvianModder on 06.12.2016.
 */
@Mod(modid = Yabba.MOD_ID, name = "YABBA", useMetadata = true)
public class Yabba
{
    public static final String MOD_ID = "yabba";

    @Mod.Instance(Yabba.MOD_ID)
    public static Yabba INST;

    @SidedProxy(serverSide = "com.latmod.yabba.YabbaCommon", clientSide = "com.latmod.yabba.client.YabbaClient")
    public static YabbaCommon PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        PROXY.preInit();
    }
}