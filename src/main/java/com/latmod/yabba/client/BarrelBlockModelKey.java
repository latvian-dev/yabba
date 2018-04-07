package com.latmod.yabba.client;

import com.latmod.yabba.util.BarrelLook;
import net.minecraft.client.renderer.block.model.ModelRotation;

/**
 * @author LatvianModder
 */
public class BarrelBlockModelKey
{
	public static final ModelRotation[] ROTATIONS = ModelRotation.values();

	public final BarrelLook look;
	public final int rotation;
	//public final EnumFacing face;

	public BarrelBlockModelKey(BarrelLook l, int r/*, @Nullable EnumFacing f*/)
	{
		look = l;
		rotation = r;
		//face = f;
	}

	public int hashCode()
	{
		return look.hashCode() * 16 + rotation;
	}

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (o instanceof BarrelBlockModelKey)
		{
			BarrelBlockModelKey k = (BarrelBlockModelKey) o;
			return look.equals(k.look) && rotation == k.rotation/* && face == k.face*/;
		}

		return false;
	}

	public String toString()
	{
		return look + ":" + ROTATIONS[rotation].name().toLowerCase();
	}
}