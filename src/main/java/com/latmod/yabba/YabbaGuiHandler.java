package com.latmod.yabba;

import com.latmod.yabba.gui.ContainerAntibarrel;
import com.latmod.yabba.gui.GuiAntibarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class YabbaGuiHandler implements IGuiHandler
{
	public static final int GUI_ANTIBARREL = 1;

	public static void open(EntityPlayer player, int ID, BlockPos pos)
	{
		player.openGui(Yabba.MOD, ID, player.world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		switch (ID)
		{
			case GUI_ANTIBARREL:
				return new ContainerAntibarrel(player, ((TileAntibarrel) tileEntity));
			default:
				return null;
		}
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return getClientGui(ID, player, world.getTileEntity(new BlockPos(x, y, z)));
	}

	@Nullable
	private Object getClientGui(int ID, EntityPlayer player, TileEntity tileEntity)
	{
		switch (ID)
		{
			case GUI_ANTIBARREL:
				return new GuiAntibarrel(new ContainerAntibarrel(player, ((TileAntibarrel) tileEntity))).getWrapper();
			default:
				return null;
		}
	}
}