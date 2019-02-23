package com.latmod.yabba;

import com.latmod.yabba.block.Tier;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = Yabba.MOD_ID)
@Config(modid = Yabba.MOD_ID, category = "")
public class YabbaConfig
{
	@Config.LangKey("stat.generalButton")
	public static final General general = new General();

	public static final TierCategory tier = new TierCategory();

	public static class General
	{
		@Config.Comment("false to inverse normal behaviour - sneak-click will give you a single item, normal-click will give a stack of items.")
		public boolean sneak_left_click_extracts_stack = true;

		@Config.Comment("How many slots can AntiBarrel have.")
		@Config.RangeInt(min = 1, max = 32768)
		public int antibarrel_capacity = 1024;

		@Config.Comment("How many items per-type can AntiBarrel have.")
		@Config.RangeInt(min = 1)
		public int antibarrel_items_per_type = Integer.MAX_VALUE;

		@Config.Comment("When you fill up a barrel with Star/Infinite Capacity tier (2 billion items), it becomes a creative barrel.")
		public boolean transform_star_to_creative = false;

		@Config.Comment("Item IDs that are blacklisted from insertion in barrels.")
		public String[] barrel_blacklist = {"yabba:upgrade_creative", "ftbquests:lootcrate"};

		private HashSet<Item> blacklistedItems = null;

		public boolean isItemBlacklisted(Item item)
		{
			if (blacklistedItems == null)
			{
				blacklistedItems = new HashSet<>();

				for (String s : barrel_blacklist)
				{
					Item item1 = Item.getByNameOrId(s);

					if (item1 != null && item1 != Items.AIR)
					{
						blacklistedItems.add(item1);
					}
				}
			}

			return blacklistedItems.contains(item);
		}
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

	public static boolean sync()
	{
		ConfigManager.sync(Yabba.MOD_ID, Config.Type.INSTANCE);
		tier.wood.syncWith(Tier.WOOD);
		tier.iron.syncWith(Tier.IRON);
		tier.gold.syncWith(Tier.GOLD);
		tier.diamond.syncWith(Tier.DIAMOND);
		general.blacklistedItems = null;
		return true;
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