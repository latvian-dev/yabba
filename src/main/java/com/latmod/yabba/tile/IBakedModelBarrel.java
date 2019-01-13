package com.latmod.yabba.tile;

import com.latmod.yabba.util.BarrelLook;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public interface IBakedModelBarrel
{
	@Nullable
	TileEntity getBarrelTileEntity();

	boolean isBarrelInvalid();

	BarrelLook getLook();

	default EnumFacing getBarrelRotation()
	{
		TileEntity entity = Objects.requireNonNull(getBarrelTileEntity());
		return entity.getWorld().getBlockState(entity.getPos()).getValue(BlockHorizontal.FACING);
	}
}