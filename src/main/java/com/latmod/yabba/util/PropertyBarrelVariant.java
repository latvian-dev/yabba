package com.latmod.yabba.util;

import com.google.common.base.Optional;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelVariant;
import net.minecraft.block.properties.PropertyHelper;

import java.util.Collection;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class PropertyBarrelVariant extends PropertyHelper<IBarrelVariant>
{
    public static PropertyBarrelVariant create(String name)
    {
        return new PropertyBarrelVariant(name);
    }

    private PropertyBarrelVariant(String name)
    {
        super(name, IBarrelVariant.class);
    }

    @Override
    public Collection<IBarrelVariant> getAllowedValues()
    {
        return YabbaRegistry.ALL_BARRELS;
    }

    @Override
    public Optional<IBarrelVariant> parseValue(String value)
    {
        IBarrelVariant v = YabbaRegistry.BARRELS.get(value);
        return v == null ? Optional.absent() : Optional.of(v);
    }

    @Override
    public String getName(IBarrelVariant value)
    {
        return value.getName();
    }
}