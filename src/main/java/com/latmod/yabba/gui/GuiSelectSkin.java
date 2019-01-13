package com.latmod.yabba.gui;

import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftblib.lib.gui.TextBox;
import com.feed_the_beast.ftblib.lib.gui.Theme;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.client.YabbaClient;
import com.latmod.yabba.net.MessageSelectSkin;
import net.minecraft.client.Minecraft;
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

		private Skin(Panel panel, BarrelSkin s)
		{
			super(panel);
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

			if (Minecraft.getMinecraft().gameSettings.advancedItemTooltips)
			{
				list.add(TextFormatting.DARK_GRAY + skin.id);

				if (skin.state != BlockUtils.AIR_STATE)
				{
					String s = BlockUtils.getNameFromState(skin.state);

					if (!s.equals(skin.id))
					{
						list.add(TextFormatting.DARK_GRAY + s);
					}
				}
			}
		}

		@Override
		public void drawBackground(Theme theme, int x, int y, int w, int h)
		{
			(getWidgetType() == WidgetType.MOUSE_OVER ? Color4I.LIGHT_GREEN.withAlpha(70) : Color4I.BLACK.withAlpha(50)).draw(x, y, w, h);
		}

		@Override
		public void drawIcon(Theme theme, int x, int y, int w, int h)
		{
			skin.icon.draw(x, y, w, h);
		}
	}

	private final TextBox searchBar;
	private final Panel skinsPanel;
	private final PanelScrollBar scrollBar;
	private final List<Skin> allSkins = new ArrayList<>();

	public GuiSelectSkin()
	{
		setSize(213, 152);

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
				scrollBar.setValue(0);
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
					widgets.get(i).setPos(1 + (i % 8) * 21, 1 + (i / 8) * 21);
				}

				scrollBar.setMaxValue(widgets.isEmpty() ? 0 : widgets.get(widgets.size() - 1).posY + 21);
			}

			@Override
			public void drawBackground(Theme theme, int x, int y, int w, int h)
			{
				theme.drawPanelBackground(x, y, w, h);
			}
		};

		skinsPanel.setPosAndSize(9, 29, 169, 113);

		scrollBar = new PanelScrollBar(this, skinsPanel);
		scrollBar.setCanAlwaysScroll(true);
		scrollBar.setCanAlwaysScrollPlane(true);
		scrollBar.setPosAndSize(186, 28, 18, 115);
		scrollBar.setScrollStep(21);

		for (BarrelSkin s : YabbaClient.ALL_SKINS)
		{
			allSkins.add(new Skin(skinsPanel, s));
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