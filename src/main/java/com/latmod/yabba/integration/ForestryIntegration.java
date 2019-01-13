package com.latmod.yabba.integration;

import com.feed_the_beast.ftblib.lib.OtherMods;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.api.YabbaSkinsEvent;
import com.latmod.yabba.client.SkinMap;
import forestry.api.arboriculture.EnumVanillaWoodType;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.WoodBlockKind;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = Yabba.MOD_ID, value = Side.CLIENT)
public class ForestryIntegration
{
	@SubscribeEvent
	public static void registerSkins(YabbaSkinsEvent event)
	{
		if (Loader.isModLoaded(OtherMods.FORESTRY))
		{
			registerSkins0(event);
		}
	}

	private static void registerSkins0(YabbaSkinsEvent event)
	{
		Yabba.LOGGER.info("Loading Forestry Integration");

		for (IWoodType type : TreeManager.woodAccess.getRegisteredWoodTypes())
		{
			if (!(type instanceof EnumVanillaWoodType))
			{
				try
				{
					BarrelSkin skin = new BarrelSkin(OtherMods.FORESTRY + ":planks_" + type.getName(), SkinMap.of("all=" + type.getPlankTexture()));

					try
					{
						skin.state = TreeManager.woodAccess.getBlock(type, WoodBlockKind.PLANKS, false);
					}
					catch (Exception ex1)
					{
					}

					skin.displayName = TreeManager.woodAccess.getStack(type, WoodBlockKind.PLANKS, false).getDisplayName();
					event.addSkin(skin);

					skin = new BarrelSkin(OtherMods.FORESTRY + ":log_" + type.getName(), SkinMap.of("up&down=" + type.getHeartTexture() + ",all=" + type.getBarkTexture()));

					try
					{
						skin.state = TreeManager.woodAccess.getBlock(type, WoodBlockKind.LOG, false);
					}
					catch (Exception ex1)
					{
					}

					skin.displayName = TreeManager.woodAccess.getStack(type, WoodBlockKind.LOG, false).getDisplayName();
					event.addSkin(skin);
				}
				catch (Exception ex)
				{
				}
			}
		}
	}
}