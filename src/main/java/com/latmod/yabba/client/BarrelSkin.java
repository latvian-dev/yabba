package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.IconSet;
import com.feed_the_beast.ftbl.lib.client.SpriteSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class BarrelSkin
{
	public final IBlockState state;
	public final ItemStack stack;
	public IconSet iconSet;
	public SpriteSet spriteSet;

	public BarrelSkin(IBlockState _state, ItemStack _stack, IconSet _iconSet)
	{
		state = _state;
		stack = _stack;
		iconSet = _iconSet;
	}

	public int hashCode()
	{
		return state.hashCode();
	}

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (o instanceof BarrelSkin)
		{
			return state == ((BarrelSkin) o).state;
		}
		return false;
	}

	public String toString()
	{
		return stack.getDisplayName();
	}
}