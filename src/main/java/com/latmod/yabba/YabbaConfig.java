package com.latmod.yabba;

import com.feed_the_beast.ftbl.api.IFTBLibClientRegistry;
import com.feed_the_beast.ftbl.api.IFTBLibRegistry;
import com.feed_the_beast.ftbl.lib.config.EnumTristate;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyColor;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.config.PropertyList;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.config.PropertyTristate;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.latmod.yabba.block.Tier;

import java.io.File;

/**
 * @author LatvianModder
 */
public class YabbaConfig
{
	public static final PropertyList ALLOWED_ORE_PREFIXES = new PropertyList(PropertyString.ID);
	public static final PropertyBool SNEAK_LEFT_CLICK_EXTRACTS_STACK = new PropertyBool(true);

	static
	{
		ALLOWED_ORE_PREFIXES.add(new PropertyString("ingot"));
		ALLOWED_ORE_PREFIXES.add(new PropertyString("block"));
		ALLOWED_ORE_PREFIXES.add(new PropertyString("nugget"));
		ALLOWED_ORE_PREFIXES.add(new PropertyString("ore"));
		ALLOWED_ORE_PREFIXES.add(new PropertyString("dust"));
		ALLOWED_ORE_PREFIXES.add(new PropertyString("gem"));
	}

	//Client
	public static final PropertyTristate ALWAYS_DISPLAY_DATA = new PropertyTristate(EnumTristate.DEFAULT);
	public static final PropertyTristate DISPLAY_BAR = new PropertyTristate(EnumTristate.DEFAULT);
	public static final PropertyColor BAR_COLOR_BORDER = new PropertyColor(0xFF3FD2FF);
	public static final PropertyColor BAR_COLOR_FREE = new PropertyColor(0xFF5BF77F);
	public static final PropertyColor BAR_COLOR_FILLED = new PropertyColor(0xFFFF635B);
	public static final PropertyInt BAR_COLOR_ALPHA = new PropertyInt(85, 1, 255);

	public static void init(IFTBLibRegistry reg)
	{
		reg.addConfigFileProvider(Yabba.MOD_ID, () -> new File(CommonUtils.folderConfig, "YABBA.json"));

		String group = Yabba.MOD_ID;
		reg.addConfig(group, "allowed_ore_prefixes", ALLOWED_ORE_PREFIXES);
		reg.addConfig(group, "sneak_left_click_extracts_stack", SNEAK_LEFT_CLICK_EXTRACTS_STACK);
		group = Yabba.MOD_ID + ".tier.item";
		reg.addConfig(group, "wood", Tier.WOOD.maxItemStacks);
		reg.addConfig(group, "iron", Tier.IRON.maxItemStacks);
		reg.addConfig(group, "gold", Tier.GOLD.maxItemStacks);
		reg.addConfig(group, "diamond", Tier.DIAMOND.maxItemStacks);
	}

	public static void initClient(IFTBLibClientRegistry reg)
	{
		String group = Yabba.MOD_ID;
		reg.addClientConfig(group, "always_display_data", ALWAYS_DISPLAY_DATA).setNameLangKey("barrel_config.yabba.always_display_data.name").setInfoLangKey("barrel_config.yabba.always_display_data.info");
		reg.addClientConfig(group, "display_bar", DISPLAY_BAR).setNameLangKey("barrel_config.yabba.display_bar.name").setInfoLangKey("barrel_config.yabba.display_bar.info");
		group = Yabba.MOD_ID + ".bar_color";
		reg.addClientConfig(group, "border", BAR_COLOR_BORDER);
		reg.addClientConfig(group, "free", BAR_COLOR_FREE);
		reg.addClientConfig(group, "filled", BAR_COLOR_FILLED);
		reg.addClientConfig(group, "alpha", BAR_COLOR_ALPHA);
	}
}