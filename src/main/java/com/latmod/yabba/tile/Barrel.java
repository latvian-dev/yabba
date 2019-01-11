package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.BarrelContentType;
import com.latmod.yabba.api.UpgradeData;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.item.upgrade.ItemUpgradeTier;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author LatvianModder
 */
public class Barrel implements IConfigCallback
{
	public final IBarrelBlock block;
	private Tier tier;
	private boolean locked;
	private BarrelLook look;
	private final UpgradeData[] upgrades;
	public boolean alwaysDisplayData;
	public boolean displayBar;
	private Boolean isCreative;
	public final BarrelContent content;

	public Barrel(IBarrelBlock b, BarrelContentType c)
	{
		block = b;
		tier = null;
		locked = false;
		look = BarrelLook.DEFAULT;
		upgrades = new UpgradeData[8];
		alwaysDisplayData = false;
		displayBar = false;
		isCreative = null;
		content = c.function.apply(this);
	}

	public void writeData(NBTTagCompound nbt)
	{
		content.writeData(nbt);

		NBTTagList nbt1 = new NBTTagList();

		for (int i = 0; i < upgrades.length; i++)
		{
			if (upgrades[i] != null)
			{
				NBTTagCompound nbt2 = upgrades[i].stack.serializeNBT();
				nbt2.setByte("Slot", (byte) i);
				nbt1.appendTag(nbt2);
			}
		}

		if (!nbt1.isEmpty())
		{
			nbt.setTag("Upgrades", nbt1);
		}

		if (locked)
		{
			nbt.setBoolean("Locked", true);
		}

		if (!look.model.isDefault())
		{
			nbt.setString("Model", look.model.getNBTName());
		}

		if (!look.skin.isEmpty())
		{
			nbt.setString("Skin", look.skin);
		}

		if (alwaysDisplayData)
		{
			nbt.setBoolean("AlwaysDisplayData", true);
		}

		if (displayBar)
		{
			nbt.setBoolean("DisplayBar", true);
		}
	}

	public void readData(NBTTagCompound nbt)
	{
		locked = nbt.getBoolean("Locked");
		content.readData(nbt);

		Arrays.fill(upgrades, null);
		NBTBase upgradeTag = nbt.getTag("Upgrades");

		if (upgradeTag instanceof NBTTagList)
		{
			NBTTagList l = (NBTTagList) upgradeTag;

			for (int i = 0; i < l.tagCount(); i++)
			{
				NBTTagCompound nbt1 = l.getCompoundTagAt(i);
				int s = nbt1.getByte("Slot");

				if (s >= 0 && s < upgrades.length)
				{
					ItemStack stack = new ItemStack(nbt1);
					UpgradeData data = stack.getCapability(UpgradeData.CAPABILITY, null);

					if (data != null)
					{
						upgrades[s] = data;
					}
				}
			}
		}
		else if (upgradeTag instanceof NBTTagCompound)
		{
			for (String s : ((NBTTagCompound) upgradeTag).getKeySet())
			{
				Item item = Item.REGISTRY.getObject(new ResourceLocation(s));

				if (item != null)
				{
					int slot = findFreeUpgradeSlot();

					if (slot == -1)
					{
						break;
					}

					ItemStack stack = new ItemStack(item);
					UpgradeData data = stack.getCapability(UpgradeData.CAPABILITY, null);

					if (data != null)
					{
						data.deserializeNBT(((NBTTagCompound) upgradeTag).getCompoundTag(s));
					}

					upgrades[slot] = data;
				}
			}
		}

		if (nbt.hasKey("Tier"))
		{
			String stier = nbt.getString("Tier");
			tier = Tier.NAME_MAP.get(stier);

			if (stier.equals("creative"))
			{
				tier = Tier.WOOD;

				int i = findFreeUpgradeSlot();

				if (i != -1)
				{
					upgrades[i] = new ItemStack(YabbaItems.UPGRADE_CREATIVE).getCapability(UpgradeData.CAPABILITY, null);
					locked = false;
					content.onCreativeChange();
				}
			}

			if (tier.tier >= Tier.IRON.tier)
			{
				int i = findFreeUpgradeSlot();

				if (i != -1)
				{
					upgrades[i] = new ItemStack(YabbaItems.UPGRADE_IRON_TIER).getCapability(UpgradeData.CAPABILITY, null);
				}
			}

			if (tier.tier >= Tier.GOLD.tier)
			{
				int i = findFreeUpgradeSlot();

				if (i != -1)
				{
					upgrades[i] = new ItemStack(YabbaItems.UPGRADE_GOLD_TIER).getCapability(UpgradeData.CAPABILITY, null);
				}
			}

			if (tier.tier >= Tier.DIAMOND.tier)
			{
				int i = findFreeUpgradeSlot();

				if (i != -1)
				{
					upgrades[i] = new ItemStack(YabbaItems.UPGRADE_DIAMOND_TIER).getCapability(UpgradeData.CAPABILITY, null);
				}
			}

			if (tier.tier >= Tier.STAR.tier)
			{
				int i = findFreeUpgradeSlot();

				if (i != -1)
				{
					upgrades[i] = new ItemStack(YabbaItems.UPGRADE_STAR_TIER).getCapability(UpgradeData.CAPABILITY, null);
				}
			}

			tier = null;
		}

		look = BarrelLook.get(EnumBarrelModel.getFromNBTName(nbt.getString("Model")), nbt.getString("Skin"));
		alwaysDisplayData = nbt.getBoolean("AlwaysDisplayData");
		displayBar = nbt.getBoolean("DisplayBar");
	}

