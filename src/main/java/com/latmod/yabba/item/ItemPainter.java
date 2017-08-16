package com.latmod.yabba.item;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.events.ApplyUpgradeEvent;
import com.latmod.yabba.client.YabbaClient;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
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
public class ItemPainter extends ItemYabba implements IUpgrade
{
	private static IBlockState getSkin(ItemStack stack)
	{
		return stack.hasTagCompound() ? Block.getStateById(stack.getTagCompound().getInteger("Skin")) : YabbaCommon.DEFAULT_SKIN_ID;
	}

	public static void setSkin(ItemStack stack, IBlockState skinId)
	{
		stack.setTagInfo("Skin", new NBTTagInt(Block.getStateId(skinId)));
	}

	public ItemPainter()
	{
		super("painter");
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
				Yabba.PROXY.openSkinGui();
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
		}

		return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(hand));
	}

	@SideOnly(Side.CLIENT)
	public static String getSkinTooltip(IBlockState skin)
	{
		return StringUtils.translate("lang.yabba.skin", StringUtils.translate(YabbaClient.getSkin(skin).toString()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(getSkinTooltip(getSkin(stack)));
	}

	@Override
	public boolean applyOn(ApplyUpgradeEvent event)
	{
		if (!event.simulate() && event.getBarrel().setSkin(getSkin(event.getUpgrade().getStack())))
		{
			event.setConsumeItem(false);
		}

		return false;
	}
}