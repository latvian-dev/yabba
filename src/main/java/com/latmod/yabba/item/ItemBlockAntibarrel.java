package com.latmod.yabba.item;

import com.feed_the_beast.ftblib.lib.block.ItemBlockBase;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.util.AntibarrelData;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemBlockAntibarrel extends ItemBlockBase
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
		return AntibarrelData.get(stack).serializeNBT();
	}

	@Override
	public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		if (nbt != null)
		{
			AntibarrelData.get(stack).deserializeNBT(nbt);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		AntibarrelData data = AntibarrelData.get(stack);
		tooltip.add(I18n.format("tile.yabba.antibarrel.tooltip"));
		tooltip.add(I18n.format("tile.yabba.antibarrel.items", data.getTotalItemCount(), data.items.size(), YabbaConfig.general.antibarrel_capacity));
	}
}