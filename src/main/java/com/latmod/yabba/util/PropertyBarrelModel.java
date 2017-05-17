package com.latmod.yabba.util;

import com.google.common.base.Optional;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModel;
import net.minecraft.block.properties.PropertyHelper;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class PropertyBarrelModel extends PropertyHelper<IBarrelModel>
{
    public static PropertyBarrelModel create(String name)
    {
        return new PropertyBarrelModel(name);
    }

    private PropertyBarrelModel(String name)
    {
        super(name, IBarrelModel.class);
    }

    @Override
    public Collection<IBarrelModel> getAllowedValues()
    {
        return YabbaRegistry.ALL_MODELS;
    }

    @Override
    public Optional<IBarrelModel> parseValue(String value)
    {
        return Optional.of(YabbaRegistry.INSTANCE.getModel(value));
    }

    @Override
    public String getName(IBarrelModel value)
    {
        return value.getName();
    }
}