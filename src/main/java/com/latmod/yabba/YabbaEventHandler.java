package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.block.ItemBlockBase;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.latmod.yabba.api.BarrelModelCommonData;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.events.YabbaCreateConfigEvent;
import com.latmod.yabba.api.events.YabbaModelDataEvent;
import com.latmod.yabba.block.BlockAntibarrel;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.block.RecipeBarrelUpgrade;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.item.ItemHammer;
import com.latmod.yabba.item.ItemPainter;
import com.latmod.yabba.item.ItemUpgrade;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumRedstoneCompMode;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = Yabba.MOD_ID)
public class YabbaEventHandler
{
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				new BlockBarrel(),
				new BlockAntibarrel());

		GameRegistry.registerTileEntity(TileBarrel.class, Yabba.MOD_ID + ".barrel");
		GameRegistry.registerTileEntity(TileAntibarrel.class, Yabba.MOD_ID + ".antibarrel");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new ItemBlockBarrel(YabbaCommon.BARREL),
				new ItemBlockBase(YabbaCommon.ANTIBARREL),
				new ItemUpgrade(),
				new ItemHammer(),
				new ItemPainter());
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		event.getRegistry().register(new RecipeBarrelUpgrade().setRegistryName(Yabba.MOD_ID + ":upgrade_barrel"));
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
		IBarrelModifiable barrel = event.getBarrel();

		String group = Yabba.MOD_ID;
		event.add(group, "disable_ore_items", PropertyBool.create(false, () -> barrel.getFlag(IBarrel.FLAG_DISABLE_ORE_DICTIONARY), v -> barrel.setFlag(IBarrel.FLAG_DISABLE_ORE_DICTIONARY, v)));
		event.add(group, "always_display_data", PropertyBool.create(false, () -> barrel.getFlag(IBarrel.FLAG_ALWAYS_DISPLAY_DATA), v -> barrel.setFlag(IBarrel.FLAG_ALWAYS_DISPLAY_DATA, v)));
		event.add(group, "display_bar", PropertyBool.create(false, () -> barrel.getFlag(IBarrel.FLAG_DISPLAY_BAR), v -> barrel.setFlag(IBarrel.FLAG_DISPLAY_BAR, v)));

		if (barrel.getFlag(IBarrel.FLAG_REDSTONE_OUT))
		{
			group = Yabba.MOD_ID + ".redstone";
			event.add(group, "mode", PropertyEnum.create(EnumRedstoneCompMode.NAME_MAP, EnumRedstoneCompMode.EQUAL, () -> EnumRedstoneCompMode.getMode(barrel.getUpgradeNBT().getByte("RedstoneMode")), v -> barrel.setUpgradeData("RedstoneMode", new NBTTagByte((byte) v.ordinal()))));
			event.add(group, "item_count", PropertyInt.create(0, 0, Integer.MAX_VALUE, () -> barrel.getUpgradeNBT().getInteger("RedstoneItemCount"), v -> barrel.setUpgradeData("RedstoneItemCount", new NBTTagInt(v))));
		}

		if (barrel.getFlag(IBarrel.FLAG_HOPPER))
		{
			group = Yabba.MOD_ID + ".hopper";
			event.add(group, "up", PropertyBool.create(true, () -> barrel.getUpgradeNBT().getBoolean("HopperUp"), v -> barrel.setUpgradeData("HopperUp", new NBTTagByte((byte) (v ? 1 : 0)))));
			event.add(group, "down", PropertyBool.create(true, () -> barrel.getUpgradeNBT().getBoolean("HopperDown"), v -> barrel.setUpgradeData("HopperDown", new NBTTagByte((byte) (v ? 1 : 0)))));
			event.add(group, "collect", PropertyBool.create(false, () -> barrel.getUpgradeNBT().getBoolean("HopperCollect"), v -> barrel.setUpgradeData("HopperCollect", new NBTTagByte((byte) (v ? 1 : 0)))));
		}
	}
}