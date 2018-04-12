package com.latmod.yabba.item;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
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
public class ItemHammer extends ItemYabba implements IUpgrade
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
		super("hammer");
		setMaxStackSize(1);
		setMaxDamage(0);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (hand == EnumHand.MAIN_HAND && playerIn.isSneaking())
		{
			if (worldIn.isRemote)
			{
				Yabba.PROXY.openModelGui();
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
		}

		return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(hand));
	}

	@SideOnly(Side.CLIENT)
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

	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		event.setConsumeItem(false);
		event.setAddUpgrade(false);
		return event.getBarrel().setModel(getModel(event.getHeldItem()), event.simulate());
	}
}