	public int findFreeUpgradeSlot()
	{
		for (int i = 0; i < upgrades.length; i++)
		{
			if (upgrades[i] == null)
			{
				return i;
			}
		}

		return -1;
	}

	public Tier getTier()
	{
		if (tier == null)
		{
			tier = Tier.WOOD;

			for (UpgradeData upgrade : upgrades)
			{
				if (upgrade != null && upgrade.stack.getItem() instanceof ItemUpgradeTier)
				{
					Tier t = ((ItemUpgradeTier) upgrade.stack.getItem()).tier;

					if (t.tier > tier.tier)
					{
						tier = t;
					}
				}
			}
		}

		return tier;
	}

	public boolean isLocked()
	{
		return locked;
	}

	public void setLocked(boolean v)
	{
		if (locked != v)
		{
			locked = v;
			block.markBarrelDirty(true);
		}
	}

	public BarrelLook getLook()
	{
		return look;
	}

	public void setLook(BarrelLook l)
	{
		if (!look.equals(l))
		{
			look = l;
			block.markBarrelDirty(true);
		}
	}

	public int getUpgradeCount()
	{
		return upgrades.length;
	}

	@Nullable
	public UpgradeData getUpgrade(int slot)
	{
		return slot >= 0 && slot < upgrades.length ? upgrades[slot] : null;
	}

	@Nullable
	public UpgradeData getUpgradeData(Item upgradeItem)
	{
		for (int i = 0; i < upgrades.length; i++)
		{
			if (upgrades[i] != null && upgrades[i].stack.getItem() == upgradeItem)
			{
				return upgrades[i];
			}
		}

		return null;
	}

	public boolean hasUpgrade(Item upgradeItem)
	{
		return getUpgradeData(upgradeItem) != null;
	}

	public void setUpgrade(int slot, UpgradeData upgrade)
	{
		if (slot >= 0 && slot < upgrades.length)
		{
			upgrades[slot] = upgrade;
			block.markBarrelDirty(true);
		}
	}

	public boolean isCreative()
	{
		if (isCreative == null)
		{
			isCreative = false;

			for (UpgradeData upgrade : upgrades)
			{
				if (upgrade != null)
				{
					if (upgrade.stack.getItem() == YabbaItems.UPGRADE_CREATIVE)
					{
						isCreative = true;
						return true;
					}
				}
			}
		}

		return isCreative;
	}

	public void openGui(EntityPlayerMP player)
	{
		ConfigGroup config = ConfigGroup.newGroup("barrel_config");
		config.setDisplayName(new TextComponentString("YABBA"));

		if (!isCreative())
		{
			config.addBool("locked", () -> locked, v -> locked = v, false);
		}

		config.addBool("always_display_data", () -> alwaysDisplayData, v -> alwaysDisplayData = v, false).setDisplayName(new TextComponentTranslation("yabba_client.general.always_display_data"));
		config.addBool("display_bar", () -> displayBar, v -> displayBar = v, false).setDisplayName(new TextComponentTranslation("yabba_client.general.display_bar"));

		for (int i = 0; i < upgrades.length; i++)
		{
			if (upgrades[i] != null)
			{
				ConfigGroup c = config.getGroup(Integer.toString(i));
				c.setDisplayName(new TextComponentTranslation(upgrades[i].stack.getTranslationKey() + ".name"));
				upgrades[i].getConfig(this, c);
			}
		}

		FTBLibAPI.editServerConfig(player, config, this);
	}

	@Override
	public void onConfigSaved(ConfigGroup group, ICommandSender sender)
	{
		block.markBarrelDirty(true);
	}

	public void clearCache()
	{
		tier = null;
		isCreative = null;
		content.clearCache();
	}
}