package com.latmod.yabba;

import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.latmod.yabba.net.YabbaNetHandler;
import com.latmod.yabba.util.BarrelModelCustomData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class YabbaCommon
{
	private static final Map<String, BarrelModelCustomData> DATA_MAP = new HashMap<>();
	public static final String DEFAULT_MODEL_ID = Yabba.MOD_ID + ":barrel";
	public static final String DEFAULT_SKIN_ID = "minecraft:planks_oak";

	public void preInit()
	{
		YabbaConfig.sync();
		YabbaNetHandler.init();
	}

	public void postInit()
	{
		for (ModContainer mod : Loader.instance().getModList())
		{
			try
			{
				JsonElement json = JsonUtils.fromJson(StringUtils.readString(Yabba.class.getResourceAsStream("/assets/" + mod.getModId() + "/yabba_models/_custom_data.json")));

				if (json.isJsonObject())
				{
					for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
					{
						DATA_MAP.put(new ResourceLocation(mod.getModId(), entry.getKey()).toString(), BarrelModelCustomData.from(entry.getValue()));
					}
				}
			}
			catch (Exception ex)
			{
			}
		}
	}

	public void openModelGui()
	{
	}

	public void openSkinGui()
	{
	}

	public static BarrelModelCustomData getModelData(String id)
	{
		BarrelModelCustomData data = DATA_MAP.get(id);
		return data == null ? BarrelModelCustomData.DEFAULT : data;
	}
}