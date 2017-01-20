package com.latmod.yabba.item;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IUpgrade;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
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
 * Created by LatvianModder on 21.12.2016.
 */
public class ItemHammer extends ItemYabba
{
    private static IBarrelModel getModel(ItemStack stack, boolean client)
    {
        return YabbaRegistry.INSTANCE.getModel(stack.hasTagCompound() ? stack.getTagCompound().getByte("BarrelModel") : 0, client);
    }

    public static void setModel(ItemStack stack, byte modelId)
    {
        stack.setTagInfo("BarrelModel", new NBTTagByte(modelId));
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
            IBarrelModel model = getModel(upgradeItem, worldIn.isRemote);

            if(barrel.getModel().equals(model))
            {
                return false;
            }

            if(!simulate)
            {
                barrel.setModel(model);
                barrel.markBarrelDirty(true);
            }

            return true;
        }
    }

    public ItemHammer()
    {
        super("hammer");
        setMaxStackSize(1);
        setMaxDamage(0);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if(hand == EnumHand.MAIN_HAND && playerIn.isSneaking())
        {
            if(worldIn.isRemote)
            {
                Yabba.PROXY.openModelGui();
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("Model: " + I18n.format("yabba.model." + getModel(stack, playerIn.worldObj.isRemote).getName()));
    }
}