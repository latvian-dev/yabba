package com.latmod.yabba.item;

import com.feed_the_beast.ftbl.lib.block.ItemBlockBase;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.latmod.yabba.YabbaCommon;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
		if (!stack.hasTagCompound())
		{
			return;
		}

		String m = stack.getTagCompound().getString("Model");
		String s = stack.getTagCompound().getString("Skin");

		tooltip.add(ItemHammer.getModelTooltip(m.isEmpty() ? YabbaCommon.DEFAULT_MODEL_ID : new ResourceLocation(m)));
		tooltip.add(ItemPainter.getSkinTooltip(s.isEmpty() ? YabbaCommon.DEFAULT_SKIN_ID : CommonUtils.getStateFromName(s)));

		/*
		Tier tier = barrel.getTier();
		ItemStack stack1 = barrel.getStoredItemType();

		if (!stack1.isEmpty())
		{
			tooltip.add("Item: " + stack1.getDisplayName());
		}

		if (!barrel.getFlag(Barrel.FLAG_IS_CREATIVE))
		{
			if (barrel.getFlag(Barrel.FLAG_INFINITE_CAPACITY))
			{
				tooltip.add(barrel.getItemCount() + " items"); //LANG
			}
			else if (!stack1.isEmpty())
			{
				tooltip.add(barrel.getItemCount() + " / " + tier.getMaxItems(barrel, stack1));
			}
			else
			{
				tooltip.add("Max " + tier.maxItemStacks.getInt() + " stacks");
			}
		}

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