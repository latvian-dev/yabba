package com.latmod.yabba.util;

import com.latmod.yabba.api.Barrel;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.api.Tier;
import com.latmod.yabba.item.YabbaItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public enum EnumUpgrade implements IUpgrade, IStringSerializable
{
	BLANK(0, "blank"),
	IRON_UPGRADE(1, "iron_upgrade"),
	GOLD_UPGRADE(2, "gold_upgrade"),
	DIAMOND_UPGRADE(3, "diamond_upgrade"),
	NETHER_STAR_UPGRADE(4, "nether_star_upgrade"),
	CREATIVE(9, "creative"),
	OBSIDIAN_SHELL(11, "obsidian_shell"),
	REDSTONE_OUT(12, "redstone_out"),
	HOPPER(13, "hopper"),
	VOID(15, "void");

	/**
	 * @author LatvianModder
	 */
	public static final EnumUpgrade[] VALUES = values();

	private final String name;
	public final String uname;
	public final int metadata;

	EnumUpgrade(int meta, String n)
	{
		name = n;
		uname = "item.yabba.upgrade." + name;
		metadata = meta;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public ItemStack item()
	{
		return new ItemStack(YabbaItems.UPGRADE, 1, metadata);
	}

	public static EnumUpgrade getFromMeta(int meta)
	{
		for (EnumUpgrade t : VALUES)
		{
			if (t.metadata == meta)
			{
				return t;
			}
		}

		return BLANK;
	}

	@Override
	public boolean applyOn(Barrel barrel, World worldIn, ItemStack upgradeItem, boolean simulate)
	{
		switch (this)
		{
			case IRON_UPGRADE:
			{
				if (barrel.getTier().equals(Tier.WOOD))
				{
					if (!simulate)
					{
						barrel.setTier(Tier.IRON);
					}
					return true;
				}
				break;
			}
			case GOLD_UPGRADE:
			{
				if (barrel.getTier().equals(Tier.IRON))
				{
					if (!simulate)
					{
						barrel.setTier(Tier.GOLD);
					}
					return true;
				}
				break;
			}
			case DIAMOND_UPGRADE:
			{
				if (barrel.getTier().equals(Tier.GOLD))
				{
					if (!simulate)
					{
						barrel.setTier(Tier.DIAMOND);
					}
					return true;
				}
				break;
			}
			case NETHER_STAR_UPGRADE:
			{
				if (!barrel.getFlag(Barrel.FLAG_INFINITE_CAPACITY))
				{
					if (!simulate)
					{
						barrel.setFlag(Barrel.FLAG_INFINITE_CAPACITY, true);
						barrel.addUpgradeName(NETHER_STAR_UPGRADE.uname);
					}
					return true;
				}
				break;
			}
			case CREATIVE:
			{
				if (!barrel.getFlag(Barrel.FLAG_IS_CREATIVE) && !barrel.getStoredItemType().isEmpty())
				{
					if (!simulate)
					{
						barrel.setItemCount(1000000000);
						barrel.setFlag(Barrel.FLAG_IS_CREATIVE, true);
						barrel.setFlag(Barrel.FLAG_INFINITE_CAPACITY, true);
						barrel.setFlag(Barrel.FLAG_LOCKED, false);
						barrel.addUpgradeName(CREATIVE.uname);
					}
					return true;
				}
				break;
			}
			case OBSIDIAN_SHELL:
			{
				if (!barrel.getFlag(Barrel.FLAG_OBSIDIAN_SHELL))
				{
					if (!simulate)
					{
						barrel.setFlag(Barrel.FLAG_OBSIDIAN_SHELL, true);
						barrel.addUpgradeName(OBSIDIAN_SHELL.uname);
					}
					return true;
				}
				break;
			}
			case REDSTONE_OUT:
			{
				if (!barrel.getFlag(Barrel.FLAG_REDSTONE_OUT))
				{
					if (!simulate)
					{
						barrel.setFlag(Barrel.FLAG_REDSTONE_OUT, true);
						barrel.getUpgradeNBT().setByte("RedstoneMode", (byte) 0);
						barrel.getUpgradeNBT().setInteger("RedstoneItemCount", 0);
						barrel.addUpgradeName(REDSTONE_OUT.uname);
					}
					return true;
				}
				break;
			}
			case HOPPER:
			{
				if (!barrel.getFlag(Barrel.FLAG_HOPPER))
				{
					if (!simulate)
					{
						barrel.setFlag(Barrel.FLAG_HOPPER, true);
						barrel.getUpgradeNBT().setBoolean("HopperUp", true);
						barrel.getUpgradeNBT().setBoolean("HopperDown", true);
						barrel.getUpgradeNBT().setBoolean("HopperCollect", false);
						barrel.addUpgradeName(HOPPER.uname);
					}
					return true;
				}
				break;
			}
			case VOID:
			{
				if (!barrel.getFlag(Barrel.FLAG_VOID_ITEMS))
				{
					if (!simulate)
					{
						barrel.setFlag(Barrel.FLAG_VOID_ITEMS, true);
						barrel.addUpgradeName(VOID.uname);
					}
					return true;
				}
				break;
			}
		}

		return false;
	}
}