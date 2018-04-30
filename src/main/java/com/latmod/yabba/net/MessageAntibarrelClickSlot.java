package com.latmod.yabba.net;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.item.ItemEntry;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.latmod.yabba.gui.ContainerAntibarrel;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class MessageAntibarrelClickSlot extends MessageToServer
{
	private ItemStack stack;
	private boolean shift;

	public MessageAntibarrelClickSlot()
	{
	}

	public MessageAntibarrelClickSlot(ItemEntry e, boolean s)
	{
		stack = e.getStack(1, true);
		shift = s;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return YabbaNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeItemStack(stack);
		data.writeBoolean(shift);
	}

	@Override
	public void readData(DataIn data)
	{
		stack = data.readItemStack();
		shift = data.readBoolean();
	}

	@Override
	public void onMessage(EntityPlayerMP player)
	{
		if (player.openContainer instanceof ContainerAntibarrel)
		{
			((ContainerAntibarrel) player.openContainer).onClick(ItemEntry.get(stack), shift);
		}
	}
}