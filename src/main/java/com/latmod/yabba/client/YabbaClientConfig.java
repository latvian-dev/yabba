package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.config.ConfigRGB;
import com.feed_the_beast.ftblib.lib.config.EnumTristate;
import com.feed_the_beast.ftblib.lib.gui.GuiLang;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.latmod.yabba.Yabba;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = Yabba.MOD_ID, value = Side.CLIENT)
@Config(modid = Yabba.MOD_ID + "_client", category = "", name = "../local/client/" + Yabba.MOD_ID)
public class YabbaClientConfig
{
	@Config.LangKey(GuiLang.LANG_GENERAL)
	public static final General general = new General();

	public static final YabbaBarColor bar_color = new YabbaBarColor();

	public static class General
	{
		public EnumTristate always_display_data = EnumTristate.DEFAULT;
		public EnumTristate display_bar = EnumTristate.DEFAULT;
	}

	public static class YabbaBarColor
	{
		public final ConfigRGB border = new ConfigRGB(0xFF3FD2FF);
		public final ConfigRGB free = new ConfigRGB(0xFF5BF77F);
		public final ConfigRGB filled = new ConfigRGB(0xFFFF635B);

		@Config.RangeInt(min = 1, max = 255)
		public int alpha = 85;

		private Color4I borderColor, freeColor, filledColor;

		public Color4I getBorderColor()
		{
			return borderColor;
		}

		public Color4I getFreeColor()
		{
			return freeColor;
		}

		public Color4I getFilledColor()
		{
			return filledColor;
		}
	}

	public static void sync()
	{
		ConfigManager.sync(Yabba.MOD_ID + "_client", Config.Type.INSTANCE);
		bar_color.borderColor = bar_color.border.createColor(bar_color.alpha);
		bar_color.freeColor = bar_color.free.createColor(bar_color.alpha);
		bar_color.filledColor = bar_color.filled.createColor(bar_color.alpha);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(Yabba.MOD_ID + "_client"))
		{
			sync();
		}
	}
}