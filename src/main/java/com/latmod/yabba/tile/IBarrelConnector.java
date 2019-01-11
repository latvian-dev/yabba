package com.latmod.yabba.tile;

import com.latmod.yabba.api.BarrelContentType;

/**
 * @author LatvianModder
 */
public interface IBarrelConnector
{
	BarrelContentType getContentType();

	void updateBarrels();
}