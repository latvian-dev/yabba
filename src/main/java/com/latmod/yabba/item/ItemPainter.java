package com.latmod.yabba.item;

import com.latmod.yabba.client.YabbaClient;
import com.latmod.yabba.gui.GuiSelectSkin;
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
public class ItemPainter extends Item
{
	public static String getSkin(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getString("Skin") : "";
	}

	public static void setSkin(ItemStack stack, String skinId)
	{
		stack.setTagInfo("Skin", new NBTTagString(skinId));
	}

	public ItemPainter()
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
				openSkinGui();
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
		}

		return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(hand));
	}

	private void openSkinGui()
	{
		new GuiSelectSkin().openGui();
	}

	public static String getSkinTooltip(String skin)
	{
		return I18n.format("lang.yabba.skin", YabbaClient.getSkin(skin).toString());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(getSkinTooltip(getSkin(stack)));
	}
}