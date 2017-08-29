package com.latmod.yabba.item;

import com.feed_the_beast.ftbl.lib.block.ItemBlockBase;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.block.BlockAntibarrel;
import com.latmod.yabba.block.BlockItemBarrel;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.client.BarrelModelLoader;
import com.latmod.yabba.client.RenderItemBarrel;
import com.latmod.yabba.item.upgrade.ItemUpgrade;
import com.latmod.yabba.item.upgrade.ItemUpgradeBlank;
import com.latmod.yabba.item.upgrade.ItemUpgradeCreative;
import com.latmod.yabba.item.upgrade.ItemUpgradeHopper;
import com.latmod.yabba.item.upgrade.ItemUpgradeInfiniteCapacity;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import com.latmod.yabba.item.upgrade.ItemUpgradeTier;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileItemBarrel;
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
	public static final Block ANTIBARREL = Blocks.AIR;

	public static final Item UPGRADE_BLANK = Items.AIR;
	public static final Item UPGRADE_IRON_TIER = Items.AIR;
	public static final Item UPGRADE_GOLD_TIER = Items.AIR;
	public static final Item UPGRADE_DIAMOND_TIER = Items.AIR;
	public static final Item UPGRADE_INFINITE_CAPACITY = Items.AIR;
	public static final Item UPGRADE_CREATIVE = Items.AIR;
	public static final Item UPGRADE_OBSIDIAN_SHELL = Items.AIR;
	public static final Item UPGRADE_REDSTONE_OUT = Items.AIR;
	public static final Item UPGRADE_HOPPER = Items.AIR;
	public static final Item UPGRADE_VOID = Items.AIR;

	public static final Item HAMMER = Items.AIR;
	public static final Item PAINTER = Items.AIR;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				new BlockItemBarrel(),
				new BlockAntibarrel());

		GameRegistry.registerTileEntity(TileItemBarrel.class, Yabba.MOD_ID + ":item_barrel");
		GameRegistry.registerTileEntity(TileAntibarrel.class, Yabba.MOD_ID + ":antibarrel");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new ItemBlockBarrel(ITEM_BARREL),
				new ItemBlockBase(ANTIBARREL),
				new ItemUpgradeBlank("upgrade_blank"),
				new ItemUpgradeTier("upgrade_iron_tier", Tier.IRON),
				new ItemUpgradeTier("upgrade_gold_tier", Tier.GOLD),
				new ItemUpgradeTier("upgrade_diamond_tier", Tier.DIAMOND),
				new ItemUpgradeInfiniteCapacity("upgrade_infinite_capacity"),
				new ItemUpgradeCreative("upgrade_creative"),
				new ItemUpgrade("upgrade_obsidian_shell"),
				new ItemUpgradeRedstone("upgrade_redstone_out"),
				new ItemUpgradeHopper("upgrade_hopper"),
				new ItemUpgrade("upgrade_void"),
				new ItemHammer(),
				new ItemPainter());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event)
	{
		ModelLoaderRegistry.registerLoader(BarrelModelLoader.INSTANCE);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ITEM_BARREL), 0, BarrelModelLoader.MODEL_LOCATION);
		ModelLoader.setCustomStateMapper(ITEM_BARREL, BarrelModelLoader.INSTANCE);

		ClientUtils.registerModel(ANTIBARREL);
		ClientUtils.registerModel(UPGRADE_BLANK);
		ClientUtils.registerModel(UPGRADE_IRON_TIER);
		ClientUtils.registerModel(UPGRADE_GOLD_TIER);
		ClientUtils.registerModel(UPGRADE_DIAMOND_TIER);
		ClientUtils.registerModel(UPGRADE_INFINITE_CAPACITY);
		ClientUtils.registerModel(UPGRADE_CREATIVE);
		ClientUtils.registerModel(UPGRADE_OBSIDIAN_SHELL);
		ClientUtils.registerModel(UPGRADE_REDSTONE_OUT);
		ClientUtils.registerModel(UPGRADE_HOPPER);
		ClientUtils.registerModel(UPGRADE_VOID);
		ClientUtils.registerModel(HAMMER);
		ClientUtils.registerModel(PAINTER);

		ClientRegistry.bindTileEntitySpecialRenderer(TileItemBarrel.class, new RenderItemBarrel());
	}
}