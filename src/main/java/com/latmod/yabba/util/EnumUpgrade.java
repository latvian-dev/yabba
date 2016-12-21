package com.latmod.yabba.util;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.world.World;

import java.util.Locale;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public enum EnumUpgrade implements IUpgrade
{
    BLANK(0),
    IRON_UPGRADE(1),
    GOLD_UPGRADE(2),
    DIAMOND_UPGRADE(3),
    NETHER_STAR_UPGRADE(4),
    CREATIVE(9),
    //LOCKED(10),
    OBSIDIAN_SHELL(11),
    REDSTONE_OUT(12),
    HOPPER(13),
    ENDER_LINK(14),
    VOID(15);

    public static final EnumUpgrade[] VALUES = values();

    private final String name;
    public final String uname;
    public final int metadata;

    EnumUpgrade(int meta)
    {
        name = name().toLowerCase(Locale.ENGLISH);
        uname = "item.yabba.upgrade." + name;
        metadata = meta;
    }

    public ItemStack item()
    {
        return new ItemStack(YabbaItems.UPGRADE, 1, metadata);
    }

    public static EnumUpgrade getFromMeta(int meta)
    {
        for(EnumUpgrade t : VALUES)
        {
            if(t.metadata == meta)
            {
                return t;
            }
        }

        return BLANK;
    }

    @Override
    public String getUpgradeName()
    {
        return name;
    }

    @Override
    public boolean applyOn(IBarrelModifiable barrel, World worldIn, ItemStack upgradeItem, boolean simulate)
    {
        switch(this)
        {
            case BLANK:
            {
                if(barrel.getTier().equals(Tier.NONE))
                {
                    if(!simulate)
                    {
                        barrel.setTier(YabbaCommon.TIER_WOOD);
                    }
                    return true;
                }
                break;
            }
            case IRON_UPGRADE:
            {
                if(barrel.getTier().equals(YabbaCommon.TIER_WOOD))
                {
                    if(!simulate)
                    {
                        barrel.setTier(YabbaCommon.TIER_IRON);
                    }
                    return true;
                }
                break;
            }
            case GOLD_UPGRADE:
            {
                if(barrel.getTier().equals(YabbaCommon.TIER_IRON))
                {
                    if(!simulate)
                    {
                        barrel.setTier(YabbaCommon.TIER_GOLD);
                    }
                    return true;
                }
                break;
            }
            case DIAMOND_UPGRADE:
            {
                if(barrel.getTier().equals(YabbaCommon.TIER_GOLD))
                {
                    if(!simulate)
                    {
                        barrel.setTier(YabbaCommon.TIER_DMD);
                    }
                    return true;
                }
                break;
            }
            case NETHER_STAR_UPGRADE:
            {
                if(barrel.getTier().equals(YabbaCommon.TIER_DMD))
                {
                    if(!simulate)
                    {
                        barrel.setTier(YabbaCommon.TIER_INF);
                    }
                    return true;
                }
                break;
            }
            case CREATIVE:
            {
                if(!barrel.getTier().equals(YabbaCommon.TIER_CREATIVE) && barrel.getStackInSlot(0) != null)
                {
                    if(!simulate)
                    {
                        barrel.setItemCount(barrel.getStackInSlot(0).getMaxStackSize());
                        barrel.setTier(YabbaCommon.TIER_CREATIVE);
                    }
                    return true;
                }
                break;
            }
            case OBSIDIAN_SHELL:
            {
                if(barrel.getUpgradeData("ObsidianShell") == null)
                {
                    if(!simulate)
                    {
                        barrel.setUpgradeData("ObsidianShell", new NBTTagByte((byte) 1));
                    }
                    return true;
                }
                break;
            }
        }

        return false;
    }
}
