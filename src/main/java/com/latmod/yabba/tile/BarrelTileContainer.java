package com.latmod.yabba.tile;

import com.latmod.yabba.BarrelTier;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelTier;
import com.latmod.yabba.api.IBarrelVariant;
import com.latmod.yabba.block.Barrel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class BarrelTileContainer extends Barrel implements INBTSerializable<NBTTagCompound>
{
    private final TileBarrel tile;
    private IBarrelTier tier;
    ItemStack storedItem;
    int itemCount;
    private NBTTagCompound upgrades;
    private IBarrelVariant variant;

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

        nbt.setString("Variant", getVariant().getName());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        tier = YabbaRegistry.INSTANCE.getTier(nbt.getString("Tier"));
        storedItem = nbt.hasKey("Item") ? ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item")) : null;
        itemCount = storedItem == null ? 0 : nbt.getInteger("Count");
        upgrades = nbt.hasKey("Upgrades") ? nbt.getCompoundTag("Upgrades") : null;
        variant = YabbaRegistry.INSTANCE.getVariant(nbt.getString("Variant"));
    }

    @Override
    public IBarrelTier getTier()
    {
        return tier == null ? BarrelTier.NONE : tier;
    }

    @Override
    public void setTier(IBarrelTier t)
    {
        tier = t;
    }

    @Override
    public int getItemCount()
    {
        return itemCount;
    }

    @Override
    public void setItemCount(int v)
    {
        itemCount = v;
    }

    @Override
    public NBTTagCompound getUpgradeNBT()
    {
        return upgrades;
    }

    @Override
    public IBarrelVariant getVariant()
    {
        if(variant == null)
        {
            variant = YabbaRegistry.DEFAULT_VARIANT;
        }

        return variant;
    }

    @Override
    public void setUpgradeNBT(@Nullable NBTTagCompound nbt)
    {
        upgrades = nbt;
    }

    @Override
    public void setVariant(IBarrelVariant v)
    {
        variant = v;
    }

    @Override
    public void setStackInSlot(int slot, @Nullable ItemStack stack)
    {
        storedItem = stack;
    }

    @Override
    @Nullable
    public ItemStack getStackInSlot(int slot)
    {
        return storedItem;
    }

    @Override
    public void updateCounter(boolean full)
    {
        if(full)
        {
            tile.markDirty();
        }
        else
        {
            tile.updateNumber = true;
        }
    }
}