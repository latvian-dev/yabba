package com.latmod.yabba.net;

import com.feed_the_beast.ftblib.lib.icon.Icon;
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
import java.util.List;

/**
 * @author LatvianModder
 */
public class MessageBarrelConnector extends MessageToClient<MessageBarrelConnector>
{
	public static class BarrelInst
	{
		public static final DataOut.Serializer<BarrelInst> SERIALIZER = (data, object) ->
		{
			data.writeTextComponent(object.title);
			data.writeTextComponent(object.title2);
			data.writeIcon(object.icon);
			data.writeIcon(object.icon2);
			data.writePos(object.pos);
		};

		public static final DataIn.Deserializer<BarrelInst> DESERIALIZER = data ->
		{
			BarrelInst inst = new BarrelInst();
			inst.title = data.readTextComponent();
			inst.title2 = data.readTextComponent();
			inst.icon = data.readIcon();
			inst.icon2 = data.readIcon();
			inst.pos = data.readPos();
			return inst;
		};

		public ITextComponent title;
		public ITextComponent title2;
		public Icon icon;
		public Icon icon2;
		public BlockPos pos;
	}

	private ITextComponent title;
	private Collection<BarrelInst> barrels;

	public MessageBarrelConnector()
	{
	}

	public MessageBarrelConnector(ITextComponent t, List<BarrelInst> l)
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
		data.writeCollection(barrels, BarrelInst.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		title = data.readTextComponent();
		barrels = data.readCollection(BarrelInst.DESERIALIZER);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		new GuiBarrelConnector(title, barrels).openGui();
	}
}