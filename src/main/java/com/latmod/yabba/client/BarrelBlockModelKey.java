package com.latmod.yabba.client;

import com.latmod.yabba.util.BarrelLook;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.EnumFacing;

/**
 * @author LatvianModder
 */
public class BarrelBlockModelKey
{
	public static final ModelRotation[] ROTATIONS = ModelRotation.values();

	public final BarrelLook look;
	public final int rotation;

	public BarrelBlockModelKey(BarrelLook l, int r)
	{
		look = l;
		rotation = r;
	}

	public int hashCode()
	{
		return look.hashCode() * 4 + rotation;
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
			return look.equals(k.look) && rotation == k.rotation;
		}

		return false;
	}

	public String toString()
	{
		return look + ":" + EnumFacing.getHorizontal(rotation).getName();
	}
}