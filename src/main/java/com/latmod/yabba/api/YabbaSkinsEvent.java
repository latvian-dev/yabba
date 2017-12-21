package com.latmod.yabba.api;

import com.feed_the_beast.ftblib.lib.util.misc.TextureSet;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class YabbaSkinsEvent extends YabbaEvent
{
	public interface Callback
	{
		void addSkin(BarrelSkin skin);
	}

	private final Callback callback;

	public YabbaSkinsEvent(Callback c)
	{
		callback = c;
	}

	public void addSkin(BarrelSkin skin)
	{
		callback.addSkin(skin);
	}

	public void addSkin(String id, String textures)
	{
		addSkin(new BarrelSkin(id, TextureSet.of(textures)));
	}

	public void addSkin(String id)
	{
		ResourceLocation id1 = new ResourceLocation(id);
		addSkin(id, "all=" + id1.getResourceDomain() + ":blocks/" + id1.getResourcePath());
	}
}