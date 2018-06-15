package com.latmod.yabba.net;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.latmod.yabba.gui.GuiAntibarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class MessageAntibarrelUpdate extends MessageToClient
{
	private BlockPos pos;
	private NBTTagCompound nbt;

	public MessageAntibarrelUpdate()
	{
	}

	public MessageAntibarrelUpdate(TileAntibarrel antibarrel)
	{
		pos = antibarrel.getPos();
		nbt = new NBTTagCompound();
		antibarrel.writeData(nbt, EnumSaveType.SAVE);
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return YabbaNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writePos(pos);
		data.writeNBT(nbt);
	}

	@Override
	public void readData(DataIn data)
	{
		pos = data.readPos();
		nbt = data.readNBT();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		TileEntity tileEntity = ClientUtils.MC.world.getTileEntity(pos);

		if (tileEntity instanceof TileAntibarrel)
		{
			((TileAntibarrel) tileEntity).readData(nbt, EnumSaveType.SAVE);

			GuiAntibarrel gui = ClientUtils.getCurrentGuiAs(GuiAntibarrel.class);

			if (gui != null)
			{
				gui.refreshWidgets();
			}
		}
	}
}