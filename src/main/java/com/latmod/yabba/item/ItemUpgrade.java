package com.latmod.yabba.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 13.12.2016.
 */
public class ItemUpgrade extends Item
{
    public ItemUpgrade()
    {
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nullable Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        for(EnumUpgrade type : EnumUpgrade.VALUES)
        {
            subItems.add(new ItemStack(this, 1, type.metadata));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv)
    {
        list.add(I18n.format(EnumUpgrade.getFromMeta(stack.getMetadata()).getUnlocalizedName()));
    }
}