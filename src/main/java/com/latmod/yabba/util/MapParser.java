package com.latmod.yabba.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 20.12.2016.
 */
public final class MapParser
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
}