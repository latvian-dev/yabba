package com.latmod.yabba.tile;

import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.ITier;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.util.Barrel;
import com.latmod.yabba.util.Tier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public abstract class BarrelTileContainer extends Barrel implements INBTSerializable<NBTTagCompound>
{
    private ITier tier;
    private int flags;
    private ItemStack storedItem;
    private int itemCount;
    private NBTTagCompound upgrades;
    private NBTTagList upgradeNames;
    private IBarrelSkin skin;
    private IBarrelModel model;

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Tier", getTier().getName());
        nbt.setInteger("Flags", flags);

        if(storedItem != null)
        {
            storedItem.stackSize = 1;
            nbt.setTag("Item", storedItem.serializeNBT());
            nbt.setInteger("Count", getItemCount());
        }

        if(upgrades != null && !upgrades.hasNoTags())
        {
            nbt.setTag("Upgrades", upgrades);
        }

        nbt.setString("Model", getModel().getName());
        nbt.setString("Skin", getSkin().getName());

        if(upgradeNames != null && !upgradeNames.hasNoTags())
        {
            nbt.setTag("UpgradeNames", upgradeNames);
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        String tierID = nbt.getString("Tier");
        tier = YabbaRegistry.INSTANCE.getTier(tierID);
        flags = nbt.getInteger("Flags");
        storedItem = nbt.hasKey("Item") ? ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item")) : null;
        itemCount = storedItem == null ? 0 : nbt.getInteger("Count");
        upgrades = nbt.hasKey("Upgrades") ? nbt.getCompoundTag("Upgrades") : null;
        model = YabbaRegistry.INSTANCE.getModel(nbt.getString("Model"));
        skin = YabbaRegistry.INSTANCE.getSkin(nbt.getString("Skin"));
        upgradeNames = nbt.hasKey("UpgradeNames") ? nbt.getTagList("UpgradeNames", Constants.NBT.TAG_STRING) : null;

        if(getFlag(FLAG_IS_CREATIVE))
        {
            itemCount = tier.getMaxItems(this, getStackInSlot(0)) / 2;
        }
    }

    @Override
    @Nullable
    public ItemStack getStackInSlot(int slot)
    {
        if(storedItem != null)
        {
            storedItem.stackSize = getItemCount();
        }

        return storedItem;
    }

    @Override
    public ITier getTier()
    {
        return tier == null ? Tier.WOOD : tier;
    }

    @Override
    public int getFlags()
    {
        return flags;
    }

    @Override
    public int getItemCount()
    {
        return itemCount;
    }

    @Override
    public NBTTagCompound getUpgradeNBT()
    {
        if(upgrades == null)
        {
            upgrades = new NBTTagCompound();
        }

        return upgrades;
    }

    @Override
    public IBarrelSkin getSkin()
    {
        if(skin == null)
        {
            skin = YabbaRegistry.DEFAULT_SKIN;
        }

        return skin;
    }

    @Override
    public IBarrelModel getModel()
    {
        if(model == null)
        {
            model = ModelBarrel.INSTANCE;
        }

        return model;
    }

    @Nullable
    @Override
    public NBTTagList getUpgradeNames()
    {
        return upgradeNames;
    }

    @Override
    public void setStackInSlot(int slot, @Nullable ItemStack stack)
    {
        storedItem = stack;
    }

    @Override
    public void setTier(ITier t)
    {
        tier = t;
    }

    @Override
    public void setFlags(int f)
    {
        flags = f;
    }

    @Override
    public void setItemCount(int v)
    {
        itemCount = v;
    }

    @Override
    public void setUpgradeNBT(@Nullable NBTTagCompound nbt)
    {
        upgrades = nbt;
    }

    @Override
    public void setSkin(IBarrelSkin v)
    {
        skin = v;
    }

    @Override
    public void setModel(IBarrelModel m)
    {
        model = m;
    }

    @Override
    public void setUpgradeNames(@Nullable NBTTagList nbt)
    {
        upgradeNames = nbt;
    }
}