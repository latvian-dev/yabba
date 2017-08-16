package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.block.BlockStorageBarrelBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IExtendedBlockState;

/**
 * @author LatvianModder
 */
public class BarrelModelKey
{
	public static final BarrelModelKey DEFAULT = new BarrelModelKey(YabbaCommon.DEFAULT_MODEL_ID, YabbaCommon.DEFAULT_SKIN_ID);

	public static BarrelModelKey get0(ResourceLocation m, IBlockState s)
	{
		if ((s == YabbaCommon.DEFAULT_SKIN_ID || s == Blocks.AIR.getDefaultState()) && m.equals(YabbaCommon.DEFAULT_MODEL_ID))
		{
			return DEFAULT;
		}

		return new BarrelModelKey(m, s);
	}

	public static BarrelModelKey get(BarrelModel m, BarrelSkin s)
	{
		return get0(m.id, s.state);
	}

	public static BarrelModelKey get(ResourceLocation m, IBlockState s)
	{
		return get0(YabbaClient.getModel(m).id, YabbaClient.getSkin(s).state);
	}

	public static BarrelModelKey get(IExtendedBlockState state)
	{
		return get(state.getValue(BlockStorageBarrelBase.MODEL), state.getValue(BlockStorageBarrelBase.SKIN));
	}

	public static BarrelModelKey get(String m, String s)
	{
		ResourceLocation model = m.isEmpty() ? YabbaCommon.DEFAULT_MODEL_ID : new ResourceLocation(m);
		IBlockState skin = s.isEmpty() ? YabbaCommon.DEFAULT_SKIN_ID : CommonUtils.getStateFromName(s);
		return get0(model, skin);
	}

	public final ResourceLocation model;
	public final IBlockState skin;

	private BarrelModelKey(ResourceLocation m, IBlockState s)
	{
		model = m;
		skin = s;
	}

	public int hashCode()
	{
		return skin.hashCode() ^ model.hashCode();
	}

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (o instanceof BarrelModelKey)
		{
			BarrelModelKey key = (BarrelModelKey) o;
			return skin == key.skin && model.equals(key.model);
		}
		return false;
	}

	public String toString()
	{
		return model + ";" + CommonUtils.getNameFromState(skin) + ": " + YabbaClient.getSkin(skin);
	}
}