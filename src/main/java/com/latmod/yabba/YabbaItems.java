package com.latmod.yabba;

import com.feed_the_beast.ftblib.lib.block.ItemBlockBase;
import com.latmod.yabba.block.BlockAntibarrel;
import com.latmod.yabba.block.BlockCompoundItemBarrel;
import com.latmod.yabba.block.BlockDecorativeBlock;
import com.latmod.yabba.block.BlockItemBarrel;
import com.latmod.yabba.block.BlockItemBarrelConnector;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.client.BarrelModelLoader;
import com.latmod.yabba.client.RenderAntibarrel;
import com.latmod.yabba.client.RenderItemBarrel;
import com.latmod.yabba.item.ItemBlockAntibarrel;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.item.ItemBlockDecorativeBlock;
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
import com.latmod.yabba.tile.TileCompoundItemBarrel;
import com.latmod.yabba.tile.TileDecorativeBlock;
import com.latmod.yabba.tile.TileItemBarrel;
import com.latmod.yabba.tile.TileItemBarrelConnector;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
	public static final Block COMPOUND_ITEM_BARREL = Blocks.AIR;
	public static final Block DECORATIVE_BLOCK = Blocks.AIR;

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

	@GameRegistry.ObjectHolder(Yabba.MOD_ID + ":decorative_block")
	public static final Item DECORATIVE_BLOCK_ITEM = Items.AIR;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				new BlockItemBarrel("item_barrel"),
				new BlockItemBarrelConnector("item_barrel_connector"),
				new BlockAntibarrel("antibarrel"),
				new BlockCompoundItemBarrel("compound_item_barrel"),
				new BlockDecorativeBlock("decorative_block")
		);

		GameRegistry.registerTileEntity(TileItemBarrel.class, new ResourceLocation(Yabba.MOD_ID, "item_barrel"));
		GameRegistry.registerTileEntity(TileItemBarrelConnector.class, new ResourceLocation(Yabba.MOD_ID, "item_barrel_connector"));
		GameRegistry.registerTileEntity(TileAntibarrel.class, new ResourceLocation(Yabba.MOD_ID, "antibarrel"));
		GameRegistry.registerTileEntity(TileCompoundItemBarrel.class, new ResourceLocation(Yabba.MOD_ID, "compound_item_barrel"));
		GameRegistry.registerTileEntity(TileDecorativeBlock.class, new ResourceLocation(Yabba.MOD_ID, "decorative_block"));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new ItemBlockBarrel(ITEM_BARREL),
				new ItemBlockBase(ITEM_BARREL_CONNECTOR),
				new ItemBlockAntibarrel(ANTIBARREL),
				new ItemBlockBarrel(COMPOUND_ITEM_BARREL),
				new ItemBlockDecorativeBlock(DECORATIVE_BLOCK),
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
				new ItemPainter()
		);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event)
	{
		ModelLoaderRegistry.registerLoader(BarrelModelLoader.INSTANCE);
		ModelLoader.setCustomModelResourceLocation(ITEM_BARREL_ITEM, 0, BarrelModelLoader.MODEL_LOCATION);
		ModelLoader.setCustomModelResourceLocation(DECORATIVE_BLOCK_ITEM, 0, BarrelModelLoader.MODEL_LOCATION);
		ModelLoader.setCustomStateMapper(ITEM_BARREL, BarrelModelLoader.INSTANCE);
		ModelLoader.setCustomStateMapper(DECORATIVE_BLOCK, BarrelModelLoader.INSTANCE);

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ITEM_BARREL_CONNECTOR), 0, new ModelResourceLocation(ITEM_BARREL_CONNECTOR.getRegistryName(), "normal"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ANTIBARREL), 0, new ModelResourceLocation(ANTIBARREL.getRegistryName(), "normal"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(COMPOUND_ITEM_BARREL), 0, new ModelResourceLocation(COMPOUND_ITEM_BARREL.getRegistryName(), "normal"));

		ModelLoader.setCustomModelResourceLocation(UPGRADE_BLANK, 0, new ModelResourceLocation(UPGRADE_BLANK.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_STONE_TIER, 0, new ModelResourceLocation(UPGRADE_STONE_TIER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_IRON_TIER, 0, new ModelResourceLocation(UPGRADE_IRON_TIER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_GOLD_TIER, 0, new ModelResourceLocation(UPGRADE_GOLD_TIER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_DIAMOND_TIER, 0, new ModelResourceLocation(UPGRADE_DIAMOND_TIER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_STAR_TIER, 0, new ModelResourceLocation(UPGRADE_STAR_TIER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_CREATIVE, 0, new ModelResourceLocation(UPGRADE_CREATIVE.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_OBSIDIAN_SHELL, 0, new ModelResourceLocation(UPGRADE_OBSIDIAN_SHELL.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_REDSTONE_OUT, 0, new ModelResourceLocation(UPGRADE_REDSTONE_OUT.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_HOPPER, 0, new ModelResourceLocation(UPGRADE_HOPPER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_VOID, 0, new ModelResourceLocation(UPGRADE_VOID.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(UPGRADE_PICKUP, 0, new ModelResourceLocation(UPGRADE_PICKUP.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(HAMMER, 0, new ModelResourceLocation(HAMMER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(PAINTER, 0, new ModelResourceLocation(PAINTER.getRegistryName(), "inventory"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileItemBarrel.class, new RenderItemBarrel());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAntibarrel.class, new RenderAntibarrel());
	}
}