package com.latmod.yabba.net;

import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.latmod.yabba.item.ItemHammer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class MessageSelectModel extends MessageToServer<MessageSelectModel>
{
	private ResourceLocation modelId;

	public MessageSelectModel()
	{
	}

	public MessageSelectModel(ResourceLocation id)
	{
		modelId = id;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return YabbaNetHandler.NET;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		modelId = NetUtils.readResourceLocation(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		NetUtils.writeResourceLocation(buf, modelId);
	}

	@Override
	public void onMessage(MessageSelectModel message, EntityPlayer player)
	{
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

		if (!stack.isEmpty() && stack.getItem() instanceof ItemHammer)
		{
			ItemHammer.setModel(stack, message.modelId);
		}
	}
}