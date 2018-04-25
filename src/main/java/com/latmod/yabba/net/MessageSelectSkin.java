package com.latmod.yabba.net;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.latmod.yabba.item.ItemPainter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * @author LatvianModder
 */
public class MessageSelectSkin extends MessageToServer<MessageSelectSkin>
{
	private String skinId;

	public MessageSelectSkin()
	{
	}

	public MessageSelectSkin(String id)
	{
		skinId = id;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return YabbaNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeString(skinId);
	}

	@Override
	public void readData(DataIn data)
	{
		skinId = data.readString();
	}

	@Override
	public void onMessage(EntityPlayerMP player)
	{
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

		if (!stack.isEmpty() && stack.getItem() instanceof ItemPainter)
		{
			ItemPainter.setSkin(stack, skinId);
		}
	}
}