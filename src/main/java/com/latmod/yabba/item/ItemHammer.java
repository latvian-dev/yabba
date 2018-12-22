package com.latmod.yabba.item;

import com.latmod.yabba.gui.GuiSelectModel;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemHammer extends Item
{
	public static EnumBarrelModel getModel(ItemStack stack)
	{
		return stack.hasTagCompound() ? EnumBarrelModel.getFromNBTName(stack.getTagCompound().getString("Model")) : EnumBarrelModel.BARREL;
	}

	public static void setModel(ItemStack stack, EnumBarrelModel modelId)
	{
		stack.setTagInfo("Model", new NBTTagString(modelId.getName()));
	}

	public ItemHammer()
	{
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (hand == EnumHand.MAIN_HAND && playerIn.isSneaking())
		{
			if (worldIn.isRemote)
			{
				openModelGui();
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
		}

		return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(hand));
	}

	private void openModelGui()
	{
		new GuiSelectModel().openGui();
	}

	public static String getModelTooltip(EnumBarrelModel model)
	{
		return I18n.format("lang.yabba.model", I18n.format(model.getUnlocalizedName()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(getModelTooltip(getModel(stack)));
	}
}