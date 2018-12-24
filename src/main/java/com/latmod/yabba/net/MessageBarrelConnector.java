package com.latmod.yabba.net;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.latmod.yabba.gui.GuiBarrelConnector;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class MessageBarrelConnector extends MessageToClient
{
	private ITextComponent title;
	private Collection<BlockPos> barrels;

	public MessageBarrelConnector()
	{
	}

	public MessageBarrelConnector(ITextComponent t, Collection<BlockPos> l)
	{
		title = t;
		barrels = l;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return YabbaNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeTextComponent(title);
		data.writeCollection(barrels, DataOut.BLOCK_POS);
	}

	@Override
	public void readData(DataIn data)
	{
		title = data.readTextComponent();
		barrels = data.readCollection(DataIn.BLOCK_POS);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		new GuiBarrelConnector(title, barrels).openGui();
	}
}