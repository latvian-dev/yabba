package com.latmod.yabba;

import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class YabbaCreativeTabAllModels extends CreativeTabs
{
    public YabbaCreativeTabAllModels()
    {
        super(Yabba.MOD_ID + ".all_models");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return Item.getItemFromBlock(YabbaItems.BARREL);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(List<ItemStack> list)
    {
        for(IBarrelModel model : YabbaRegistry.ALL_MODELS)
        {
            for(IBarrelSkin skin : YabbaRegistry.ALL_SKINS)
            {
                list.add(YabbaItems.BARREL.createStack(model, skin, YabbaCommon.TIER_WOOD));
            }
        }
    }
}
