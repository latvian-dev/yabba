package com.latmod.yabba.api;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.latmod.yabba.client.SkinMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class BarrelSkin
{
	public final String id;
	public SkinMap skinMap;
	public Icon icon = Icon.EMPTY;
	public IBlockState state = BlockUtils.AIR_STATE;
	public String displayName = "";
	public BlockRenderLayer layer = BlockRenderLayer.SOLID;
	public int color = 0xFFFFFFFF;

	public BarrelSkin(String s, SkinMap m)
	{
		id = new ResourceLocation(s).toString();
		skinMap = m;
	}

	public int hashCode()
	{
		return id.hashCode();
	}

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (o instanceof BarrelSkin)
		{
			return id.equals(((BarrelSkin) o).id);
		}

		return false;
	}

	public String toString()
	{
		return displayName.isEmpty() ? id : displayName;
	}
}