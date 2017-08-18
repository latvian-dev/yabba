package com.latmod.yabba.integration;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.lib.TextureSet;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.api.YabbaSkinsEvent;
import forestry.api.arboriculture.EnumVanillaWoodType;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.WoodBlockKind;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@EventHandler(requiredMods = ForestryIntegration.MOD_ID)
public class ForestryIntegration
{
	public static final String MOD_ID = "forestry";

	@SubscribeEvent
	public static void registerSkins(YabbaSkinsEvent event)
	{
		for (IWoodType type : TreeManager.woodAccess.getRegisteredWoodTypes())
		{
			if (!(type instanceof EnumVanillaWoodType))
			{
				BarrelSkin skin = new BarrelSkin(MOD_ID + ":planks_" + type.getName(), TextureSet.of("all=" + type.getPlankTexture()));
				skin.state = TreeManager.woodAccess.getBlock(type, WoodBlockKind.PLANKS, false);
				event.addSkin(skin);
				skin = new BarrelSkin(MOD_ID + ":log_" + type.getName(), TextureSet.of("up&down=" + type.getHeartTexture() + ",all=" + type.getBarkTexture()));
				skin.state = TreeManager.woodAccess.getBlock(type, WoodBlockKind.LOG, false);
				event.addSkin(skin);
			}
		}
	}
}