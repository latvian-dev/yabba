package com.latmod.yabba.net;

import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.latmod.yabba.item.ItemPainter;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * @author LatvianModder
 */
public class MessageSelectSkin extends MessageToServer<MessageSelectSkin>
{
	private IBlockState skinId;

	public MessageSelectSkin()
	{
	}

	public MessageSelectSkin(IBlockState id)
	{
		skinId = id;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return YabbaNetHandler.NET;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		skinId = NetUtils.readBlockState(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		NetUtils.writeBlockState(buf, skinId);
	}

	@Override
	public void onMessage(MessageSelectSkin message, EntityPlayer player)
	{
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

		if (!stack.isEmpty() && stack.getItem() instanceof ItemPainter)
		{
			ItemPainter.setSkin(stack, message.skinId);
		}
	}
}