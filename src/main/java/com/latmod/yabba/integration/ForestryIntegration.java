package com.latmod.yabba.integration;

import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.events.YabbaRegistryEvent;
import forestry.api.arboriculture.EnumVanillaWoodType;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.WoodBlockKind;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by LatvianModder on 02.01.2017.
 */
public class ForestryIntegration
{
    @SubscribeEvent
    public void onRegistryEvent(YabbaRegistryEvent event)
    {
        IYabbaRegistry reg = event.getRegistry();

        for(IWoodType type : TreeManager.woodAccess.getRegisteredWoodTypes())
        {
            if(type instanceof EnumVanillaWoodType)
            {
                continue;
            }

            reg.addSkin(TreeManager.woodAccess.getBlock(type, WoodBlockKind.PLANKS, false), TreeManager.woodAccess.getStack(type, WoodBlockKind.PLANKS, false), "all=" + type.getPlankTexture());
            reg.addSkin(TreeManager.woodAccess.getBlock(type, WoodBlockKind.LOG, false), TreeManager.woodAccess.getStack(type, WoodBlockKind.LOG, false), "up&down=" + type.getHeartTexture() + ",all=" + type.getBarkTexture());
        }
    }
}