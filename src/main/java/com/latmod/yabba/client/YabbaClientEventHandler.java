package com.latmod.yabba.client;

import com.latmod.yabba.Yabba;
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
		ModelLoader.setCustomModelResourceLocation(YabbaItems.ITEM_BARREL_ITEM, 0, BarrelModelLoader.MODEL_LOCATION);
		ModelLoader.setCustomModelResourceLocation(YabbaItems.DECORATIVE_BLOCK_ITEM, 0, BarrelModelLoader.MODEL_LOCATION);
		ModelLoader.setCustomStateMapper(YabbaItems.ITEM_BARREL, BarrelModelLoader.INSTANCE);
		ModelLoader.setCustomStateMapper(YabbaItems.DECORATIVE_BLOCK, BarrelModelLoader.INSTANCE);

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YabbaItems.ITEM_BARREL_CONNECTOR), 0, new ModelResourceLocation(YabbaItems.ITEM_BARREL_CONNECTOR.getRegistryName(), "normal"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YabbaItems.ANTIBARREL), 0, new ModelResourceLocation(YabbaItems.ANTIBARREL.getRegistryName(), "normal"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YabbaItems.COMPOUND_ITEM_BARREL), 0, new ModelResourceLocation(YabbaItems.COMPOUND_ITEM_BARREL.getRegistryName(), "normal"));

		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_BLANK, 0, new ModelResourceLocation(YabbaItems.UPGRADE_BLANK.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_STONE_TIER, 0, new ModelResourceLocation(YabbaItems.UPGRADE_STONE_TIER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_IRON_TIER, 0, new ModelResourceLocation(YabbaItems.UPGRADE_IRON_TIER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_GOLD_TIER, 0, new ModelResourceLocation(YabbaItems.UPGRADE_GOLD_TIER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_DIAMOND_TIER, 0, new ModelResourceLocation(YabbaItems.UPGRADE_DIAMOND_TIER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_STAR_TIER, 0, new ModelResourceLocation(YabbaItems.UPGRADE_STAR_TIER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_CREATIVE, 0, new ModelResourceLocation(YabbaItems.UPGRADE_CREATIVE.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_OBSIDIAN_SHELL, 0, new ModelResourceLocation(YabbaItems.UPGRADE_OBSIDIAN_SHELL.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_REDSTONE_OUT, 0, new ModelResourceLocation(YabbaItems.UPGRADE_REDSTONE_OUT.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_HOPPER, 0, new ModelResourceLocation(YabbaItems.UPGRADE_HOPPER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_VOID, 0, new ModelResourceLocation(YabbaItems.UPGRADE_VOID.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.UPGRADE_PICKUP, 0, new ModelResourceLocation(YabbaItems.UPGRADE_PICKUP.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.HAMMER, 0, new ModelResourceLocation(YabbaItems.HAMMER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(YabbaItems.PAINTER, 0, new ModelResourceLocation(YabbaItems.PAINTER.getRegistryName(), "inventory"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileItemBarrel.class, new RenderItemBarrel());
	}
}