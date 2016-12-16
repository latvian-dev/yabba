package com.latmod.yabba;

import com.latmod.yabba.block.BlockAntibarrel;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.item.EnumUpgrade;
import com.latmod.yabba.item.ItemUpgrade;
import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by LatvianModder on 12.12.2016.
 */
public class YabbaItems
{
    public static final CreativeTabs TAB = new CreativeTabs(Yabba.MOD_ID)
    {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return UPGRADE;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public int getIconItemDamage()
        {
            return EnumUpgrade.BLANK.metadata;
        }
    };

    public static final ItemUpgrade UPGRADE = new ItemUpgrade();
    public static final Map<BlockPlanks.EnumType, BlockBarrel> BARRELS = new EnumMap<>(BlockPlanks.EnumType.class);
    public static final BlockAntibarrel ANTIBARREL = new BlockAntibarrel();
}