package com.latmod.yabba;

import com.feed_the_beast.ftblib.lib.gui.misc.BlockGuiHandler;
import com.feed_the_beast.ftblib.lib.gui.misc.BlockGuiSupplier;
import com.latmod.yabba.gui.ContainerAntibarrel;
import com.latmod.yabba.gui.GuiAntibarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

/**
 * @author LatvianModder
 */
public class YabbaGuiHandler extends BlockGuiHandler
{
	public static final BlockGuiSupplier ANTIBARREL = new BlockGuiSupplier(Yabba.MOD, 1)
	{
		@Override
		public Container getContainer(EntityPlayer player, TileEntity tileEntity)
		{
			return new ContainerAntibarrel(player, ((TileAntibarrel) tileEntity));
		}

		@Override
		public Object getGui(Container container)
		{
			return getGui0(container);
		}

		private Object getGui0(Container container)
		{
			return new GuiAntibarrel((ContainerAntibarrel) container).getWrapper();
		}
	};

	public YabbaGuiHandler()
	{
		add(ANTIBARREL);
	}
}