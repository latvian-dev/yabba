package com.latmod.yabba.util;

import com.google.common.base.Optional;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.block.properties.PropertyHelper;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class PropertyBarrelSkin extends PropertyHelper<IBarrelSkin>
{
    public static PropertyBarrelSkin create(String name)
    {
        return new PropertyBarrelSkin(name);
    }

    private PropertyBarrelSkin(String name)
    {
        super(name, IBarrelSkin.class);
    }

    @Override
    public Collection<IBarrelSkin> getAllowedValues()
    {
        return YabbaRegistry.ALL_SKINS;
    }

    @Override
    public Optional<IBarrelSkin> parseValue(String value)
    {
        return Optional.of(YabbaRegistry.INSTANCE.getSkin(value));
    }

    @Override
    public String getName(IBarrelSkin value)
    {
        return value.getName();
    }
}