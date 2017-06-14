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
		super(m.getName(), s.getName());
	}

	public BarrelModelKey(String m, String s)
	{
		this(YabbaClient.getModel(m), YabbaClient.getSkin(s));
	}
}