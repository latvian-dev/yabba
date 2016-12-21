package com.latmod.yabba;

import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class YabbaCreativeTab extends CreativeTabs
{
    public YabbaCreativeTab()
    {
        super(Yabba.MOD_ID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return YabbaItems.UPGRADE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconItemDamage()
    {
        return EnumUpgrade.BLANK.metadata;
    }
}
