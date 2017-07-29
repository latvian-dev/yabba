package com.latmod.yabba;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.events.ConfigLoadedEvent;
import com.feed_the_beast.ftbl.api.events.FTBLibRegistryEvent;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.latmod.yabba.api.Barrel;
import com.latmod.yabba.api.BarrelModelCommonData;
import com.latmod.yabba.api.events.YabbaCreateConfigEvent;
import com.latmod.yabba.api.events.YabbaModelDataEvent;
import com.latmod.yabba.api.events.YabbaSkinsEvent;
import com.latmod.yabba.util.EnumRedstoneCompMode;
import forestry.api.arboriculture.EnumVanillaWoodType;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.WoodBlockKind;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@EventHandler
public class YabbaEventHandler
{
	@SubscribeEvent
	public static void registerCommon(FTBLibRegistryEvent event)
	{
		YabbaConfig.init(event.getRegistry());
	}

	@SubscribeEvent
	public static void configLoaded(ConfigLoadedEvent event)
	{
		Barrel.clearCache();
	}

	@SubscribeEvent
	public static void addModelData(YabbaModelDataEvent event)
	{
		YabbaModelDataEvent.YabbaModelDataRegistry reg = event.getRegistry();

		reg.addModelData("cover", new BarrelModelCommonData.Panel(0.125F));
		reg.addModelData("panel", new BarrelModelCommonData.Panel(0.25F));
		reg.addModelData("slab", new BarrelModelCommonData.Panel(0.5F));
	}

	@SubscribeEvent
	public static void createConfig(YabbaCreateConfigEvent event)
	{
		Barrel barrel = event.getBarrel();

		String group = Yabba.MOD_ID;
		event.add(group, "disable_ore_items", PropertyBool.create(false, () -> barrel.getFlag(Barrel.FLAG_DISABLE_ORE_DICTIONARY), v -> barrel.setFlag(Barrel.FLAG_DISABLE_ORE_DICTIONARY, v)));
		event.add(group, "always_display_data", PropertyBool.create(false, () -> barrel.getFlag(Barrel.FLAG_ALWAYS_DISPLAY_DATA), v -> barrel.setFlag(Barrel.FLAG_ALWAYS_DISPLAY_DATA, v)));
		event.add(group, "display_bar", PropertyBool.create(false, () -> barrel.getFlag(Barrel.FLAG_DISPLAY_BAR), v -> barrel.setFlag(Barrel.FLAG_DISPLAY_BAR, v)));

		if (barrel.getFlag(Barrel.FLAG_REDSTONE_OUT))
		{
			group = Yabba.MOD_ID + ".redstone";
			event.add(group, "mode", PropertyEnum.create(EnumRedstoneCompMode.NAME_MAP, () -> EnumRedstoneCompMode.NAME_MAP.get(barrel.getUpgradeNBT().getByte("RedstoneMode")), v -> barrel.getUpgradeNBT().setByte("RedstoneMode", (byte) v.ordinal())));
			event.add(group, "item_count", PropertyInt.create(0, 0, Integer.MAX_VALUE, () -> barrel.getUpgradeNBT().getInteger("RedstoneItemCount"), v -> barrel.getUpgradeNBT().setInteger("RedstoneItemCount", v)));
		}

		if (barrel.getFlag(Barrel.FLAG_HOPPER))
		{
			group = Yabba.MOD_ID + ".hopper";
			event.add(group, "up", PropertyBool.create(true, () -> barrel.getUpgradeNBT().getBoolean("HopperUp"), v -> barrel.getUpgradeNBT().setBoolean("HopperUp", v)));
			event.add(group, "down", PropertyBool.create(true, () -> barrel.getUpgradeNBT().getBoolean("HopperDown"), v -> barrel.getUpgradeNBT().setBoolean("HopperDown", v)));
			event.add(group, "collect", PropertyBool.create(false, () -> barrel.getUpgradeNBT().getBoolean("HopperCollect"), v -> barrel.getUpgradeNBT().setBoolean("HopperCollect", v)));
		}
	}

	@SubscribeEvent
	@Optional.Method(modid = "forestry")
	public static void onRegistryEvent(YabbaSkinsEvent event)
	{
		for (IWoodType type : TreeManager.woodAccess.getRegisteredWoodTypes())
		{
			if (!(type instanceof EnumVanillaWoodType))
			{
				event.getRegistry().addSkin(TreeManager.woodAccess.getBlock(type, WoodBlockKind.PLANKS, false), "all=" + type.getPlankTexture());
				event.getRegistry().addSkin(TreeManager.woodAccess.getBlock(type, WoodBlockKind.LOG, false), "up&down=" + type.getHeartTexture() + ",all=" + type.getBarkTexture());
			}
		}
	}
}