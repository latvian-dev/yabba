package com.latmod.yabba.client;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaBlocks;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Yabba.MOD_ID)
public class YabbaClientEventHandler
{
	private static void addModel(Item item, String variant)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		ModelLoaderRegistry.registerLoader(BarrelModelLoader.INSTANCE);
		ModelLoader.setCustomModelResourceLocation(YabbaItems.ITEM_BARREL, 0, BarrelModelLoader.ID);
		ModelLoader.setCustomModelResourceLocation(YabbaItems.DECORATIVE_BLOCK, 0, BarrelModelLoader.ID);
		ModelLoader.setCustomStateMapper(YabbaBlocks.ITEM_BARREL, BarrelModelLoader.INSTANCE);
		ModelLoader.setCustomStateMapper(YabbaBlocks.DECORATIVE_BLOCK, BarrelModelLoader.INSTANCE);

		addModel(YabbaItems.ITEM_BARREL_CONNECTOR, "normal");
		addModel(YabbaItems.ANTIBARREL, "normal");

		addModel(YabbaItems.UPGRADE_BLANK, "inventory");
		addModel(YabbaItems.UPGRADE_IRON_TIER, "inventory");
		addModel(YabbaItems.UPGRADE_GOLD_TIER, "inventory");
		addModel(YabbaItems.UPGRADE_DIAMOND_TIER, "inventory");
		addModel(YabbaItems.UPGRADE_STAR_TIER, "inventory");
		addModel(YabbaItems.UPGRADE_CREATIVE, "inventory");
		addModel(YabbaItems.UPGRADE_OBSIDIAN_SHELL, "inventory");
		addModel(YabbaItems.UPGRADE_REDSTONE_OUT, "inventory");
		addModel(YabbaItems.UPGRADE_HOPPER, "inventory");
		addModel(YabbaItems.UPGRADE_VOID, "inventory");
		addModel(YabbaItems.UPGRADE_PICKUP, "inventory");
		addModel(YabbaItems.UPGRADE_SMELTING, "inventory");

		addModel(YabbaItems.HAMMER, "inventory");
		addModel(YabbaItems.PAINTER, "inventory");
		addModel(YabbaItems.WRENCH, "inventory");

		ClientRegistry.bindTileEntitySpecialRenderer(TileItemBarrel.class, new RenderItemBarrel());
	}

	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event)
	{
		event.getBlockColors().registerBlockColorHandler(BarrelModelLoader.INSTANCE,
				YabbaBlocks.ITEM_BARREL,
				YabbaBlocks.DECORATIVE_BLOCK);
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event)
	{
		event.getItemColors().registerItemColorHandler(BarrelModelLoader.INSTANCE,
				YabbaItems.ITEM_BARREL,
				YabbaItems.DECORATIVE_BLOCK);
	}
}