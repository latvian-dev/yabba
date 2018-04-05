package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.config.ConfigBoolean;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.feed_the_beast.ftblib.lib.tile.TileBase;
import com.feed_the_beast.ftblib.lib.util.misc.DataStorage;
import com.google.gson.JsonObject;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.RemoveUpgradeEvent;
import com.latmod.yabba.api.YabbaConfigEvent;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.item.IUpgrade;
import com.latmod.yabba.item.upgrade.ItemUpgradeHopper;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.UpgradeInst;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class TileBarrelBase extends TileBase implements IBarrelBase
{
	protected Tier tier = Tier.WOOD;
	public Map<Item, UpgradeInst> upgrades = new HashMap<>();
	protected ConfigBoolean isLocked = new ConfigBoolean(false);

	public TileBarrelBase()
	{
		updateContainingBlockInfo();
	}

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (tier != Tier.WOOD)
		{
			nbt.setString("Tier", tier.getName());
		}

		if (!upgrades.isEmpty())
		{
			NBTTagCompound nbt1 = new NBTTagCompound();

			for (UpgradeInst inst : upgrades.values())
			{
				ResourceLocation id = Item.REGISTRY.getNameForObject(inst.getItem());

				if (id != null)
				{
					NBTTagCompound nbt2 = new NBTTagCompound();

					if (!inst.getData().isEmpty())
					{
						inst.getData().serializeNBT(nbt2, type);
					}

					nbt1.setTag(id.toString(), nbt2);
				}
			}

			nbt.setTag("Upgrades", nbt1);
		}

		if (isLocked.getBoolean())
		{
			nbt.setBoolean("Locked", true);
		}
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		updateContainingBlockInfo();
		tier = Tier.NAME_MAP.get(nbt.getString("Tier"));

		upgrades.clear();
		NBTTagCompound upgradeTag = nbt.getCompoundTag("Upgrades");

		for (String s : upgradeTag.getKeySet())
		{
			Item item = Item.REGISTRY.getObject(new ResourceLocation(s));

			if (item instanceof IUpgrade)
			{
				UpgradeInst inst = new UpgradeInst(item);

				if (!inst.getData().isEmpty())
				{
					inst.getData().deserializeNBT(upgradeTag.getCompoundTag(s), type);
				}

				upgrades.put(item, inst);
			}
		}

		isLocked.setBoolean(nbt.getBoolean("Locked"));
	}

	@Override
	public void markBarrelDirty(boolean majorChange)
	{
		if (majorChange)
		{
			updateContainingBlockInfo();
		}

		markDirty();
	}

	@Override
	public void onUpdatePacket(EnumSaveType type)
	{
	}

	@Override
	public void markDirty()
	{
		//barrel.markBarrelDirty();
	}

	@Override
	public boolean isLocked()
	{
		return isLocked.getBoolean();
	}

	public void setLocked(boolean b)
	{
		if (isLocked.getBoolean() != b)
		{
			isLocked.setBoolean(b);
			markBarrelDirty(true);
		}
	}

	@Override
	public Tier getTier()
	{
		return tier;
	}

	@Override
	public boolean setTier(Tier t, boolean simulate)
	{
		if (tier != t)
		{
			if (!simulate)
			{
				tier = t;
				markBarrelDirty(true);
			}

			return true;
		}

		return false;
	}

	@Override
	public BarrelLook getLook()
	{
		return BarrelLook.DEFAULT;
	}

	@Override
	public boolean setLook(BarrelLook look, boolean simulate)
	{
		return false;
	}

	@Override
	public DataStorage getUpgradeData(Item upgrade)
	{
		UpgradeInst inst = upgrades.get(upgrade);
		return inst == null ? DataStorage.EMPTY : inst.getData();
	}

	@Override
	public boolean hasUpgrade(Item upgrade)
	{
		return upgrades.containsKey(upgrade);
	}

	@Override
	public boolean removeUpgrade(Item upgrade, boolean simulate)
	{
		if (hasUpgrade(upgrade))
		{
			if (!simulate)
			{
				UpgradeInst inst = upgrades.remove(upgrade);
				inst.getUpgrade().onRemoved(new RemoveUpgradeEvent(this, inst));
				markBarrelDirty(true);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean addUpgrade(Item upgrade, boolean simulate)
	{
		if (upgrade instanceof IUpgrade && !hasUpgrade(upgrade))
		{
			if (!simulate)
			{
				upgrades.put(upgrade, new UpgradeInst(upgrade));
				markBarrelDirty(true);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean shouldDrop()
	{
		return !upgrades.isEmpty() || tier != Tier.WOOD || isLocked.getBoolean();
	}

	@Override
	public void saveConfig(ConfigGroup group, ICommandSender sender, JsonObject json)
	{
		group.fromJson(json);
		markBarrelDirty(true);
	}

	public void createConfig(YabbaConfigEvent event)
	{
		DataStorage data = getUpgradeData(YabbaItems.UPGRADE_REDSTONE_OUT);
		if (data instanceof ItemUpgradeRedstone.Data)
		{
			String group = Yabba.MOD_ID + ".redstone";
			event.getConfig().setGroupName(group, new TextComponentTranslation(YabbaItems.UPGRADE_REDSTONE_OUT.getUnlocalizedName() + ".name"));
			ItemUpgradeRedstone.Data data1 = (ItemUpgradeRedstone.Data) data;
			event.getConfig().add(group, "mode", data1.mode);
			event.getConfig().add(group, "count", data1.count);
		}

		data = getUpgradeData(YabbaItems.UPGRADE_HOPPER);
		if (data instanceof ItemUpgradeHopper.Data)
		{
			String group = Yabba.MOD_ID + ".hopper";
			event.getConfig().setGroupName(group, new TextComponentTranslation(YabbaItems.UPGRADE_HOPPER.getUnlocalizedName() + ".name"));
			ItemUpgradeHopper.Data data1 = (ItemUpgradeHopper.Data) data;
			event.getConfig().add(group, "up", data1.up);
			event.getConfig().add(group, "down", data1.down);
			event.getConfig().add(group, "collect", data1.collect);
		}
	}

	@Override
	public final void openGui(EntityPlayer player)
	{
		System.out.println("Test!");
		ConfigGroup configGroup = new ConfigGroup(getDisplayName());
		configGroup.setSupergroup("barrel_config");
		YabbaConfigEvent event = new YabbaConfigEvent(this, configGroup, player);
		event.post();
		createConfig(event);
		FTBLibAPI.editServerConfig((EntityPlayerMP) player, configGroup, this);
	}

	@Override
	public boolean isBarrelInvalid()
	{
		return isInvalid();
	}
}