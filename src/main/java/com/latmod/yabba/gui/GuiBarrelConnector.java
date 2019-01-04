package com.latmod.yabba.gui;

import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.Theme;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.ItemIcon;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.net.MessageOpenBarrelGui;
import com.latmod.yabba.tile.ItemBarrel;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiBarrelConnector extends GuiButtonListBase
{
	private List<TileItemBarrel> barrels;

	public GuiBarrelConnector(ITextComponent t, Collection<BlockPos> c)
	{
		setTitle(t.getFormattedText());
		setHasSearchBox(true);
		barrels = new ArrayList<>();

		for (BlockPos p : c)
		{
			TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(p);

			if (tileEntity instanceof TileItemBarrel)
			{
				barrels.add((TileItemBarrel) tileEntity);
			}
		}

		barrels.sort((o1, o2) -> Integer.compare(((ItemBarrel) o2.barrel.content).count, ((ItemBarrel) o1.barrel.content).count));
	}

	@Override
	public void addButtons(Panel panel)
	{
		for (TileItemBarrel tile : barrels)
		{
			ItemStack barrelStack = new ItemStack(YabbaItems.ITEM_BARREL);
			tile.writeToItem(barrelStack);
			Icon icon2 = ItemIcon.getItemIcon(barrelStack);
			String title;
			Icon icon;
			ItemBarrel barrel = (ItemBarrel) tile.barrel.content;

			if (!barrel.type.isEmpty())
			{
				title = StringUtils.formatDouble(barrel.count, true) + "x " + TextFormatting.getTextWithoutFormattingCodes(barrel.type.getDisplayName());
				icon = ItemIcon.getItemIcon(ItemHandlerHelper.copyStackWithSize(barrel.type, 1));
			}
			else
			{
				title = "Empty"; //LANG
				icon = icon2;
			}

			SimpleTextButton button = new SimpleTextButton(panel, title, icon)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					new MessageOpenBarrelGui(tile.getPos()).sendToServer();
				}

				@Override
				public void addMouseOverText(List<String> list)
				{
					super.addMouseOverText(list);

					if (isShiftKeyDown())
					{
						BlockPos p = tile.getPos();
						list.add(TextFormatting.DARK_GRAY + "[" + p.getX() + ", " + p.getY() + ", " + p.getZ() + "]");
					}
				}

				@Override
				public boolean hasIcon()
				{
					return !(isShiftKeyDown() ? icon2 : icon).isEmpty();
				}

				@Override
				public void drawIcon(Theme theme, int x, int y, int w, int h)
				{
					(isShiftKeyDown() ? icon2 : icon).draw(x, y, w, h);

					if (tile.barrel.isLocked())
					{
						GlStateManager.pushMatrix();
						GlStateManager.translate(0, 0, 500);
						GuiIcons.LOCK.draw(x + w / 2, y, w / 2, h / 2);
						GlStateManager.popMatrix();
					}
				}
			};

			panel.add(button);
		}
	}
}