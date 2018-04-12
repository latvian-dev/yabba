package com.latmod.yabba.util;

import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import com.latmod.yabba.client.BarrelModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public enum EnumBarrelModel implements IStringSerializable
{
	BARREL("barrel", Block.FULL_BLOCK_AABB),
	COVER("cover", new AxisAlignedBB(0, 0.875, 0, 1, 1, 1)),
	PANEL("panel", new AxisAlignedBB(0, 0.75, 0, 1, 1, 1)),
	SLAB("slab", new AxisAlignedBB(0, 0.5, 0, 1, 1, 1)),
	BLOCK("block", Block.FULL_BLOCK_AABB),
	BLOCK_WINDOW("block_window", Block.FULL_BLOCK_AABB),
	BLOCK_BORDERS("block_borders", Block.FULL_BLOCK_AABB),
	CRATE("crate", Block.FULL_BLOCK_AABB);

	public static final NameMap<EnumBarrelModel> NAME_MAP = NameMap.create(BARREL, values());

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

	@SideOnly(Side.CLIENT)
	private BarrelModel model;

	EnumBarrelModel(String n, AxisAlignedBB box)
	{
		name = n;
		unlocalizedName = "yabba.yabba_model." + name;
		boxes = MathUtils.getRotatedBoxes(box);
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
		return boxes[state.getValue(BlockHorizontal.FACING).getIndex()];
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