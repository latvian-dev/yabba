package com.latmod.yabba.util;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.client.BarrelModel;
import com.latmod.yabba.client.YabbaClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BarrelLook
{
	public static final BarrelLook DEFAULT = new BarrelLook("", "");
	public static final String DEFAULT_MODEL_ID = Yabba.MOD_ID + ":barrel";
	public static final String DEFAULT_SKIN_ID = "minecraft:planks_oak";

	public static BarrelLook get(@Nullable String m, @Nullable String s)
	{
		if (m == null || !m.isEmpty() && m.equals(DEFAULT_MODEL_ID))
		{
			m = "";
		}

		if (s == null || !s.isEmpty() && s.equals(DEFAULT_SKIN_ID))
		{
			s = "";
		}

		return m.isEmpty() && s.isEmpty() ? DEFAULT : new BarrelLook(m, s);
	}

	public static BarrelLook get(BarrelModel m, BarrelSkin s)
	{
		return get(m.id, s.id);
	}

	public final String model;
	public final String skin;

	private BarrelLook(String m, String s)
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
		else if (o instanceof BarrelLook)
		{
			BarrelLook key = (BarrelLook) o;
			return skin.equals(key.skin) && model.equals(key.model);
		}
		return false;
	}

	public String toString()
	{
		return (model.isEmpty() ? DEFAULT_MODEL_ID : model) + ":" + (skin.isEmpty() ? DEFAULT_SKIN_ID : skin);
	}

	@SideOnly(Side.CLIENT)
	public BarrelModel getModel()
	{
		return YabbaClient.getModel(model);
	}

	@SideOnly(Side.CLIENT)
	public BarrelSkin getSkin()
	{
		return YabbaClient.getSkin(skin);
	}

	public BarrelModelCustomData getModelCustomData()
	{
		return YabbaCommon.getModelData(model);
	}
}