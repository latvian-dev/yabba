package com.latmod.yabba.client.gui;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.icon.ItemIcon;
import com.feed_the_beast.ftbl.lib.icon.TexturelessRectangle;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.block.BlockItemBarrel;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.client.BarrelModel;
import com.latmod.yabba.client.YabbaClient;
import com.latmod.yabba.net.MessageSelectModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSelectModel extends GuiBase
{
	public static final Icon GUI_BACKGROUND = new TexturelessRectangle(0x22000000).setLineColor(Color4I.BLACK).setRoundEdges(true);
	public static final Icon BUTTON_BACKGROUND = new TexturelessRectangle(Color4I.NONE).setLineColor(Color4I.BLACK);

	private class ButtonModel extends Button
	{
		private final BarrelModel model;
		private final int index;

		public ButtonModel(int i)
		{
			super(2 + (i % 5) * 35, 2 + (i / 5) * 35, 34, 34);
			index = i;
			model = index >= YabbaClient.ALL_MODELS.size() ? null : YabbaClient.ALL_MODELS.get(index);

			if (model != null)
			{
				setTitle(model.toString());
				setIcon(new ItemIcon(((BlockItemBarrel) YabbaItems.ITEM_BARREL).createStack(YabbaItems.ITEM_BARREL.getDefaultState(), model.id, "", Tier.WOOD)));
			}
		}

		@Override
		public void onClicked(GuiBase gui, MouseButton button)
		{
			if (model != null)
			{
				GuiHelper.playClickSound();
				new MessageSelectModel(model.id).sendToServer();
				gui.closeGui();
			}
		}

		@Override
		public void renderWidget(GuiBase gui)
		{
			int ax = getAX();
			int ay = getAY();
			BUTTON_BACKGROUND.draw(ax, ay, width, height, Color4I.NONE);
			getIcon(gui).draw(ax + 1, ay + 1, 32, 32, Color4I.NONE);
		}
	}

	private final List<ButtonModel> buttons;

	public GuiSelectModel()
	{
		super(178, 143);

		buttons = new ArrayList<>();

		for (int i = 0; i < 20; i++)
		{
			buttons.add(new ButtonModel(i));
		}
	}

	@Override
	public void addWidgets()
	{
		addAll(buttons);
	}

	@Override
	public Icon getIcon(GuiBase gui)
	{
		return GUI_BACKGROUND;
	}
}