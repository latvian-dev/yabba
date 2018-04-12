package com.latmod.yabba.tile;

import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;

/**
 * @author LatvianModder
 */
public interface ITileWithBarrelLook
{
	BarrelLook getLook();

	default boolean setModel(EnumBarrelModel v, boolean simulate)
	{
		return setLook(BarrelLook.get(v, getLook().skin), simulate);
	}

	default boolean setSkin(String v, boolean simulate)
	{
		return setLook(BarrelLook.get(getLook().model, v), simulate);
	}

	boolean setLook(BarrelLook look, boolean simulate);
}