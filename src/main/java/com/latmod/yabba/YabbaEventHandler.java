package com.latmod.yabba;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.events.ConfigLoadedEvent;
import com.feed_the_beast.ftbl.lib.util.DataStorage;
import com.latmod.yabba.api.YabbaCreateConfigEvent;
import com.latmod.yabba.item.YabbaItems;
import com.latmod.yabba.item.upgrade.ItemUpgradeHopper;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import com.latmod.yabba.tile.TileBarrelBase;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@EventHandler
public class YabbaEventHandler
{
	@SubscribeEvent
	public static void configLoaded(ConfigLoadedEvent event)
	{
		TileItemBarrel.clearCache();
	}

	@SubscribeEvent
	public static void createConfig(YabbaCreateConfigEvent event)
	{
		TileBarrelBase barrel = event.getBarrel();

		String group = Yabba.MOD_ID;

		if (barrel instanceof TileItemBarrel)
		{
			event.add(group, "disable_ore_items", ((TileItemBarrel) barrel).disableOreItems);
		}

		event.add(group, "always_display_data", barrel.alwaysDisplayData);
		event.add(group, "display_bar", barrel.displayBar);

		DataStorage data = barrel.getUpgradeData(YabbaItems.UPGRADE_REDSTONE_OUT);
		if (data instanceof ItemUpgradeRedstone.Data)
		{
			group = Yabba.MOD_ID + ".redstone";
			ItemUpgradeRedstone.Data data1 = (ItemUpgradeRedstone.Data) data;
			event.add(group, "mode", data1.mode);
			event.add(group, "count", data1.count);
		}

		data = barrel.getUpgradeData(YabbaItems.UPGRADE_HOPPER);
		if (data instanceof ItemUpgradeHopper.Data)
		{
			group = Yabba.MOD_ID + ".hopper";
			ItemUpgradeHopper.Data data1 = (ItemUpgradeHopper.Data) data;
			event.add(group, "up", data1.up);
			event.add(group, "down", data1.down);
			event.add(group, "collect", data1.collect);
		}
	}
}