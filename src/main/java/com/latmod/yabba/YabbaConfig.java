package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = Yabba.MOD_ID)
@Config(modid = Yabba.MOD_ID, category = "config")
public class YabbaConfig
{
	@Config.LangKey(GuiLang.LANG_GENERAL)
	public static final General general = new General();

	public static final TierCategory tier = new TierCategory();

	public static class General
	{
		@Config.Comment({
				"Whitelist of OreDictionary prefixes that are allowed to merge inside barrel",
				"Example: Two different copper ores can go in the same barrel and will become whichever was the original in barrel"
		})
		public String[] allowed_ore_prefixes = {"ingot", "block", "nugget", "ore", "dust", "gem", "gear", "rod", "gear"};

		@Config.Comment("false to inverse normal behaviour - sneak-click will give you a single item, normal-click will give a stack of items")
		public boolean sneak_left_click_extracts_stack = true;

		@Config.Comment("How many items can Antibarrel store")
		public int antibarrel_capacity = 100000;

		@Config.Comment({
				"Recommended to be true. Only adding this as config option in case something really breaks.",
				"This config option will be removed once better mod integration is added"
		})
		public boolean autocreate_slots = false;
		public int connector_update_ticks = (int) (CommonUtils.TICKS_MINUTE * 5L);
	}

	public static class TierCategory
	{
		public final TierCategoryBase wood = new TierCategoryBase(64);
		public final TierCategoryBase iron = new TierCategoryBase(256);
		public final TierCategoryBase gold = new TierCategoryBase(1024);
		public final TierCategoryBase diamond = new TierCategoryBase(4096);

		private static class TierCategoryBase
		{
			@Config.RangeInt(min = 1, max = 1000000)
			@Config.LangKey("yabba.config.tier.max_item_stacks")
			public int max_item_stacks;

			public TierCategoryBase(int maxStacks)
			{
				max_item_stacks = maxStacks;
			}

			public void syncWith(Tier tier)
			{
				tier.maxItemStacks = max_item_stacks;
			}
		}
	}

	public static void sync()
	{
		ConfigManager.sync(Yabba.MOD_ID, Config.Type.INSTANCE);
		tier.wood.syncWith(Tier.WOOD);
		tier.iron.syncWith(Tier.IRON);
		tier.gold.syncWith(Tier.GOLD);
		tier.diamond.syncWith(Tier.DIAMOND);
		TileItemBarrel.clearCache();
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(Yabba.MOD_ID))
		{
			sync();
		}
	}
}