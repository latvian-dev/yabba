package com.latmod.yabba.tile;

import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface IBarrelBlock
{
	@Nullable
	TileEntity getBarrelTileEntity();

	void clearCache();

	Barrel getBarrel();

	void markBarrelDirty(boolean majorChange);

	boolean isBarrelInvalid();
}