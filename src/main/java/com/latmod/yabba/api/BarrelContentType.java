package com.latmod.yabba.api;

import com.latmod.yabba.tile.Barrel;
import com.latmod.yabba.tile.BarrelContent;
import com.latmod.yabba.tile.DecorativeBarrel;
import com.latmod.yabba.tile.EnergyBarrel;
import com.latmod.yabba.tile.FluidBarrel;
import com.latmod.yabba.tile.ItemBarrel;

import java.util.function.Function;

/**
 * @author LatvianModder
 */
public enum BarrelContentType
{
	DECORATIVE(DecorativeBarrel::new),
	ITEM(ItemBarrel::new),
	FLUID(FluidBarrel::new),
	ENERGY(EnergyBarrel::new);

	public final Function<Barrel, BarrelContent> function;

	BarrelContentType(Function<Barrel, BarrelContent> f)
	{
		function = f;
	}
}