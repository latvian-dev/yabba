package com.latmod.yabba.gui;

import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.latmod.yabba.client.BarrelModel;
import com.latmod.yabba.net.MessageSelectModel;
import com.latmod.yabba.util.EnumBarrelModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSelectModel extends GuiBase
{
	private class ButtonModel extends Button
	{
		private final BarrelModel model;

		public ButtonModel(Panel panel, int i)
		{
			super(panel);
			setPosAndSize(8 + (i % 4) * 39, 8 + (i / 4) * 39, 38, 38);
			model = i >= EnumBarrelModel.NAME_MAP.size() ? null : EnumBarrelModel.NAME_MAP.get(i).getModel();

			if (model != null)
			{
				setTitle(model.toString());
			}
			else
			{
				setIcon(getTheme().getButton(WidgetType.DISABLED));
			}
		}

		@Override
		public void onClicked(MouseButton button)
		{
			if (model != null)
			{
				GuiHelper.playClickSound();
				new MessageSelectModel(model.id).sendToServer();
				getGui().closeGui();
			}
		}

		@Override
		public void draw()
		{
			int ax = getAX();
			int ay = getAY();
			getIcon().draw(ax, ay, width, height);

			if (model != null)
			{
				model.icon.draw(ax + 3, ay + 3, 32, 32);
			}
		}
	}

	private final List<ButtonModel> buttons;

	public GuiSelectModel()
	{
		int cols = 4;
		int rows = 3;

		setSize(19 + cols * 38, 19 + rows * 38);

		buttons = new ArrayList<>();

		for (int i = 0; i < cols * rows; i++)
		{
			buttons.add(new ButtonModel(this, i));
		}
	}

	@Override
	public void addWidgets()
	{
		addAll(buttons);
	}
}