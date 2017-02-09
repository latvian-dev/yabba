package com.latmod.yabba;

import com.feed_the_beast.ftbl.api.IFTBLibRegistry;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyByte;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.config.PropertyShort;
import com.feed_the_beast.ftbl.lib.util.LMUtils;

import java.io.File;

/**
 * Created by LatvianModder on 23.01.2017.
 */
public class YabbaConfig
{
    public static final PropertyShort BARRELTIER_BASE_MAX_STACKS = new PropertyShort(64, 1, 4096);
    public static final PropertyByte BARRELTIER_MULTIPLIER = new PropertyByte(4, 2, 64);
    public static final PropertyInt BARRELTIER_INFINITE_CAPACITY = new PropertyInt(2000000000, 1, Integer.MAX_VALUE);

    public static final PropertyByte CRAFTING_UPGRADE_STACK_SIZE = new PropertyByte(16, 0, 64);
    public static final PropertyBool CRAFTING_BARREL_EASY_RECIPE = new PropertyBool(true);

    public static void init(IFTBLibRegistry reg)
    {
        reg.addConfigFileProvider(Yabba.MOD_ID, () -> new File(LMUtils.folderConfig, "YABBA.json"));

        reg.addConfig(Yabba.MOD_ID, "barreltier.base_max_stacks", BARRELTIER_BASE_MAX_STACKS);
        reg.addConfig(Yabba.MOD_ID, "barreltier.multiplier", BARRELTIER_MULTIPLIER);
        reg.addConfig(Yabba.MOD_ID, "barreltier.infinite_capacity", BARRELTIER_INFINITE_CAPACITY);

        reg.addConfig(Yabba.MOD_ID, "crafting.upgrade_stack_size", CRAFTING_UPGRADE_STACK_SIZE);
        reg.addConfig(Yabba.MOD_ID, "crafting.barrel_easy_recipe", CRAFTING_BARREL_EASY_RECIPE);
    }
}