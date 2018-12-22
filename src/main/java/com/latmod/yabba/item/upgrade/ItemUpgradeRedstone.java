package com.latmod.yabba.item.upgrade;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import com.latmod.yabba.api.UpgradeData;
import com.latmod.yabba.tile.Barrel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

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

	public static class RedstoneUpgradeData extends UpgradeData
	{
		public Mode mode = Mode.GREATER_THAN_OR_EQUAL;
		public int count = 1;

		public RedstoneUpgradeData(ItemStack is)
		{
			super(is);
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();

			if (mode != Mode.GREATER_THAN_OR_EQUAL)
			{
				nbt.setString("Mode", mode.getName());
			}

			if (count != 1)
			{
				nbt.setInteger("Count", count);
			}

			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			mode = Mode.NAME_MAP.get(nbt.getString("Mode"));
			count = nbt.hasKey("Count") ? nbt.getInteger("Count") : 1;
		}

		@Override
		public boolean hasData()
		{
			return mode != Mode.GREATER_THAN_OR_EQUAL || count != 1;
		}

		@Override
		public void resetData()
		{
			mode = Mode.GREATER_THAN_OR_EQUAL;
			count = 1;
		}

		public int redstoneOutput(int amount)
		{
			return mode.matchesCount(amount, count) ? 15 : 0;
		}

		@Override
		public void getConfig(Barrel barrel, ConfigGroup config)
		{
			config.addEnum("mode", () -> mode, v -> mode = v, Mode.NAME_MAP);
			config.addInt("count", () -> count, v -> count = v, 1, 0, Integer.MAX_VALUE);
		}
	}

	@Override
	public UpgradeData initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new RedstoneUpgradeData(stack);
	}
}