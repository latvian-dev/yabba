package com.latmod.yabba.item;

import com.latmod.yabba.util.AntibarrelData;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ItemBlockAntibarrel extends ItemBlock
{
	public ItemBlockAntibarrel(Block block)
	{
		super(block);
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		AntibarrelData data = AntibarrelData.get(stack);
		return data == null || data.items.isEmpty() ? 16 : 1;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new AntibarrelData(null);
	}

	@Override
	public NBTTagCompound getNBTShareTag(ItemStack stack)
	{
		return AntibarrelData.get(stack).serializeNBTForNet();
	}

	@Override
	public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		if (nbt != null)
		{
			AntibarrelData.get(stack).deserializeNBTFromNet(nbt);
		}
		else
		{
			AntibarrelData.get(stack).clear();
		}
	}
}