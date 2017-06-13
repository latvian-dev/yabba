package com.latmod.yabba.client;

import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class BarrelModelKey extends ResourceLocation
{
	public static final BarrelModelKey DEFAULT = new BarrelModelKey(YabbaClient.DEFAULT_MODEL_ID, YabbaClient.DEFAULT_SKIN_ID);

	public BarrelModelKey(IBarrelModel m, IBarrelSkin s)
	{
		this(m.getName(), s.getName());
	}

	public BarrelModelKey(String m, String s)
	{
		super(m, s);
	}
}