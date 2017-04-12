package com.latmod.yabba;

import com.feed_the_beast.ftbl.api.IFTBLibClientRegistry;
import com.feed_the_beast.ftbl.api.IFTBLibRegistry;
import com.feed_the_beast.ftbl.lib.config.EnumTristate;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyByte;
import com.feed_the_beast.ftbl.lib.config.PropertyColor;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.config.PropertyList;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.config.PropertyTristate;
import com.feed_the_beast.ftbl.lib.util.LMUtils;

import java.io.File;

/**
 * Created by LatvianModder on 23.01.2017.
 */
public class YabbaConfig
{
    public static final PropertyInt TIER_ITEM_WOOD = new PropertyInt(64, 1, Integer.MAX_VALUE);
    public static final PropertyInt TIER_ITEM_IRON = new PropertyInt(256, 1, Integer.MAX_VALUE);
    public static final PropertyInt TIER_ITEM_GOLD = new PropertyInt(1024, 1, Integer.MAX_VALUE);
    public static final PropertyInt TIER_ITEM_DIAMOND = new PropertyInt(4096, 1, Integer.MAX_VALUE);
    public static final PropertyInt TIER_ITEM_INFINITY = new PropertyInt(2000000000, 1, Integer.MAX_VALUE);

    public static final PropertyByte CRAFTING_UPGRADE_STACK_SIZE = new PropertyByte(16, 0, 64);
    public static final PropertyBool CRAFTING_BARREL_EASY_RECIPE = new PropertyBool(true);

    public static final PropertyList ALLOWED_ORE_PREFIXES = new PropertyList(PropertyString.ID);

    //Client
    public static final PropertyTristate ALWAYS_DISPLAY_DATA = new PropertyTristate(EnumTristate.DEFAULT);
    public static final PropertyTristate DISPLAY_BAR = new PropertyTristate(EnumTristate.DEFAULT);
    public static final PropertyColor BAR_COLOR_BORDER = new PropertyColor(0xFF3FD2FF);
    public static final PropertyColor BAR_COLOR_FREE = new PropertyColor(0xFF5BF77F);
    public static final PropertyColor BAR_COLOR_FILLED = new PropertyColor(0xFFFF635B);
    public static final PropertyInt BAR_COLOR_ALPHA = new PropertyInt(85, 1, 255);

    static
    {
        ALLOWED_ORE_PREFIXES.add(new PropertyString("ingot"));
        ALLOWED_ORE_PREFIXES.add(new PropertyString("block"));
        ALLOWED_ORE_PREFIXES.add(new PropertyString("nugget"));
        ALLOWED_ORE_PREFIXES.add(new PropertyString("ore"));
        ALLOWED_ORE_PREFIXES.add(new PropertyString("dust"));
        ALLOWED_ORE_PREFIXES.add(new PropertyString("gem"));
    }

    public static void init(IFTBLibRegistry reg)
    {
        reg.addConfigFileProvider(Yabba.MOD_ID, () -> new File(LMUtils.folderConfig, "YABBA.json"));

        String group = Yabba.MOD_ID;
        reg.addConfig(group, "allowed_ore_prefixes", ALLOWED_ORE_PREFIXES);
        group = Yabba.MOD_ID + ".tier.item";
        reg.addConfig(group, "wood", TIER_ITEM_WOOD);
        reg.addConfig(group, "iron", TIER_ITEM_IRON);
        reg.addConfig(group, "gold", TIER_ITEM_GOLD);
        reg.addConfig(group, "diamond", TIER_ITEM_DIAMOND);
        reg.addConfig(group, "infinity", TIER_ITEM_INFINITY);
        group = Yabba.MOD_ID + ".crafting";
        reg.addConfig(group, "upgrade_stack_size", CRAFTING_UPGRADE_STACK_SIZE);
        reg.addConfig(group, "barrel_easy_recipe", CRAFTING_BARREL_EASY_RECIPE);
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