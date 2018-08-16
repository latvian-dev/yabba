package com.latmod.yabba.item.upgrade;

import com.feed_the_beast.ftblib.lib.config.ConfigEnum;
import com.feed_the_beast.ftblib.lib.config.ConfigInt;
import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.feed_the_beast.ftblib.lib.util.misc.DataStorage;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public class ItemUpgradeRedstone extends ItemUpgrade
{
	public enum Mode implements IStringSerializable
	{
		EQUAL("=="),
		NOT("!="),
		GREATER_THAN(">"),
		GREATER_THAN_OR_EQUAL(">="),
		LESS_THAN("<"),
		LESS_THAN_OR_EQUAL("<=");

		public static final NameMap<Mode> NAME_MAP = NameMap.create(GREATER_THAN_OR_EQUAL, values());

		private final String name;

		Mode(String s)
		{
			name = s;
		}

		@Override
		public String getName()
		{
			return name;
		}

		public boolean matchesCount(int items1, int items2)
		{
			switch (this)
			{
				case EQUAL:
					return items1 == items2;
				case NOT:
					return items1 != items2;
				case GREATER_THAN:
					return items1 > items2;
				case GREATER_THAN_OR_EQUAL:
					return items1 >= items2;
				case LESS_THAN:
					return items1 < items2;
				case LESS_THAN_OR_EQUAL:
					return items1 <= items2;
				default:
					return false;
			}
		}
	}

	public static class Data extends DataStorage
	{
		public ConfigEnum<Mode> mode = new ConfigEnum<>(Mode.NAME_MAP);
		public ConfigInt count = new ConfigInt(1);

		@Override
		public void serializeNBT(NBTTagCompound nbt, EnumSaveType type)
		{
			Mode.NAME_MAP.writeToNBT(nbt, "Mode", type, mode.getValue());
			nbt.setInteger("Count", count.getInt());
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt, EnumSaveType type)
		{
			mode.setValue(Mode.NAME_MAP.readFromNBT(nbt, "Mode", type));
			count.setInt(nbt.getInteger("Count"));
		}

		public int redstoneOutput(EnumFacing facing, int amount)
		{
			return mode.getValue().matchesCount(amount, count.getInt()) ? 15 : 0;
		}
	}

	@Override
	public DataStorage createBarrelUpgradeData()
	{
		return new Data();
	}
}