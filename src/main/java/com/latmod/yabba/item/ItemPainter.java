package com.latmod.yabba.item;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.util.YabbaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
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
public class ItemPainter extends Item
{
    public enum CapUpgrade implements ICapabilityProvider, IUpgrade
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
        public String getUpgradeName()
        {
            return "painter";
        }

        @Override
        public boolean applyOn(IBarrelModifiable barrel, World worldIn, ItemStack upgradeItem, boolean simulate)
        {
            IBarrelModel model = YabbaRegistry.INSTANCE.getModel(upgradeItem.hasTagCompound() ? upgradeItem.getTagCompound().getString("BarrelModel") : "");
            IBarrelSkin skin = YabbaRegistry.INSTANCE.getSkin(upgradeItem.hasTagCompound() ? upgradeItem.getTagCompound().getString("BarrelSkin") : "");

            if(barrel.getModel().equals(model) && barrel.getSkin().equals(skin))
            {
                return false;
            }

            if(!simulate)
            {
                barrel.setModel(model);
                barrel.setSkin(skin);
                barrel.markBarrelDirty(true);
            }

            return true;
        }
    }

    public ItemPainter()
    {
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
        if(worldIn.isRemote)
        {
            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }

        if(playerIn.isSneaking())
        {
            IBarrelModel model = YabbaRegistry.INSTANCE.getModel(itemStackIn.hasTagCompound() ? itemStackIn.getTagCompound().getString("BarrelModel") : "");
            itemStackIn.setTagInfo("BarrelModel", new NBTTagString(YabbaRegistry.ALL_MODELS.get((YabbaRegistry.ALL_MODELS.indexOf(model) + 1) % YabbaRegistry.ALL_MODELS.size()).getName()));
        }
        else
        {
            RayTraceResult ray = rayTrace(worldIn, playerIn, true);

            if(ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                String id = YabbaUtils.getName(worldIn.getBlockState(ray.getBlockPos()));

                if(YabbaRegistry.INSTANCE.hasSkin(id))
                {
                    IBarrelSkin skin = YabbaRegistry.INSTANCE.getSkin(id);

                    if(!skin.equals(YabbaRegistry.INSTANCE.getSkin(itemStackIn.hasTagCompound() ? itemStackIn.getTagCompound().getString("BarrelSkin") : "")))
                    {
                        itemStackIn.setTagInfo("BarrelSkin", new NBTTagString(skin.getName()));
                        playerIn.addChatMessage(new TextComponentString("Selected " + skin.getDisplayName()));
                    }
                }
            }
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("Model: " + YabbaRegistry.INSTANCE.getModel(stack.hasTagCompound() ? stack.getTagCompound().getString("BarrelModel") : "").getName());
        tooltip.add("Skin: " + YabbaRegistry.INSTANCE.getSkin(stack.hasTagCompound() ? stack.getTagCompound().getString("BarrelSkin") : "").getDisplayName());
    }
}