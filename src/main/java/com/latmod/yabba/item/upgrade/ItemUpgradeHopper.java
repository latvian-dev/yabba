package com.latmod.yabba.item.upgrade;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
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
		public boolean down = true;
		public boolean up = true;
		public boolean collect = false;

		@Override
		public void serializeNBT(NBTTagCompound nbt, EnumSaveType type)
		{
			nbt.setBoolean("Down", down);
			nbt.setBoolean("Up", up);
			nbt.setBoolean("Collect", collect);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt, EnumSaveType type)
		{
			down = nbt.getBoolean("Down");
			up = nbt.getBoolean("Up");
			collect = nbt.getBoolean("Collect");
		}

		public void getConfig(ConfigGroup config)
		{
			config.addBool("up", () -> up, v -> up = v, true);
			config.addBool("down", () -> down, v -> down = v, true);
			config.addBool("collect", () -> collect, v -> collect = v, false);
		}
	}

	@Override
	public DataStorage createBarrelUpgradeData()
	{
		return new ItemUpgradeHopper.Data();
	}
}