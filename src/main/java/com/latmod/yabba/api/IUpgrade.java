package com.latmod.yabba.api;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public interface IUpgrade
{
    boolean applyOn(IBarrelModifiable barrel, World worldIn, ItemStack upgradeItem, boolean simulate);
}