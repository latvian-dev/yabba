package com.latmod.yabba.net;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class MessageUpdateBarrelContent extends MessageToClient
{
	private BlockPos pos;
	private NBTBase nbt;

	public MessageUpdateBarrelContent()
	{
	}

	public MessageUpdateBarrelContent(TileBarrel tile)
	{
		pos = tile.getPos();
		nbt = tile.barrel.content.writeContentData();
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
		data.writeNBTBase(nbt);
	}

	@Override
	public void readData(DataIn data)
	{
		pos = data.readPos();
		nbt = data.readNBTBase();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(pos);

		if (tileEntity instanceof TileBarrel)
		{
			((TileBarrel) tileEntity).barrel.content.readContentData(nbt);
			((TileBarrel) tileEntity).markBarrelDirty(false);
		}
	}
}