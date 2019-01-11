package com.latmod.yabba.tile;

import com.latmod.yabba.api.BarrelContentType;

/**
 * @author LatvianModder
 */
public class TileItemBarrel extends TileBarrel
{
	@Override
	public BarrelContentType getContentType()
	{
		return BarrelContentType.ITEM;
	}
}