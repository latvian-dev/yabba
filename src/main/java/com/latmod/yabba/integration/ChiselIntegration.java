package com.latmod.yabba.integration;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.api.YabbaSkinsEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.api.carving.ICarvingVariation;

import java.util.Objects;

/**
 * @author LatvianModder
 */
@EventHandler(requiredMods = FTBLibFinals.CHISEL)
public class ChiselIntegration
{
	@SubscribeEvent
	public static void registerSkins(YabbaSkinsEvent event)
	{
		Yabba.LOGGER.info("Loading Chisel Integration");

		ICarvingRegistry registry = CarvingUtils.getChiselRegistry();

		if (registry == null)
		{
			return;
		}

		for (String s : registry.getSortedGroupNames())
		{
			for (ICarvingVariation variation : Objects.requireNonNull(registry.getGroup(s)).getVariations())
			{
				IBlockState state = variation.getBlockState();

				if (state != null && state.getBlock().getRegistryName().getResourceDomain().equals(FTBLibFinals.CHISEL))
				{

				}
			}
		}

		/*
		 * BarrelSkin skin = new BarrelSkin(FTBLibFinals.FORESTRY + ":planks_" + type.getName(), TextureSet.of("all=" + type.getPlankTexture()));

		 try
		 {
		 skin.state = TreeManager.woodAccess.getBlock(type, WoodBlockKind.PLANKS, false);
		 }
		 catch (Exception ex1)
		 {
		 }

		 skin.displayName = TreeManager.woodAccess.getStack(type, WoodBlockKind.PLANKS, false).getDisplayName();
		 event.addSkin(skin);*/

	}
}