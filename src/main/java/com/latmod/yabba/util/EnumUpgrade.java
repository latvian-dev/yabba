package com.latmod.yabba.util;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IUpgrade;
import net.minecraft.item.ItemStack;
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
    //10
    OBSIDIAN_SHELL(11),
    REDSTONE_OUT(12),
    HOPPER(13),
    //14
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
                if(!barrel.getFlag(IBarrel.FLAG_INFINITE_CAPACITY))
                {
                    if(!simulate)
                    {
                        barrel.setFlag(IBarrel.FLAG_INFINITE_CAPACITY, true);
                    }
                    return true;
                }
                break;
            }
            case CREATIVE:
            {
                if(!barrel.getFlag(IBarrel.FLAG_IS_CREATIVE) && barrel.getStackInSlot(0) != null)
                {
                    if(!simulate)
                    {
                        barrel.setItemCount(1000000000);
                        barrel.setFlag(IBarrel.FLAG_IS_CREATIVE, true);
                        barrel.setFlag(IBarrel.FLAG_INFINITE_CAPACITY, true);
                        barrel.setFlag(IBarrel.FLAG_LOCKED, false);
                        barrel.addUpgradeName(CREATIVE.uname);
                    }
                    return true;
                }
                break;
            }
            case OBSIDIAN_SHELL:
            {
                if(!barrel.getFlag(IBarrel.FLAG_OBSIDIAN_SHELL))
                {
                    if(!simulate)
                    {
                        barrel.setFlag(IBarrel.FLAG_OBSIDIAN_SHELL, true);
                        barrel.addUpgradeName(OBSIDIAN_SHELL.uname);
                    }
                    return true;
                }
                break;
            }
            /*
            case REDSTONE_OUT:
            {
                if(!barrel.getFlag(IBarrel.FLAG_REDSTONE_OUT))
                {
                    if(!simulate)
                    {
                        barrel.setFlag(IBarrel.FLAG_REDSTONE_OUT, true);
                        barrel.setUpgradeData("RedstoneMode", new NBTTagByte((byte) 0));
                        barrel.addUpgradeName(REDSTONE_OUT.uname);
                    }
                    return true;
                }
                break;
            }
            case HOPPER:
            {
                if(!barrel.getFlag(IBarrel.FLAG_HOPPER))
                {
                    if(!simulate)
                    {
                        barrel.setFlag(IBarrel.FLAG_HOPPER, true);
                        barrel.addUpgradeName(HOPPER.uname);
                    }
                    return true;
                }
                break;
            }
            */
            case VOID:
            {
                if(!barrel.getFlag(IBarrel.FLAG_VOID_ITEMS))
                {
                    if(!simulate)
                    {
                        barrel.setFlag(IBarrel.FLAG_VOID_ITEMS, true);
                        barrel.addUpgradeName(VOID.uname);
                    }
                    return true;
                }
                break;
            }
        }

        return false;
    }
}