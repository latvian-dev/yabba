package com.latmod.yabba.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * @author LatvianModder
 */
public class GuiYabba extends GuiScreen
{
    public final ResourceLocation texture;
    public final int guiWidth, guiHeight;
    public int guiX, guiY;

    public GuiYabba(ResourceLocation tex, int w, int h)
    {
        texture = tex;
        guiWidth = w;
        guiHeight = h;
    }

    @Override
    public void initGui()
    {
        guiX = (width - guiWidth) / 2;
        guiY = (height - guiHeight) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float ticks)
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        mc.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiX, guiY, 0, 0, guiWidth, guiHeight);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if(keyCode == 1 || mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
        {
            mc.player.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}