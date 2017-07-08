package com.latmod.yabba.integration;

import com.latmod.yabba.api.events.YabbaSkinsEvent;
import forestry.api.arboriculture.EnumVanillaWoodType;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.WoodBlockKind;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = "forestry")
public class ForestryIntegration
{
	@SubscribeEvent
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