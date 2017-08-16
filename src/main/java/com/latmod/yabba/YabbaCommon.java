package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.latmod.yabba.util.BarrelModelCustomData;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
	private static final Map<ResourceLocation, BarrelModelCustomData> DATA_MAP = new HashMap<>();
	public static final ResourceLocation DEFAULT_MODEL_ID = new ResourceLocation(Yabba.MOD_ID, "barrel");
	public static final IBlockState DEFAULT_SKIN_ID = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);

	public void preInit()
	{
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
						DATA_MAP.put(new ResourceLocation(mod.getModId(), entry.getKey()), BarrelModelCustomData.from(entry.getValue()));
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

	public static BarrelModelCustomData getModelData(ResourceLocation id)
	{
		BarrelModelCustomData data = DATA_MAP.get(id);
		return data == null ? BarrelModelCustomData.DEFAULT : data;
	}
}