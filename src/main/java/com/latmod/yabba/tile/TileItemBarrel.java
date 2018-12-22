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

	@Override
	public void validate()
	{
		super.validate();

		if (world != null && !world.isRemote)
		{
			TileItemBarrelConnector.markAllDirty(pos, world.provider.getDimension());
		}
	}

	@Override
	public void invalidate()
	{
		super.invalidate();

		if (world != null && !world.isRemote)
		{
			TileItemBarrelConnector.markAllDirty(pos, world.provider.getDimension());
		}
	}
}