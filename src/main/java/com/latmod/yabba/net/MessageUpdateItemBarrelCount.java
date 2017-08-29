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
public class MessageUpdateItemBarrelCount extends MessageToClient<MessageUpdateItemBarrelCount>
{
	private BlockPos pos;
	private int count;

	public MessageUpdateItemBarrelCount()
	{
	}

	public MessageUpdateItemBarrelCount(BlockPos p, int c)
	{
		pos = p;
		count = c;
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
		count = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		NetUtils.writePos(buf, pos);
		buf.writeInt(count);
	}

	@Override
	public void onMessage(MessageUpdateItemBarrelCount message, EntityPlayer player)
	{
		TileEntity tile = player.world.getTileEntity(message.pos);

		if (tile instanceof TileItemBarrel)
		{
			TileItemBarrel barrel = (TileItemBarrel) tile;
			barrel.setItemCount(message.count);
			barrel.updateContainingBlockInfo();
		}
	}
}