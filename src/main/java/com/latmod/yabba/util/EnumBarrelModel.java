package com.latmod.yabba.util;

import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.block.BlockAdvancedBarrelBase;
import com.latmod.yabba.client.BarrelModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author LatvianModder
 */
public enum EnumBarrelModel implements IStringSerializable
{
	BARREL("barrel", Block.FULL_BLOCK_AABB),
	CRATE("crate", Block.FULL_BLOCK_AABB),
	BLOCK_WINDOW("block_window", Block.FULL_BLOCK_AABB),
	BLOCK_BORDERS("block_borders", Block.FULL_BLOCK_AABB),
	COVER("cover", new AxisAlignedBB(0, 0.875, 0, 1, 1, 1)),
	PANEL("panel", new AxisAlignedBB(0, 0.75, 0, 1, 1, 1)),
	SLAB("slab", new AxisAlignedBB(0, 0.5, 0, 1, 1, 1)),
	BLOCK("block", Block.FULL_BLOCK_AABB);

	public static final NameMap<EnumBarrelModel> NAME_MAP = NameMap.create(BARREL, values());
	public static final Collection<ResourceLocation> ALL_MODEL_LOCATIONS;

	static
	{
		BARREL.cutoutModel = new ResourceLocation(Yabba.MOD_ID, "block/barrel/barrel_cutout");
		BLOCK_WINDOW.cutoutModel = new ResourceLocation(Yabba.MOD_ID, "block/barrel/block_window_cutout");
		BLOCK_BORDERS.cutoutModel = new ResourceLocation(Yabba.MOD_ID, "block/barrel/block_borders_cutout");

		ArrayList<ResourceLocation> list = new ArrayList<>(EnumBarrelModel.NAME_MAP.size() + 3);

		for (EnumBarrelModel model : EnumBarrelModel.NAME_MAP)
		{
			list.add(model.getBaseModel());

			if (model.getCutoutModel() != null)
			{
				list.add(model.getCutoutModel());
			}
		}

		ALL_MODEL_LOCATIONS = Collections.unmodifiableList(list);
	}

	public static EnumBarrelModel getFromNBTName(String id)
	{
		if (id.isEmpty())
		{
			return BARREL;
		}

		int i = id.indexOf(':');

		if (i != -1)
		{
			id = id.substring(i + 1);
		}

		return NAME_MAP.get(id);
	}

	private final String name;
	private final String unlocalizedName;
	private final AxisAlignedBB[] boxes;
	private final ResourceLocation baseModel;
	private ResourceLocation cutoutModel;

	@SideOnly(Side.CLIENT)
	private BarrelModel model;

	EnumBarrelModel(String n, AxisAlignedBB box)
	{
		name = n;
		unlocalizedName = "yabba.yabba_model." + name;
		boxes = MathUtils.getRotatedBoxes(box);
		baseModel = new ResourceLocation(Yabba.MOD_ID, "block/barrel/" + name);
	}

	@Override
	public String getName()
	{
		return name;
	}

	public String getNBTName()
	{
		return isDefault() ? "" : getName();
	}

	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}

	public AxisAlignedBB getAABB(IBlockState state)
	{
		return boxes[state.getValue(BlockAdvancedBarrelBase.FACING).getIndex()];
	}

	public ResourceLocation getBaseModel()
	{
		return baseModel;
	}

	@Nullable
	public ResourceLocation getCutoutModel()
	{
		return cutoutModel;
	}

	@SideOnly(Side.CLIENT)
	public BarrelModel getModel()
	{
		return model;
	}

	@SideOnly(Side.CLIENT)
	public void setModel(BarrelModel m)
	{
		model = m;
	}

	public boolean isDefault()
	{
		return this == BARREL;
	}
}