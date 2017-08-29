package com.latmod.yabba.client;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.BarrelSkin;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BarrelModelKey
{
	public static final BarrelModelKey DEFAULT = new BarrelModelKey("", "");

	public static BarrelModelKey get(@Nullable String m, @Nullable String s)
	{
		if (m == null || !m.isEmpty() && m.equals(YabbaCommon.DEFAULT_MODEL_ID))
		{
			m = "";
		}

		if (s == null || !s.isEmpty() && s.equals(YabbaCommon.DEFAULT_SKIN_ID))
		{
			s = "";
		}

		return m.isEmpty() && s.isEmpty() ? DEFAULT : new BarrelModelKey(m, s);
	}

	public static BarrelModelKey get(BarrelModel m, BarrelSkin s)
	{
		return get(m.id, s.id);
	}

	public final String model;
	public final String skin;

	private BarrelModelKey(String m, String s)
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
			return skin.equals(key.skin) && model.equals(key.model);
		}
		return false;
	}

	public String toString()
	{
		return model + ";" + skin + ": " + YabbaClient.getSkin(skin);
	}
}