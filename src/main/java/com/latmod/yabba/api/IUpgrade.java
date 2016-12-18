package com.latmod.yabba.api;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public interface IUpgrade
{
    String getUpgradeName();

    boolean applyOn(IBarrelModifiable barrel, World worldIn, ItemStack upgradeItem, boolean simulate);
}