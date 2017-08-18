package com.latmod.yabba.item;

import com.feed_the_beast.ftbl.lib.block.ItemBlockBase;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemBlockBarrel extends ItemBlockBase
{
	public ItemBlockBarrel(Block block)
	{
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		String m = "", s = "";
		NBTTagCompound data = null;

		if (stack.hasTagCompound())
		{
			data = stack.getTagCompound().getCompoundTag("BlockEntityTag");
			m = data.getString("Model");
			s = data.getString("Skin");
		}

		tooltip.add(ItemHammer.getModelTooltip(m));
		tooltip.add(ItemPainter.getSkinTooltip(s));

		if (data != null && !data.hasNoTags())
		{
			TileItemBarrel barrel = new TileItemBarrel();
			barrel.readFromNBT(data);

			if (!barrel.storedItem.isEmpty())
			{
				tooltip.add("Item: " + barrel.storedItem.getDisplayName()); //LANG
			}

			if (!barrel.hasUpgrade(YabbaItems.UPGRADE_CREATIVE))
			{
				if (barrel.hasUpgrade(YabbaItems.UPGRADE_INFINITE_CAPACITY))
				{
					tooltip.add(barrel.itemCount + " items"); //LANG
				}
				else if (!barrel.storedItem.isEmpty())
				{
					tooltip.add(barrel.itemCount + " / " + barrel.tier.getMaxItems(barrel, barrel.storedItem));
				}
				else
				{
					tooltip.add("Max " + barrel.tier.maxItemStacks.getInt() + " stacks"); //LANG
				}
			}
			
			/*
			List<ITextComponent> upgrades = barrel.getUpgradeNames();

			if (!upgrades.isEmpty())
			{
				tooltip.add("Upgrades:"); //LANG

				for (ITextComponent component : upgrades)
				{
					tooltip.add("> " + component.getFormattedText());
				}
			}*/
		}
	}
}