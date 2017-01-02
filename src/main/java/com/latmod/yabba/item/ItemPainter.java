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
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
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
    private static IBarrelModel getModel(ItemStack stack, boolean client)
    {
        return YabbaRegistry.INSTANCE.getModel(stack.hasTagCompound() ? stack.getTagCompound().getByte("BarrelModel") : 0, client);
    }

    private static IBarrelSkin getSkin(ItemStack stack, boolean client)
    {
        return YabbaRegistry.INSTANCE.getSkin(stack.hasTagCompound() ? stack.getTagCompound().getInteger("BarrelSkin") : 0, client);
    }

    private static void setModel(ItemStack stack, String modelId)
    {
        stack.setTagInfo("BarrelModel", new NBTTagByte(YabbaRegistry.INSTANCE.getModelId(modelId)));
    }

    private static void setSkin(ItemStack stack, String skinId)
    {
        stack.setTagInfo("BarrelSkin", new NBTTagInt(YabbaRegistry.INSTANCE.getSkinId(skinId)));
    }

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
            IBarrelModel model = getModel(upgradeItem, worldIn.isRemote);
            IBarrelSkin skin = getSkin(upgradeItem, worldIn.isRemote);

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
            setModel(itemStackIn, YabbaRegistry.ALL_MODELS.get((YabbaRegistry.ALL_MODELS.indexOf(getModel(itemStackIn, false)) + 1) % YabbaRegistry.ALL_MODELS.size()).getName());
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

                    if(!skin.equals(getSkin(itemStackIn, false)))
                    {
                        setSkin(itemStackIn, skin.getName());
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
        tooltip.add("Model: " + getModel(stack, playerIn.worldObj.isRemote).getName());
        tooltip.add("Skin: " + getSkin(stack, playerIn.worldObj.isRemote).getDisplayName());
    }
}