package com.latmod.yabba.item;

import com.feed_the_beast.ftblib.lib.block.ItemBlockBase;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.latmod.yabba.block.BlockAdvancedBarrelBase;
import com.latmod.yabba.tile.TileBarrelBase;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
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
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (CommonUtils.hasBlockData(stack))
		{
			TileBarrelBase barrel = (TileBarrelBase) block.createTileEntity(world, block.getDefaultState());
			barrel.readFromNBT(CommonUtils.getBlockData(stack));

			if (block instanceof BlockAdvancedBarrelBase)
			{
				BarrelLook look = barrel.getLook();
				tooltip.add(ItemHammer.getModelTooltip(look.model));
				tooltip.add(ItemPainter.getSkinTooltip(look.skin));
			}

			barrel.addInformation(tooltip, flag);
		}
		else if (block instanceof BlockAdvancedBarrelBase)
		{
			tooltip.add(ItemHammer.getModelTooltip(EnumBarrelModel.BARREL));
			tooltip.add(ItemPainter.getSkinTooltip(""));
		}
	}
}