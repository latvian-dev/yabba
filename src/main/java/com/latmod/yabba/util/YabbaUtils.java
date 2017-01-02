package com.latmod.yabba.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class YabbaUtils
{
    public static final Map<String, String> TEMP_MAP = new HashMap<>();

    public static Map<String, String> parse(Map<String, String> map, String s)
    {
        if(map == TEMP_MAP)
        {
            map.clear();
        }

        for(String entry : s.split(","))
        {
            String[] val = entry.split("=");

            for(String key : val[0].split("&"))
            {
                map.put(key, val[1]);
            }
        }

        return map;
    }

    public static <T> T castTo(Object o)
    {
        return (T) o;
    }

    public static String getName(IBlockState state)
    {
        ResourceLocation id = state.getBlock().getRegistryName();
        return id.getResourceDomain() + '_' + id.getResourcePath().replace('.', '_') + '_' + state.getBlock().getMetaFromState(state);
    }
}