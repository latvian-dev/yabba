package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.CreativeTabBase;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.item.ItemStack;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class YabbaCreativeTab extends CreativeTabBase
{
    public YabbaCreativeTab()
    {
        super(Yabba.MOD_ID);
        addIcon(new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.BLANK.metadata));
    }
}