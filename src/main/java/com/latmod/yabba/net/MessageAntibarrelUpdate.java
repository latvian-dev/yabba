package com.latmod.yabba.net;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.latmod.yabba.gui.GuiAntibarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author LatvianModder
 */
public class MessageAntibarrelUpdate extends MessageToClient<MessageAntibarrelUpdate>
{
	private NBTTagCompound nbt;

	public MessageAntibarrelUpdate()
	{
	}

	public MessageAntibarrelUpdate(TileAntibarrel antibarrel)
	{
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
		data.writeNBT(nbt);
	}

	@Override
	public void readData(DataIn data)
	{
		nbt = data.readNBT();
	}

	@Override
	public void onMessage(MessageAntibarrelUpdate message, EntityPlayer player)
	{
		GuiAntibarrel gui = ClientUtils.getCurrentGuiAs(GuiAntibarrel.class);

		if (gui != null)
		{
			gui.container.tile.readData(message.nbt, EnumSaveType.SAVE);
			gui.refreshWidgets();
		}
	}
}