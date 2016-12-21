package com.latmod.yabba.client;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrelVariant;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class BarrelItemMeshDefinition implements ItemMeshDefinition
{
    private final ResourceLocation ID;
    private final Map<IBarrelVariant, ModelResourceLocation> cache;

    public BarrelItemMeshDefinition(ResourceLocation id)
    {
        ID = id;
        cache = new HashMap<>();
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        IBarrelVariant variant = stack.getCapability(YabbaCommon.BARREL_CAPABILITY, null).getVariant();
        ModelResourceLocation model = cache.get(variant);

        if(model == null)
        {
            model = new ModelResourceLocation(ID, "facing=north,variant=" + variant.getName());
            cache.put(variant, model);
        }

        return model;
    }
}