package com.latmod.yabba.item;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemUpgrade extends ItemYabba
{
    private static class UpgradeCapProvider implements ICapabilityProvider
    {
        private final ItemStack itemStack;

        private UpgradeCapProvider(ItemStack is)
        {
            itemStack = is;
        }

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
        {
            return capability == YabbaCommon.UPGRADE_CAPABILITY;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
        {
            return capability == YabbaCommon.UPGRADE_CAPABILITY ? (T) EnumUpgrade.getFromMeta(itemStack.getMetadata()) : null;
        }
    }

    public ItemUpgrade()
    {
        super("upgrade");
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new UpgradeCapProvider(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nullable Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        for(EnumUpgrade type : EnumUpgrade.VALUES)
        {
            subItems.add(new ItemStack(this, 1, type.metadata));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv)
    {
        list.add(StringUtils.translate(EnumUpgrade.getFromMeta(stack.getMetadata()).uname));
    }
}