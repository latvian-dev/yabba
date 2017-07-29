package com.latmod.yabba.item;

import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.latmod.yabba.api.Barrel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;

/**
 * @author LatvianModder
 */
public class BarrelItemData extends Barrel
{
	public final ItemStack itemStack;

	public BarrelItemData(ItemStack is)
	{
		itemStack = is;
	}

	@Override
	public void markBarrelDirty(boolean majorChange)
	{
		itemStack.setTagInfo("Update", new NBTTagInt(MathUtils.RAND.nextInt()));
	}
}