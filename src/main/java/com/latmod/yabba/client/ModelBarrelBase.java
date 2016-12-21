package com.latmod.yabba.client;

import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelVariant;
import com.latmod.yabba.util.MapParser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public abstract class ModelBarrelBase implements IModel
{
    public final EnumFacing facing;
    public final IBarrelVariant variant;
    public Collection<ResourceLocation> textures;

    public ModelBarrelBase(String v)
    {
        Map<String, String> map = MapParser.parse(MapParser.TEMP_MAP, v);
        facing = EnumFacing.byName(map.get("facing"));
        variant = YabbaRegistry.INSTANCE.getVariant(map.get("variant"));
        textures = Collections.unmodifiableCollection(variant.getTextures().getTextures());
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        return textures;
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }
}