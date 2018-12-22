package com.latmod.yabba.block;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.ItemIcon;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.latmod.yabba.net.MessageBarrelConnector;
import com.latmod.yabba.tile.ItemBarrel;
import com.latmod.yabba.tile.TileItemBarrelConnector;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author LatvianModder
 */
public class BlockItemBarrelConnector extends Block
{
	public BlockItemBarrelConnector()
	{
		super(Material.WOOD, MapColor.WOOD);
		setHardness(1F);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileItemBarrelConnector();
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
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		for (int i = 0; i < 10; i++)
		{
			world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + rand.nextFloat(), pos.getY(), pos.getZ() + rand.nextFloat(), 0D, 0D, 0D);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}

		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileItemBarrelConnector)
		{
			TileItemBarrelConnector connector = (TileItemBarrelConnector) tileEntity;
			connector.getSlots();
			List<MessageBarrelConnector.BarrelInst> list = new ArrayList<>();

			for (ItemBarrel barrel : connector.linkedBarrels)
			{
				MessageBarrelConnector.BarrelInst inst = new MessageBarrelConnector.BarrelInst();
				inst.pos = ((TileEntity) barrel.barrel.block).getPos();
				IBlockState blockState = world.getBlockState(inst.pos);
				ItemStack stack = blockState.getBlock().getItem(world, inst.pos, blockState);
				inst.title2 = new TextComponentString(stack.getDisplayName());
				inst.icon2 = ItemIcon.getItemIcon(stack);

				if (!barrel.type.isEmpty())
				{
					inst.title = new TextComponentString(StringUtils.formatDouble(barrel.count, true) + "x " + barrel.type.getDisplayName());
					inst.icon = ItemIcon.getItemIcon(ItemHandlerHelper.copyStackWithSize(barrel.type, 1));
				}
				else
				{
					inst.title = new TextComponentString("Empty"); //LANG
					inst.icon = Icon.EMPTY;
				}

				if (barrel.barrel.isLocked())
				{
					inst.title.getStyle().setColor(TextFormatting.GOLD);
				}

				list.add(inst);
			}

			new MessageBarrelConnector(new TextComponentTranslation("tile.yabba.item_barrel_connector.name"), list).sendTo((EntityPlayerMP) player);
		}

		return true;
	}
}