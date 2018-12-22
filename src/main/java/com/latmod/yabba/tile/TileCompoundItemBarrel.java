package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.latmod.yabba.YabbaBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * @author LatvianModder
 */
public class TileCompoundItemBarrel extends TileBarrel
{
	@Override
	public void update()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeData(nbt, EnumSaveType.SAVE);
		world.setBlockState(pos, YabbaBlocks.ITEM_BARREL.getDefaultState());
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileItemBarrel)
		{
			((TileItemBarrel) tileEntity).readData(nbt, EnumSaveType.SAVE);
			tileEntity.markDirty();
		}
	}
}