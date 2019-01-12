package com.latmod.yabba.client;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaBlocks;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		ModelLoaderRegistry.registerLoader(BarrelModelLoader.INSTANCE);
		ModelLoader.setCustomModelResourceLocation(YabbaItems.ITEM_BARREL, 0, BarrelModelLoader.ID);
		ModelLoader.setCustomModelResourceLocation(YabbaItems.DECORATIVE_BLOCK, 0, BarrelModelLoader.ID);
		ModelLoader.setCustomStateMapper(YabbaBlocks.ITEM_BARREL, BarrelModelLoader.INSTANCE);
		ModelLoader.setCustomStateMapper(YabbaBlocks.DECORATIVE_BLOCK, BarrelModelLoader.INSTANCE);

		setItemModel(YabbaItems.ITEM_BARREL_CONNECTOR, "normal");
		setItemModel(YabbaItems.ANTIBARREL, "normal");

		setItemModel(YabbaItems.UPGRADE_BLANK, "inventory");
		setItemModel(YabbaItems.UPGRADE_IRON_TIER, "inventory");
		setItemModel(YabbaItems.UPGRADE_GOLD_TIER, "inventory");
		setItemModel(YabbaItems.UPGRADE_DIAMOND_TIER, "inventory");
		setItemModel(YabbaItems.UPGRADE_STAR_TIER, "inventory");
		setItemModel(YabbaItems.UPGRADE_CREATIVE, "inventory");
		setItemModel(YabbaItems.UPGRADE_OBSIDIAN_SHELL, "inventory");
		setItemModel(YabbaItems.UPGRADE_REDSTONE_OUT, "inventory");
		setItemModel(YabbaItems.UPGRADE_HOPPER, "inventory");
		setItemModel(YabbaItems.UPGRADE_VOID, "inventory");
		setItemModel(YabbaItems.UPGRADE_PICKUP, "inventory");

		setItemModel(YabbaItems.HAMMER, "inventory");
		setItemModel(YabbaItems.PAINTER, "inventory");
		setItemModel(YabbaItems.WRENCH, "inventory");

		ClientRegistry.bindTileEntitySpecialRenderer(TileItemBarrel.class, new RenderItemBarrel());
	}

	private static void setItemModel(Item item, String variant)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
	}
}