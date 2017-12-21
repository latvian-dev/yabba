package com.latmod.yabba.item;

import com.feed_the_beast.ftblib.lib.block.ItemBlockBase;
import com.latmod.yabba.tile.TileBarrelBase;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
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
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag"))
		{
			TileBarrelBase barrel = (TileBarrelBase) block.createTileEntity(worldIn, block.getDefaultState());
			barrel.readFromNBT(stack.getTagCompound().getCompoundTag("BlockEntityTag"));
			tooltip.add(ItemHammer.getModelTooltip(barrel.model));
			tooltip.add(ItemPainter.getSkinTooltip(barrel.skin));
			barrel.addInformation(tooltip, flagIn);
		}
		else
		{
			tooltip.add(ItemHammer.getModelTooltip(""));
			tooltip.add(ItemPainter.getSkinTooltip(""));
		}
	}

	@Override
	public boolean renderPlacement(ItemStack stack, EntityPlayer player, RayTraceResult ray)
	{
		return true;
	}
}