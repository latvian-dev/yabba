package com.latmod.yabba.block;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrelModifiable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * @author LatvianModder
 */
public class RecipeBarrelUpgrade implements IRecipe
{
	private ItemStack barrelStack, upgradeStack;
	private World worldObj;

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		worldObj = worldIn;
		barrelStack = ItemStack.EMPTY;
		upgradeStack = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack is = inv.getStackInSlot(i);

			if (is.isEmpty())
			{
				continue;
			}

			if (is.hasCapability(YabbaCommon.BARREL_CAPABILITY, null))
			{
				if (!barrelStack.isEmpty())
				{
					return false;
				}
				else
				{
					barrelStack = inv.getStackInSlot(i);
				}
			}
			else if (is.hasCapability(YabbaCommon.UPGRADE_CAPABILITY, null))
			{
				if (!upgradeStack.isEmpty())
				{
					return false;
				}
				else
				{
					upgradeStack = inv.getStackInSlot(i);
				}
			}
		}

		if (barrelStack.isEmpty() || upgradeStack.isEmpty())
		{
			return false;
		}
		else
		{
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack is = inv.getStackInSlot(i);

				if (!is.isEmpty() && is != barrelStack && is != upgradeStack)
				{
					return false;
				}
			}
		}

		return upgradeStack.getCapability(YabbaCommon.UPGRADE_CAPABILITY, null).applyOn((IBarrelModifiable) barrelStack.getCapability(YabbaCommon.BARREL_CAPABILITY, null), worldObj, upgradeStack, true);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack output = ItemHandlerHelper.copyStackWithSize(barrelStack, 1);
		upgradeStack.getCapability(YabbaCommon.UPGRADE_CAPABILITY, null).applyOn((IBarrelModifiable) output.getCapability(YabbaCommon.BARREL_CAPABILITY, null), worldObj, upgradeStack, false);
		return output;
	}

	@Override
	public boolean func_194133_a(int w, int h)
	{
		return w * h >= 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < list.size(); ++i)
		{
			ItemStack itemstack = inv.getStackInSlot(i);
			list.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
		}

		return list;
	}

	@Override
	public String func_193358_e()
	{
		return Yabba.MOD_ID + ":upgrade_barrel";
	}
}