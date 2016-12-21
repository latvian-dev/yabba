package com.latmod.yabba.tile;

import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.ITier;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.util.Barrel;
import com.latmod.yabba.util.Tier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class BarrelTileContainer extends Barrel implements INBTSerializable<NBTTagCompound>
{
    private final TileBarrel tile;
    private ITier tier;
    ItemStack storedItem;
    int itemCount;
    private NBTTagCompound upgrades;
    private IBarrelSkin skin;
    private IBarrelModel model;

    public BarrelTileContainer(TileBarrel t)
    {
        tile = t;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Tier", getTier().getName());

        if(storedItem != null)
        {
            nbt.setTag("Item", storedItem.serializeNBT());
            nbt.setInteger("Count", getItemCount());
        }

        if(upgrades != null)
        {
            nbt.setTag("Upgrades", upgrades);
        }

        nbt.setString("Skin", getSkin().getName());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        tier = YabbaRegistry.INSTANCE.getTier(nbt.getString("Tier"));
        storedItem = nbt.hasKey("Item") ? ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item")) : null;
        itemCount = storedItem == null ? 0 : nbt.getInteger("Count");
        upgrades = nbt.hasKey("Upgrades") ? nbt.getCompoundTag("Upgrades") : null;
        skin = YabbaRegistry.INSTANCE.getSkin(nbt.getString("Skin"));
    }

    @Override
    @Nullable
    public ItemStack getStackInSlot(int slot)
    {
        return storedItem;
    }

    @Override
    public ITier getTier()
    {
        return tier == null ? Tier.NONE : tier;
    }

    @Override
    public int getItemCount()
    {
        return itemCount;
    }

    @Override
    public NBTTagCompound getUpgradeNBT()
    {
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

    @Override
    public boolean isLocked()
    {
        return getUpgradeData("Locked") != null;
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
    public void setLocked(boolean locked)
    {
        setUpgradeData("Locked", locked ? new NBTTagByte((byte) 1) : null);
    }

    @Override
    public void markBarrelDirty(boolean full)
    {
        tile.sendUpdate = full ? 2 : 1;
    }
}