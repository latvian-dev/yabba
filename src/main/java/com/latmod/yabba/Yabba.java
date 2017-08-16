package com.latmod.yabba;

import com.latmod.yabba.block.BlockStorageBarrelBase;
import com.latmod.yabba.item.YabbaItems;
import com.latmod.yabba.net.YabbaNetHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author LatvianModder
 */
@Mod(modid = Yabba.MOD_ID, name = "YABBA", useMetadata = true, acceptedMinecraftVersions = "[1.12,)", dependencies = "required-after:ftbl")
public class Yabba
{
	public static final String MOD_ID = "yabba";

	@Mod.Instance(Yabba.MOD_ID)
	public static Yabba INST;

	@SidedProxy(serverSide = "com.latmod.yabba.YabbaCommon", clientSide = "com.latmod.yabba.client.YabbaClient")
	public static YabbaCommon PROXY;

	public static final Logger LOGGER = LogManager.getLogger("YABBA");

	public static final CreativeTabs TAB = new CreativeTabs(MOD_ID)
	{
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(YabbaItems.UPGRADE_BLANK);
		}
	};

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		YabbaNetHandler.init();
		PROXY.preInit();
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event)
	{
		PROXY.postInit();
	}

	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent event)
	{
		BlockStorageBarrelBase.LAST_CLICK_MAP.clear();
	}
}