package com.latmod.yabba;

import com.feed_the_beast.ftblib.lib.block.ItemBlockBase;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.latmod.yabba.block.BlockAntibarrel;
import com.latmod.yabba.block.BlockItemBarrel;
import com.latmod.yabba.block.BlockItemBarrelConnector;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.client.BarrelModelLoader;
import com.latmod.yabba.client.RenderItemBarrel;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.item.ItemHammer;
import com.latmod.yabba.item.ItemPainter;
import com.latmod.yabba.item.upgrade.ItemUpgrade;
import com.latmod.yabba.item.upgrade.ItemUpgradeBlank;
import com.latmod.yabba.item.upgrade.ItemUpgradeCreative;
import com.latmod.yabba.item.upgrade.ItemUpgradeHopper;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import com.latmod.yabba.item.upgrade.ItemUpgradeStone;
import com.latmod.yabba.item.upgrade.ItemUpgradeTier;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileItemBarrel;
import com.latmod.yabba.tile.TileItemBarrelConnector;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
@GameRegistry.ObjectHolder(Yabba.MOD_ID)
@Mod.EventBusSubscriber(modid = Yabba.MOD_ID)
public class YabbaItems
{
	public static final Block ITEM_BARREL = Blocks.AIR;
	public static final Block ITEM_BARREL_CONNECTOR = Blocks.AIR;
	public static final Block ANTIBARREL = Blocks.AIR;

	public static final Item UPGRADE_BLANK = Items.AIR;
	public static final Item UPGRADE_STONE_TIER = Items.AIR;
	public static final Item UPGRADE_IRON_TIER = Items.AIR;
	public static final Item UPGRADE_GOLD_TIER = Items.AIR;
	public static final Item UPGRADE_DIAMOND_TIER = Items.AIR;
	public static final Item UPGRADE_STAR_TIER = Items.AIR;
	public static final Item UPGRADE_CREATIVE = Items.AIR;
	public static final Item UPGRADE_OBSIDIAN_SHELL = Items.AIR;
	public static final Item UPGRADE_REDSTONE_OUT = Items.AIR;
	public static final Item UPGRADE_HOPPER = Items.AIR;
	public static final Item UPGRADE_VOID = Items.AIR;
	public static final Item UPGRADE_PICKUP = Items.AIR;

	public static final Item HAMMER = Items.AIR;
	public static final Item PAINTER = Items.AIR;

	@GameRegistry.ObjectHolder(Yabba.MOD_ID + ":item_barrel")
	public static final Item ITEM_BARREL_ITEM = Items.AIR;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				new BlockItemBarrel(),
				new BlockItemBarrelConnector(),
				new BlockAntibarrel());

		GameRegistry.registerTileEntity(TileItemBarrel.class, Yabba.MOD_ID + ":item_barrel");
		GameRegistry.registerTileEntity(TileItemBarrelConnector.class, Yabba.MOD_ID + ":item_barrel_connector");
		GameRegistry.registerTileEntity(TileAntibarrel.class, Yabba.MOD_ID + ":antibarrel");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new ItemBlockBarrel(ITEM_BARREL),
				new ItemBlockBase(ITEM_BARREL_CONNECTOR),
				new ItemBlockBase(ANTIBARREL),
				new ItemUpgradeBlank("upgrade_blank"),
				new ItemUpgradeStone("upgrade_stone_tier"),
				new ItemUpgradeTier("upgrade_iron_tier", Tier.IRON),
				new ItemUpgradeTier("upgrade_gold_tier", Tier.GOLD),
				new ItemUpgradeTier("upgrade_diamond_tier", Tier.DIAMOND),
				new ItemUpgradeTier("upgrade_star_tier", Tier.STAR),
				new ItemUpgradeCreative("upgrade_creative"),
				new ItemUpgrade("upgrade_obsidian_shell"),
				new ItemUpgradeRedstone("upgrade_redstone_out"),
				new ItemUpgradeHopper("upgrade_hopper"),
				new ItemUpgrade("upgrade_void"),
				new ItemUpgrade("upgrade_pickup"),
				new ItemHammer(),
				new ItemPainter());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event)
	{
		ModelLoaderRegistry.registerLoader(BarrelModelLoader.INSTANCE);
		ModelLoader.setCustomModelResourceLocation(ITEM_BARREL_ITEM, 0, BarrelModelLoader.MODEL_LOCATION);
		ModelLoader.setCustomStateMapper(ITEM_BARREL, BarrelModelLoader.INSTANCE);

		ClientUtils.registerModel(ITEM_BARREL_CONNECTOR);
		ClientUtils.registerModel(ANTIBARREL);
		ClientUtils.registerModel(UPGRADE_BLANK);
		ClientUtils.registerModel(UPGRADE_STONE_TIER);
		ClientUtils.registerModel(UPGRADE_IRON_TIER);
		ClientUtils.registerModel(UPGRADE_GOLD_TIER);
		ClientUtils.registerModel(UPGRADE_DIAMOND_TIER);
		ClientUtils.registerModel(UPGRADE_STAR_TIER);
		ClientUtils.registerModel(UPGRADE_CREATIVE);
		ClientUtils.registerModel(UPGRADE_OBSIDIAN_SHELL);
		ClientUtils.registerModel(UPGRADE_REDSTONE_OUT);
		ClientUtils.registerModel(UPGRADE_HOPPER);
		ClientUtils.registerModel(UPGRADE_VOID);
		ClientUtils.registerModel(UPGRADE_PICKUP);
		ClientUtils.registerModel(HAMMER);
		ClientUtils.registerModel(PAINTER);

		ClientRegistry.bindTileEntitySpecialRenderer(TileItemBarrel.class, new RenderItemBarrel());
	}
}