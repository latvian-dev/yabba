package com.latmod.yabba;

import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.integration.ForestryIntegration;
import com.latmod.yabba.net.YabbaNetHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author LatvianModder
 */
@Mod(modid = Yabba.MOD_ID, name = "YABBA", useMetadata = true, acceptedMinecraftVersions = "[1.10,1.12)", dependencies = "required-after:ftbl;after:*")
public class Yabba
{
    public static final String MOD_ID = "yabba";

    @Mod.Instance(Yabba.MOD_ID)
    public static Yabba INST;

    @SidedProxy(serverSide = "com.latmod.yabba.YabbaCommon", clientSide = "com.latmod.yabba.client.YabbaClient")
    public static YabbaCommon PROXY;

    public static final Logger LOGGER = LogManager.getLogger("YABBA");

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(YabbaEventHandler.class);

        if(Loader.isModLoaded("forestry"))
        {
            MinecraftForge.EVENT_BUS.register(ForestryIntegration.class);
        }

        YabbaNetHandler.init();
        PROXY.preInit();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event)
    {
        PROXY.init();
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event)
    {
        BlockBarrel.LAST_CLICK_MAP.clear();
    }
}