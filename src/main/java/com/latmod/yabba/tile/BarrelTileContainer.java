package com.latmod.yabba.tile;

import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.Tier;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.util.Barrel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public abstract class BarrelTileContainer extends Barrel implements INBTSerializable<NBTTagCompound>
{
    private Tier tier = Tier.WOOD;
    private int flags;
    private ItemStack storedItem = ItemStack.EMPTY;
    private int itemCount;
    private NBTTagCompound upgrades;
    private NBTTagList upgradeNames;
    private String skin = YabbaRegistry.DEFAULT_SKIN.getName();
    private String model = ModelBarrel.INSTANCE.getName();

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Tier", getTier().getName());
        nbt.setInteger("Flags", flags);

        if(!storedItem.isEmpty())
        {
            storedItem.setCount(1);
            nbt.setTag("Item", storedItem.serializeNBT());
            nbt.setInteger("Count", getItemCount());
        }

        if(upgrades != null && !upgrades.hasNoTags())
        {
            nbt.setTag("Upgrades", upgrades);
        }

        nbt.setString("Model", getModel());
        nbt.setString("Skin", getSkin());

        if(upgradeNames != null && !upgradeNames.hasNoTags())
        {
            nbt.setTag("UpgradeNames", upgradeNames);
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        tier = Tier.getFromName(nbt.getString("Tier"));
        flags = nbt.getInteger("Flags");
        storedItem = nbt.hasKey("Item") ? new ItemStack(nbt.getCompoundTag("Item")) : ItemStack.EMPTY;
        itemCount = storedItem.isEmpty() ? 0 : nbt.getInteger("Count");
        upgrades = nbt.hasKey("Upgrades") ? nbt.getCompoundTag("Upgrades") : null;
        model = nbt.getString("Model");
        skin = nbt.getString("Skin");
        upgradeNames = nbt.hasKey("UpgradeNames") ? nbt.getTagList("UpgradeNames", Constants.NBT.TAG_STRING) : null;

        if(getFlag(FLAG_IS_CREATIVE))
        {
            itemCount = tier.getMaxItems(this, getStackInSlot(0)) / 2;
        }
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        int count = getItemCount();
        if(count > 0)
        {
            storedItem.setCount(count);
        }

        return storedItem;
    }

    @Override
    public Tier getTier()
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
    public String getSkin()
    {
        return skin;
    }

    @Override
    public String getModel()
    {
        return model;
    }

    @Nullable
    @Override
    public NBTTagList getUpgradeNames()
    {
        return upgradeNames;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack)
    {
        storedItem = (stack == null || stack.isEmpty()) ? ItemStack.EMPTY : stack;
    }

    @Override
    public void setTier(Tier t)
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
    public void setSkin(String v)
    {
        skin = v;
    }

    @Override
    public void setModel(String m)
    {
        model = m;
    }

    @Override
    public void setUpgradeNames(@Nullable NBTTagList nbt)
    {
        upgradeNames = nbt;
    }
}