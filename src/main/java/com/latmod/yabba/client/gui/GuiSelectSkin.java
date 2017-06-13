package com.latmod.yabba.client.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.TextBox;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.client.YabbaClient;
import com.latmod.yabba.net.MessageSelectSkin;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSelectSkin extends GuiBase
{
	public static final IDrawableObject PURPLE_BACKGROUND = new TexturelessRectangle(new Color4I(false, 0x228060FF)).setLineColor(Color4I.BLACK);
	public static final IDrawableObject BUTTON_GREEN = new TexturelessRectangle(Color4I.NONE).setLineColor(new Color4I(false, 0xFF007F0E));

	private class Skin extends Button
	{
		private final IBarrelSkin skin;
		private final String spriteName;
		private final String searchText;

		private Skin(IBarrelSkin s)
		{
			super(0, 0, 18, 18);
			skin = s;
			String t = StringUtils.translate(s.getUnlocalizedName());
			setTitle(t);
			spriteName = String.valueOf(skin.getTextures().getTexture(EnumFacing.NORTH));
			searchText = t.replace(" ", "").toLowerCase();
		}

		@Override
		public void onClicked(GuiBase gui, IMouseButton button)
		{
			new MessageSelectSkin(skin.getName()).sendToServer();
			gui.closeGui();
		}

		@Override
		public void renderWidget(GuiBase gui)
		{
			int ax = getAX();
			int ay = getAY();
			(gui.isMouseOver(this) ? BUTTON_GREEN : GuiSelectModel.BUTTON_BACKGROUND).draw(ax, ay, width, height, Color4I.NONE);

			gui.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(spriteName);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			buffer.pos(ax + 1D, ay + 17D, 0D).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
			buffer.pos(ax + 1D + 16D, ay + 17D, 0D).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
			buffer.pos(ax + 1D + 16D, ay + 1D, 0D).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
			buffer.pos(ax + 1D, ay + 1D, 0D).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
			tessellator.draw();
		}
	}

	private final TextBox searchBar;
	private final Panel skinsPanel;
	private final PanelScrollBar scrollBar;
	private final List<Skin> allSkins = new ArrayList<>();

	public GuiSelectSkin()
	{
		super(193, 134);

		searchBar = new TextBox(2, 2, 189, 14)
		{
			@Override
			public void onTextChanged(GuiBase gui)
			{
				skinsPanel.refreshWidgets();
			}
		};

		searchBar.background = PURPLE_BACKGROUND;

		skinsPanel = new Panel(2, 18, 171, 115)
		{
			@Override
			public void addWidgets()
			{
				scrollBar.setValue(GuiSelectSkin.this, 0D);
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
						if (skin.searchText.contains(searchBar1))
						{
							add(skin);
						}
					}
				}

				updateWidgetPositions();
			}

			@Override
			public void updateWidgetPositions()
			{
				int x = 0;
				int y = 1;

				for (Widget w : widgets)
				{
					w.posX = x;
					w.posY = y;

					x += w.width + 1;

					if (x > width)
					{
						x = 0;
						y += w.height + 1;
					}
				}

				scrollBar.setElementSize(widgets.isEmpty() ? 0 : widgets.get(widgets.size() - 1).posY + 19);
				scrollBar.setSrollStepFromOneElementSize(19);
			}
		};

		skinsPanel.addFlags(Panel.FLAG_DEFAULTS);

		scrollBar = new PanelScrollBar(173, 19, 18, 113, 12, skinsPanel)
		{
			@Override
			public boolean shouldRender(GuiBase gui)
			{
				return true;
			}
		};

		scrollBar.background = PURPLE_BACKGROUND;
		scrollBar.slider = PURPLE_BACKGROUND;

		for (IBarrelSkin s : YabbaClient.ALL_SKINS)
		{
			allSkins.add(new Skin(s));
		}
	}

	@Override
	public void addWidgets()
	{
		addAll(searchBar, skinsPanel, scrollBar);
	}

	@Override
	public IDrawableObject getIcon(GuiBase gui)
	{
		return GuiSelectModel.GUI_BACKGROUND;
	}

	@Override
	public void drawBackground()
	{
		super.drawBackground();
		GuiHelper.drawBlankRect(posX, posY + 17, width, 1, Color4I.BLACK);
	}
}