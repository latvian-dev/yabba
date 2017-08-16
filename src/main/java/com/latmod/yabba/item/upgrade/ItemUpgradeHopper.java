package com.latmod.yabba.item.upgrade;

import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.tile.EnumSaveType;
import com.feed_the_beast.ftbl.lib.util.DataStorage;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author LatvianModder
 */
public class ItemUpgradeHopper extends ItemUpgrade
{
	public static class Data extends DataStorage
	{
		public PropertyBool down = new PropertyBool(true);
		public PropertyBool up = new PropertyBool(true);
		public PropertyBool collect = new PropertyBool(false);

		@Override
		public void serializeNBT(NBTTagCompound nbt, EnumSaveType type)
		{
			nbt.setBoolean("Down", down.getBoolean());
			nbt.setBoolean("Up", up.getBoolean());
			nbt.setBoolean("Collect", collect.getBoolean());
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt, EnumSaveType type)
		{
			down.setBoolean(nbt.getBoolean("Down"));
			up.setBoolean(nbt.getBoolean("Up"));
			collect.setBoolean(nbt.getBoolean("Collect"));
		}
	}

	public ItemUpgradeHopper(String id)
	{
		super(id);
	}

	@Override
	public DataStorage createBarrelUpgradeData()
	{
		return new ItemUpgradeHopper.Data();
	}
}