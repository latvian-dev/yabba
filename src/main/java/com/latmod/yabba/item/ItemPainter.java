package com.latmod.yabba.item;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.client.YabbaClient;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemPainter extends ItemYabba
{
	private static String getSkin(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getString("BarrelSkin") : YabbaCommon.DEFAULT_SKIN_ID;
	}

	public static void setSkin(ItemStack stack, String skinId)
	{
		stack.setTagInfo("BarrelSkin", new NBTTagString(skinId));
	}

	private enum CapUpgrade implements ICapabilityProvider, IUpgrade
	{
		INSTANCE;

		@Override
		public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
		{
			return capability == YabbaCommon.UPGRADE_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
		{
			return capability == YabbaCommon.UPGRADE_CAPABILITY ? (T) this : null;
		}

		@Override
		public boolean applyOn(IBarrelModifiable barrel, World worldIn, ItemStack upgradeItem, boolean simulate)
		{
			String skin = getSkin(upgradeItem);

			if (barrel.getSkin().equals(skin))
			{
				return false;
			}

			if (!simulate)
			{
				barrel.setSkin(skin);
				barrel.markBarrelDirty();
			}

			return true;
		}
	}

	public ItemPainter()
	{
		super("painter");
		setMaxStackSize(1);
		setMaxDamage(0);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return CapUpgrade.INSTANCE;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		return ItemHandlerHelper.copyStackWithSize(stack, 1);
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
	public static String getSkinTooltip(String skin)
	{
		return "Skin: " + StringUtils.translate(YabbaClient.getSkin(skin).getUnlocalizedName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag advanced)
	{
		tooltip.add(getSkinTooltip(getSkin(stack)));
	}
}