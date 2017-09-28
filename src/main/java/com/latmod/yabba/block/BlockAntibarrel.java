package com.latmod.yabba.block;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.latmod.yabba.tile.TileAntibarrel;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BlockAntibarrel extends BlockYabba
{
	public BlockAntibarrel()
	{
		super("antibarrel", Material.ROCK, MapColor.NETHERRACK);
		setHardness(4F);
		setResistance(1000F);
	}

	@Override
	public boolean dropSpecial(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileAntibarrel();
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side != EnumFacing.DOWN;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.CUTOUT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		tooltip.add(StringUtils.translate("guide.yabba.antibarrel.info"));

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag"))
		{
			NBTTagList list = stack.getTagCompound().getCompoundTag("BlockEntityTag").getTagList("Inv", Constants.NBT.TAG_COMPOUND);

			if (list.tagCount() > 0)
			{
				tooltip.add(list.tagCount() + " items");
			}
		}
	}
}