package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.feed_the_beast.ftblib.lib.tile.TileBase;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author LatvianModder
 */
public class TileDecorativeBlock extends TileBase implements ITileWithBarrelLook
{
	private BarrelLook look = BarrelLook.DEFAULT;
	private AxisAlignedBB cachedAABB;

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (!look.model.isDefault())
		{
			nbt.setString("Model", look.model.getName());
		}

		if (!look.skin.isEmpty())
		{
			nbt.setString("Skin", look.skin);
		}
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		updateContainingBlockInfo();
		look = BarrelLook.get(EnumBarrelModel.getFromNBTName(nbt.getString("Model")), nbt.getString("Skin"));
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		cachedAABB = null;
	}

	@Override
	public void markDirty()
	{
		sendDirtyUpdate();
	}

	public AxisAlignedBB getAABB(IBlockState state)
	{
		if (cachedAABB == null)
		{
			cachedAABB = look.model.getAABB(state);
		}

		return cachedAABB;
	}

	@Override
	public BarrelLook getLook()
	{
		return look;
	}

	@Override
	public boolean setLook(BarrelLook l, boolean simulate)
	{
		if (!look.equals(l))
		{
			if (!simulate)
			{
				look = l;
				markDirty();
			}

			return true;
		}

		return false;
	}
}