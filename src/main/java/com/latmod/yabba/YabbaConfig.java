package com.latmod.yabba;

import com.feed_the_beast.ftbl.api.IFTBLibClientRegistry;
import com.feed_the_beast.ftbl.api.IFTBLibRegistry;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyByte;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.config.PropertyList;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
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
    public static final PropertyBool ALWAYS_RENDER_BARREL_DATA = new PropertyBool(false);

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

        reg.addConfig(Yabba.MOD_ID, "tier.item.wood", TIER_ITEM_WOOD);
        reg.addConfig(Yabba.MOD_ID, "tier.item.iron", TIER_ITEM_IRON);
        reg.addConfig(Yabba.MOD_ID, "tier.item.gold", TIER_ITEM_GOLD);
        reg.addConfig(Yabba.MOD_ID, "tier.item.diamond", TIER_ITEM_DIAMOND);
        reg.addConfig(Yabba.MOD_ID, "tier.item.infinity", TIER_ITEM_INFINITY);

        reg.addConfig(Yabba.MOD_ID, "crafting.upgrade_stack_size", CRAFTING_UPGRADE_STACK_SIZE);
        reg.addConfig(Yabba.MOD_ID, "crafting.barrel_easy_recipe", CRAFTING_BARREL_EASY_RECIPE);

        reg.addConfig(Yabba.MOD_ID, "allowed_ore_prefixes", ALLOWED_ORE_PREFIXES);
    }

    public static void initClient(IFTBLibClientRegistry reg)
    {
        reg.addClientConfig(Yabba.MOD_ID, "always_render_barrel_data", ALWAYS_RENDER_BARREL_DATA);
    }
}