package com.latmod.yabba.block;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrelModifiable;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 17.12.2016.
 */
public class RecipeBarrelUpgrade implements IRecipe
{
    private ItemStack barrelStack, upgradeStack;
    private World worldObj;

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        worldObj = worldIn;
        barrelStack = ItemStackTools.getEmptyStack();
        upgradeStack = ItemStackTools.getEmptyStack();

        for(int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack is = inv.getStackInSlot(i);

            if(ItemStackTools.isEmpty(is))
            {
                continue;
            }

            if(is.hasCapability(YabbaCommon.BARREL_CAPABILITY, null))
            {
                if(!ItemStackTools.isEmpty(barrelStack))
                {
                    return false;
                }
                else
                {
                    barrelStack = inv.getStackInSlot(i);
                }
            }
            else if(is.hasCapability(YabbaCommon.UPGRADE_CAPABILITY, null))
            {
                if(!ItemStackTools.isEmpty(upgradeStack))
                {
                    return false;
                }
                else
                {
                    upgradeStack = inv.getStackInSlot(i);
                }
            }
        }

        if(ItemStackTools.isEmpty(barrelStack) || ItemStackTools.isEmpty(upgradeStack))
        {
            return false;
        }
        else
        {
            for(int i = 0; i < inv.getSizeInventory(); i++)
            {
                ItemStack is = inv.getStackInSlot(i);

                if(!ItemStackTools.isEmpty(is) && is != barrelStack && is != upgradeStack)
                {
                    return false;
                }
            }
        }

        return upgradeStack.getCapability(YabbaCommon.UPGRADE_CAPABILITY, null).applyOn((IBarrelModifiable) barrelStack.getCapability(YabbaCommon.BARREL_CAPABILITY, null), worldObj, upgradeStack, true);
    }

    @Nullable
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack output = ItemHandlerHelper.copyStackWithSize(barrelStack, 1);
        upgradeStack.getCapability(YabbaCommon.UPGRADE_CAPABILITY, null).applyOn((IBarrelModifiable) output.getCapability(YabbaCommon.BARREL_CAPABILITY, null), worldObj, upgradeStack, false);
        return output;
    }

    @Override
    public int getRecipeSize()
    {
        return 2;
    }

    @Nullable
    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStackTools.getEmptyStack();
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv)
    {
        ItemStack[] stacks = new ItemStack[inv.getSizeInventory()];

        for(int i = 0; i < stacks.length; ++i)
        {
            stacks[i] = ForgeHooks.getContainerItem(inv.getStackInSlot(i));
        }

        return stacks;
    }
}
