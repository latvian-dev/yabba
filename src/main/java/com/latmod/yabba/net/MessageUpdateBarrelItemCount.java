package com.latmod.yabba.net;

import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.latmod.yabba.tile.TileItemBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author LatvianModder
 */
public class MessageUpdateBarrelItemCount extends MessageToClient<MessageUpdateBarrelItemCount>
{
	private BlockPos pos;
	private int itemCount;

	public MessageUpdateBarrelItemCount()
	{
	}

	public MessageUpdateBarrelItemCount(BlockPos p, int c)
	{
		pos = p;
		itemCount = c;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return YabbaNetHandler.NET;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = NetUtils.readPos(buf);
		itemCount = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		NetUtils.writePos(buf, pos);
		buf.writeInt(itemCount);
	}

	@Override
	public void onMessage(MessageUpdateBarrelItemCount message, EntityPlayer player)
	{
		TileEntity tile = player.world.getTileEntity(message.pos);

		if (tile instanceof TileItemBarrel)
		{
			TileItemBarrel barrel = (TileItemBarrel) tile;
			barrel.setItemCount(message.itemCount);
			barrel.clearCachedData();
		}
	}
}