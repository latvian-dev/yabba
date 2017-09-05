package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.events.registry.RegisterClientConfigEvent;
import com.feed_the_beast.ftbl.lib.ConfigRGB;
import com.feed_the_beast.ftbl.lib.client.DrawableItem;
import com.feed_the_beast.ftbl.lib.config.EnumTristate;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.item.YabbaItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@EventHandler(Side.CLIENT)
@Config(modid = Yabba.MOD_ID + "_client", category = "config", name = "../local/client/" + Yabba.MOD_ID)
public class YabbaClientConfig
{
	@Config.LangKey("ftbl.config.general")
	public static final General general = new General();

	public static final YabbaBarColor bar_color = new YabbaBarColor();

	public static class General
	{
		public EnumTristate always_display_data = EnumTristate.DEFAULT;
		public EnumTristate display_bar = EnumTristate.DEFAULT;
	}

	public static class YabbaBarColor
	{
		public ConfigRGB border = new ConfigRGB(0xFF3FD2FF);
		public ConfigRGB free = new ConfigRGB(0xFF5BF77F);
		public ConfigRGB filled = new ConfigRGB(0xFFFF635B);

		@Config.RangeInt(min = 1, max = 255)
		public int alpha = 85;
	}

	public static void sync()
	{
		ConfigManager.sync(Yabba.MOD_ID + "_client", Config.Type.INSTANCE);
		bar_color.border.updateColor();
		bar_color.free.updateColor();
		bar_color.filled.updateColor();
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(Yabba.MOD_ID + "_client"))
		{
			sync();
		}
	}

	@SubscribeEvent
	public static void registerClientConfig(RegisterClientConfigEvent event)
	{
		event.register(Yabba.MOD_ID + "_client", Yabba.MOD_NAME, new DrawableItem(new ItemStack(YabbaItems.ITEM_BARREL)));
	}
}