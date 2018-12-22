package com.latmod.yabba.gui;

import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.Theme;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.latmod.yabba.net.MessageBarrelConnector;
import com.latmod.yabba.net.MessageOpenBarrelGui;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiBarrelConnector extends GuiButtonListBase
{
	private Collection<MessageBarrelConnector.BarrelInst> barrels;

	public GuiBarrelConnector(ITextComponent t, Collection<MessageBarrelConnector.BarrelInst> c)
	{
		setTitle(t.getFormattedText());
		setHasSearchBox(true);
		barrels = c;
	}

	@Override
	public void addButtons(Panel panel)
	{
		Theme theme = getGui().getTheme();

		for (MessageBarrelConnector.BarrelInst inst : barrels)
		{
			final String title2 = inst.title2.getFormattedText();
			SimpleTextButton button = new SimpleTextButton(panel, inst.title.getFormattedText(), inst.icon)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					new MessageOpenBarrelGui(inst.pos).sendToServer();
				}

				@Override
				public void addMouseOverText(List<String> list)
				{
					super.addMouseOverText(list);

					if (isShiftKeyDown())
					{
						list.add(TextFormatting.DARK_GRAY + "[" + inst.pos.getX() + ", " + inst.pos.getY() + ", " + inst.pos.getZ() + "]");
					}
				}

				@Override
				public String getTitle()
				{
					return isShiftKeyDown() ? title2 : super.getTitle();
				}

				@Override
				public boolean hasIcon()
				{
					return !(isShiftKeyDown() ? inst.icon2 : inst.icon).isEmpty();
				}

				@Override
				public void drawIcon(Theme theme, int x, int y, int w, int h)
				{
					(isShiftKeyDown() ? inst.icon2 : inst.icon).draw(x, y, w, h);
				}
			};
			button.setWidth(Math.max(button.width, theme.getStringWidth(title2) + 28));
			panel.add(button);
		}
	}
}