package com.latmod.yabba.item;

import com.feed_the_beast.ftblib.lib.block.ItemBlockBase;
import com.feed_the_beast.ftblib.lib.util.NBTUtils;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
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
public class ItemBlockDecorativeBlock extends ItemBlockBase
{
	public ItemBlockDecorativeBlock(Block block)
	{
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (NBTUtils.hasBlockData(stack))
		{
			NBTTagCompound tag = NBTUtils.getBlockData(stack);
			tooltip.add(ItemHammer.getModelTooltip(EnumBarrelModel.getFromNBTName(tag.getString("Model"))));
			tooltip.add(ItemPainter.getSkinTooltip(tag.getString("Skin")));
		}
		else
		{
			tooltip.add(ItemHammer.getModelTooltip(EnumBarrelModel.BARREL));
			tooltip.add(ItemPainter.getSkinTooltip(""));
		}
	}
}