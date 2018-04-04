package com.latmod.yabba.item;

import com.feed_the_beast.ftblib.lib.block.ItemBlockBase;
import com.latmod.yabba.tile.TileAdvancedBarrelBase;
import com.latmod.yabba.tile.TileBarrelBase;
import com.latmod.yabba.util.BarrelLook;
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
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag"))
		{
			TileBarrelBase barrel = (TileBarrelBase) block.createTileEntity(world, block.getDefaultState());
			barrel.readFromNBT(stack.getTagCompound().getCompoundTag("BlockEntityTag"));

			if (barrel instanceof TileAdvancedBarrelBase)
			{
				BarrelLook look = barrel.getLook();
				tooltip.add(ItemHammer.getModelTooltip(look.model));
				tooltip.add(ItemPainter.getSkinTooltip(look.skin));
			}

			barrel.addInformation(tooltip, flag);
		}
		else
		{
			tooltip.add(ItemHammer.getModelTooltip(""));
			tooltip.add(ItemPainter.getSkinTooltip(""));
		}
	}
}