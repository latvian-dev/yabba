package com.latmod.yabba;

import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.integration.ForestryIntegration;
import com.latmod.yabba.net.YabbaNetHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by LatvianModder on 06.12.2016.
 */
@Mod(modid = Yabba.MOD_ID, name = "YABBA", useMetadata = true, dependencies = "required-after:ftbl;after:forestry")
public class Yabba
{
    public static final String MOD_ID = "yabba";

    @Mod.Instance(Yabba.MOD_ID)
    public static Yabba INST;

    @SidedProxy(serverSide = "com.latmod.yabba.YabbaCommon", clientSide = "com.latmod.yabba.client.YabbaClient")
    public static YabbaCommon PROXY;

    public static final Logger LOGGER = LogManager.getLogger("YABBA");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new YabbaEventHandler());

        if(Loader.isModLoaded("forestry"))
        {
            MinecraftForge.EVENT_BUS.register(new ForestryIntegration());
        }

        YabbaNetHandler.init();
        PROXY.preInit();
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event)
    {
        BlockBarrel.LAST_CLICK_MAP.clear();
    }
}