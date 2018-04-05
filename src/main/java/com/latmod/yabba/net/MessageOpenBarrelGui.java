package com.latmod.yabba.net;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.latmod.yabba.tile.IBarrelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author LatvianModder
 */
public class MessageOpenBarrelGui extends MessageToServer<MessageOpenBarrelGui>
{
	private BlockPos pos;

	public MessageOpenBarrelGui()
	{
	}

	public MessageOpenBarrelGui(BlockPos p)
	{
		pos = p;
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
	}

	@Override
	public void readData(DataIn data)
	{
		pos = data.readPos();
	}

	@Override
	public void onMessage(MessageOpenBarrelGui message, EntityPlayer player)
	{
		if (player.world.isBlockLoaded(message.pos))
		{
			TileEntity tileEntity = player.world.getTileEntity(pos);

			if (tileEntity instanceof IBarrelBase)
			{
				((IBarrelBase) tileEntity).openGui(player);
			}
		}
	}
}