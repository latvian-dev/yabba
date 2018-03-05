package com.latmod.yabba.client.gui;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftblib.lib.gui.TextBox;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.client.YabbaClient;
import com.latmod.yabba.net.MessageSelectSkin;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSelectSkin extends GuiBase
{
	private class Skin extends Button
	{
		private final BarrelSkin skin;
		private final String searchText;

		private Skin(GuiBase gui, BarrelSkin s)
		{
			super(gui);
			setSize(20, 20);
			skin = s;
			String t = s.toString();
			setTitle(t);
			searchText = t.replace(" ", "").toLowerCase();
		}

		@Override
		public void onClicked(MouseButton button)
		{
			if (skin != null)
			{
				new MessageSelectSkin(skin.id).sendToServer();
				getGui().closeGui();
			}
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			super.addMouseOverText(list);

			if (ClientUtils.MC.gameSettings.advancedItemTooltips)
			{
				list.add(TextFormatting.DARK_GRAY + skin.id);

				if (skin.state != CommonUtils.AIR_STATE)
				{
					String s = CommonUtils.getNameFromState(skin.state);

					if (!s.equals(skin.id))
					{
						list.add(TextFormatting.DARK_GRAY + s);
					}
				}
			}
		}

		@Override
		public void draw()
		{
			int ax = getAX();
			int ay = getAY();

			getIcon().draw(ax, ay, width, height);
			skin.icon.draw(ax + 2, ay + 2, 16, 16);
		}
	}

	private final TextBox searchBar;
	private final Panel skinsPanel;
	private final PanelScrollBar scrollBar;
	private final List<Skin> allSkins = new ArrayList<>();

	public GuiSelectSkin()
	{
		setSize(211, 150);

		searchBar = new TextBox(this)
		{
			@Override
			public void onTextChanged()
			{
				skinsPanel.refreshWidgets();
			}
		};

		searchBar.setPosAndSize(8, 8, 194, 14);

		skinsPanel = new Panel(this)
		{
			@Override
			public void addWidgets()
			{
				scrollBar.setValue(0D);
				String search = searchBar.getText();

				if (search.isEmpty())
				{
					for (Skin s : allSkins)
					{
						add(s);
					}
				}
				else
				{
					String searchBar1 = search.replace(" ", "").toLowerCase();

					for (Skin skin : allSkins)
					{
						if (!skin.searchText.isEmpty() && skin.searchText.contains(searchBar1))
						{
							add(skin);
						}
					}
				}
			}

			@Override
			public void alignWidgets()
			{
				for (int i = 0; i < widgets.size(); i++)
				{
					widgets.get(i).setPos((i % 8) * 21, (i / 8) * 21);
				}

				scrollBar.setElementSize(widgets.isEmpty() ? 0 : widgets.get(widgets.size() - 1).posY + 20);
				scrollBar.setSrollStepFromOneElementSize(19);
			}

			@Override
			public Icon getIcon()
			{
				return getTheme().getPanelBackground();
			}
		};

		skinsPanel.setPosAndSize(9, 29, 167, 111);
		skinsPanel.addFlags(Panel.DEFAULTS);

		scrollBar = new PanelScrollBar(this, skinsPanel)
		{
			@Override
			public boolean shouldDraw()
			{
				return true;
			}
		};

		scrollBar.setPosAndSize(184, 28, 18, 113);

		for (BarrelSkin s : YabbaClient.ALL_SKINS)
		{
			allSkins.add(new Skin(this, s));
		}
	}

	@Override
	public void addWidgets()
	{
		add(searchBar);
		add(skinsPanel);
		add(scrollBar);
	}

	@Override
	public void alignWidgets()
	{
		skinsPanel.alignWidgets();
	}
}