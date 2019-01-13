package com.latmod.yabba.util;

import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.client.YabbaClient;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BarrelLook
{
	public static final BarrelLook DEFAULT = new BarrelLook(EnumBarrelModel.BARREL, "");
	public static final String DEFAULT_SKIN_ID = "minecraft:planks_oak";

	public static BarrelLook get(EnumBarrelModel m, @Nullable String s)
	{
		if (s == null || !s.isEmpty() && s.equals(DEFAULT_SKIN_ID))
		{
			s = "";
		}

		return m.isDefault() && s.isEmpty() ? DEFAULT : new BarrelLook(m, s);
	}

	public final EnumBarrelModel model;
	public final String skin;

	private BarrelLook(EnumBarrelModel m, String s)
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
			return model == key.model && skin.equals(key.skin);
		}
		return false;
	}

	public String toString()
	{
		return model.getName() + ":" + (skin.isEmpty() ? DEFAULT_SKIN_ID : skin);
	}

	public boolean isDefault()
	{
		return this == DEFAULT;
	}

	public BarrelSkin getSkin()
	{
		return YabbaClient.getSkin(skin);
	}
}