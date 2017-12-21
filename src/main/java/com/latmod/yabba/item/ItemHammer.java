package com.latmod.yabba.item;

import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.api.ApplyUpgradeEvent;
import com.latmod.yabba.client.YabbaClient;
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
	private static String getModel(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getString("Model") : "";
	}

	public static void setModel(ItemStack stack, String modelId)
	{
		stack.setTagInfo("Model", new NBTTagString(modelId));
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
	public static String getModelTooltip(String model)
	{
		return StringUtils.translate("lang.yabba.model", YabbaClient.getModel(model).toString());
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