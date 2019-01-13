package com.latmod.yabba.tile;

import com.latmod.yabba.util.BarrelLook;

/**
 * @author LatvianModder
 */
public interface IBarrelBlock extends IBakedModelBarrel
{
	void clearCache();

	Barrel getBarrel();

	void markBarrelDirty(boolean majorChange);

	@Override
	default BarrelLook getLook()
	{
		return getBarrel().getLook();
	}
}