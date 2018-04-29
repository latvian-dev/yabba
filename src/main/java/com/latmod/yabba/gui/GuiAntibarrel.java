package com.latmod.yabba.gui;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiContainerWrapper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftblib.lib.gui.Widget;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.ItemIcon;
import com.feed_the_beast.ftblib.lib.item.ItemEntry;
import com.feed_the_beast.ftblib.lib.item.ItemEntryWithCount;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.YabbaLang;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiAntibarrel extends GuiBase
{
	private static class ButtonItem extends Widget implements Comparable<ButtonItem>
	{
		private static final DecimalFormat FORMATTER_1 = new DecimalFormat("#0.00");
		private static final DecimalFormat FORMATTER_2 = new DecimalFormat("#0.0");

		public final ItemEntryWithCount entry;
		private List<String> tooltip;
		private Icon icon;
		private String countString;

		public ButtonItem(Panel panel, ItemEntryWithCount e)
		{
			super(panel);
			setSize(18, 18);
			entry = e;

			if (entry.isEmpty())
			{
				return;
			}

			ItemStack stack = entry.getStack(true);
			stack.setCount(1);

			if (entry.count >= 1000000000)
			{
				countString = FORMATTER_1.format(entry.count / 1000000000D) + "B";
			}
			else if (entry.count >= 100000000)
			{
				countString = entry.count / 1000000 + "M";
			}
			else if (entry.count >= 10000000)
			{
				countString = FORMATTER_2.format(entry.count / 1000000D) + "M";
			}
			else if (entry.count >= 1000000)
			{
				countString = FORMATTER_1.format(entry.count / 1000000D) + "M";
			}
			else if (entry.count >= 100000)
			{
				countString = entry.count / 1000 + "K";
			}
			else if (entry.count >= 10000)
			{
				countString = FORMATTER_2.format(entry.count / 1000D) + "K";
			}
			else if (entry.count >= 1000)
			{
				countString = FORMATTER_1.format(entry.count / 1000D) + "K";
			}
			else
			{
				countString = Integer.toString(entry.count);
			}

			icon = ItemIcon.getItemIcon(stack);
			tooltip = new ArrayList<>();

			for (String s : stack.getTooltip(ClientUtils.MC.player, ITooltipFlag.TooltipFlags.NORMAL))
			{
				if (tooltip.isEmpty())
				{
					if (entry.count > 1)
					{
						s += TextFormatting.GRAY + " x" + entry.count;
					}

					tooltip.add(stack.getRarity().rarityColor + s);
				}
				else
				{
					tooltip.add(TextFormatting.GRAY + s);
				}
			}

			if (tooltip.isEmpty())
			{
				tooltip = Collections.emptyList();
			}
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			if (!entry.isEmpty())
			{
				list.addAll(tooltip);
			}
		}

		@Override
		public boolean mousePressed(MouseButton button)
		{
			if (button.isLeft() && !entry.isEmpty() && isMouseOver() && ClientUtils.MC.player.inventory.getItemStack().isEmpty())
			{
				((GuiAntibarrel) getGui()).container.onClick(entry.entry, isShiftKeyDown());
				return true;
			}

			return false;
		}

		@Override
		public void draw()
		{
			int ax = getAX();
			int ay = getAY();
			getTheme().getSlot(getWidgetType()).draw(ax, ay, width, height);

			if (!entry.isEmpty())
			{
				pushFontUnicode(false);
				icon.draw(ax + 1, ay + 1, 16, 16);
				GlStateManager.pushMatrix();
				GlStateManager.translate(ax + 17 - getStringWidth(countString) / 2F, ay + 13, 1000);
				GlStateManager.scale(0.5F, 0.5F, 1F);
				drawString(countString, 0, 0, getTheme().getContentColor(getWidgetType()), SHADOW);
				GlStateManager.popMatrix();
				popFontUnicode();
			}
		}

		@Override
		public int compareTo(ButtonItem o)
		{
			int i1 = entry.isEmpty() ? Integer.MAX_VALUE : Item.getIdFromItem(entry.entry.item);
			int i2 = o.entry.isEmpty() ? Integer.MAX_VALUE : Item.getIdFromItem(o.entry.entry.item);
			return Integer.compare(i1, i2);
		}
	}

	public final ContainerAntibarrel container;
	public final Panel panel;
	public final PanelScrollBar scrollBar;
	public String title = "";

	public GuiAntibarrel(ContainerAntibarrel c)
	{
		container = c;

		panel = new Panel(this)
		{
			@Override
			public void addWidgets()
			{
				for (ItemEntryWithCount entry : container.tile.items.values())
				{
					add(new ButtonItem(this, entry));
				}

				int s = container.tile.items.size() % 8;

				if (s != 0)
				{
					for (int i = 0; i < 8 - s; i++)
					{
						add(new ButtonItem(this, ItemEntryWithCount.EMPTY));
					}

				}

				widgets.sort(null);
			}

			@Override
			public void alignWidgets()
			{
				for (int i = 0; i < widgets.size(); i++)
				{
					Widget widget = widgets.get(i);
					widget.setPos((i % 8) * 18, (i / 8) * 18);
				}

				scrollBar.setMaxValue(MathHelper.ceil(widgets.size() / 8F) * 18);
			}

			@Override
			public Icon getIcon()
			{
				return getTheme().getSlot(WidgetType.NORMAL);
			}

			@Override
			public boolean mousePressed(MouseButton button)
			{
				if (super.mousePressed(button))
				{
					return true;
				}
				else if (button.isLeft() && isMouseOver() && !ClientUtils.MC.player.inventory.getItemStack().isEmpty())
				{
					((GuiAntibarrel) getGui()).container.onClick(ItemEntry.EMPTY, isShiftKeyDown());
					return true;
				}

				return false;
			}
		};

		panel.setUnicode(true);
		panel.setPosAndSize(7, 7, 18 * 8, 72);

		scrollBar = new PanelScrollBar(this, panel);
		scrollBar.setPosAndSize(157, panel.posY, 12, panel.height);
		scrollBar.setCanAlwaysScroll(true);
		scrollBar.setScrollStep(18);
	}

	@Override
	public void addWidgets()
	{
		add(panel);
		add(scrollBar);

		int totalItems = 0;

		for (ItemEntryWithCount entry : container.tile.items.values())
		{
			totalItems += entry.count;
		}

		title = YabbaLang.ANTIBARREL_ITEMS.translate(totalItems, container.tile.items.size(), YabbaConfig.general.antibarrel_capacity);

		/*
		if (Loader.isModLoaded(OtherMods.FTBGUIDES))
		{
			panel.add(new ButtonSide(panel, GuiLang.INFO.translate(), GuiIcons.INFO)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					ClientUtils.execClientCommand("/ftbc open_guides /yabba/antibarrel");
				}
			});
		}*/
	}

	@Override
	public GuiScreen getWrapper()
	{
		return new GuiContainerWrapper(this, container);
	}

	@Override
	public void drawBackground()
	{
		super.drawBackground();
		drawString(title, getAX() + 4, getAY() - 10);
	}
}