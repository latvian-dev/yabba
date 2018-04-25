package com.latmod.yabba.net;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.latmod.yabba.item.ItemHammer;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * @author LatvianModder
 */
public class MessageSelectModel extends MessageToServer<MessageSelectModel>
{
	private EnumBarrelModel modelId;

	public MessageSelectModel()
	{
	}

	public MessageSelectModel(EnumBarrelModel id)
	{
		modelId = id;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return YabbaNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeString(modelId.getNBTName());
	}

	@Override
	public void readData(DataIn data)
	{
		modelId = EnumBarrelModel.getFromNBTName(data.readString());
	}

	@Override
	public void onMessage(EntityPlayerMP player)
	{
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

		if (!stack.isEmpty() && stack.getItem() instanceof ItemHammer)
		{
			ItemHammer.setModel(stack, modelId);
		}
	}
}