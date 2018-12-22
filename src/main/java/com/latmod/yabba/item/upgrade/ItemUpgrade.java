package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.UpgradeData;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemUpgrade extends Item
{
	@Override
	public UpgradeData initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new UpgradeData(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(I18n.format("item.yabba.upgrade.desc"));
	}
}