package com.latmod.yabba.client;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.util.BarrelModelData;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class BarrelItemMeshDefinition implements ItemMeshDefinition
{
    private final ResourceLocation ID;
    private final Map<BarrelModelData, ModelResourceLocation> cache;

    public BarrelItemMeshDefinition(ResourceLocation id)
    {
        ID = id;
        cache = new HashMap<>();
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        IBarrel barrel = stack.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
        BarrelModelData key = new BarrelModelData(barrel.getModel(), barrel.getSkin(), EnumFacing.NORTH);
        ModelResourceLocation model = cache.get(key);

        if(model == null)
        {
            model = new ModelResourceLocation(ID, key.toString());
            cache.put(key, model);
        }

        return model;
    }
}