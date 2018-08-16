package com.latmod.yabba.item.upgrade;

import com.feed_the_beast.ftblib.lib.config.ConfigBoolean;
import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.feed_the_beast.ftblib.lib.util.misc.DataStorage;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author LatvianModder
 */
public class ItemUpgradeHopper extends ItemUpgrade
{
	public static class Data extends DataStorage
	{
		public ConfigBoolean down = new ConfigBoolean(true);
		public ConfigBoolean up = new ConfigBoolean(true);
		public ConfigBoolean collect = new ConfigBoolean(false);

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

	@Override
	public DataStorage createBarrelUpgradeData()
	{
		return new ItemUpgradeHopper.Data();
	}
}