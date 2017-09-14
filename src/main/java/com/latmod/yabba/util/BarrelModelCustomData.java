package com.latmod.yabba.util;

import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.yabba.block.BlockBarrelBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author LatvianModder
 */
public class BarrelModelCustomData
{
	public static final BarrelModelCustomData DEFAULT = new BarrelModelCustomData();

	public static final BarrelModelCustomData from(JsonElement json)
	{
		if (json.isJsonObject())
		{
			JsonObject o = json.getAsJsonObject();

			if (o.size() > 0)
			{
				BarrelModelCustomData data = new BarrelModelCustomData();

				if (o.has("bounding_box"))
				{
					JsonArray array = o.getAsJsonArray("bounding_box");
					data.setBoundingBox(new AxisAlignedBB(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble(), array.get(3).getAsDouble(), array.get(4).getAsDouble(), array.get(5).getAsDouble()));
				}

				return data;
			}
		}

		return DEFAULT;
	}

	private AxisAlignedBB[] boxes;

	public BarrelModelCustomData()
	{
		boxes = new AxisAlignedBB[6];

		for (int i = 0; i < boxes.length; i++)
		{
			boxes[i] = Block.FULL_BLOCK_AABB;
		}
	}

	public void setBoundingBox(AxisAlignedBB aabb)
	{
		boxes = MathUtils.getRotatedBoxes(aabb);
	}

	public AxisAlignedBB getAABB(IBlockState state)
	{
		return this == DEFAULT ? Block.FULL_BLOCK_AABB : boxes[BlockBarrelBase.normalizeFacing(state).getIndex()];
	}
}