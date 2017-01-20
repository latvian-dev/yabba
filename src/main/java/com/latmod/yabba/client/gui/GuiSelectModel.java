package com.latmod.yabba.client.gui;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.client.YabbaClient;
import com.latmod.yabba.net.MessageSelectModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Collections;

/**
 * Created by LatvianModder on 05.01.2017.
 */
public class GuiSelectModel extends GuiYabba
{
    public static final GuiSelectModel INSTANCE = new GuiSelectModel();

    private GuiSelectModel()
    {
        super(new ResourceLocation(Yabba.MOD_ID, "textures/gui/model.png"), 178, 143);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float ticks)
    {
        super.drawScreen(mouseX, mouseY, ticks);

        int skin = (int) ((System.currentTimeMillis() / 1000L) % YabbaRegistry.ALL_SKINS.size());
        itemRender.zLevel = 200F;

        for(int i = 0; i < YabbaRegistry.ALL_MODELS.size(); i++)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(guiX + 3 + (i % 5) * 35, guiY + 3 + (i / 5) * 35, 32F);
            GlStateManager.scale(2F, 2F, 1F);
            GlStateManager.enableLighting();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
            GlStateManager.color(1F, 1F, 1F, 1F);
            itemRender.renderItemAndEffectIntoGUI(YabbaClient.STACKS_FOR_GUI[i][skin], 0, 0);
            GlStateManager.popMatrix();
        }

        itemRender.zLevel = 0F;

        if(mouseX >= guiX + 2 && mouseY >= guiY + 2)
        {
            for(int i = 0; i < YabbaRegistry.ALL_MODELS.size(); i++)
            {
                int bx = (mouseX - guiX - 2) / 35;
                int by = (mouseY - guiY - 2) / 35;
                int buttonOver = bx + by * 5;

                if(buttonOver < YabbaRegistry.ALL_MODELS.size())
                {
                    drawHoveringText(Collections.singletonList(I18n.format("yabba.model." + YabbaRegistry.ALL_MODELS.get(buttonOver).getName())), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if(mouseX >= guiX + 2 && mouseY >= guiY + 2)
        {
            int bx = (mouseX - guiX - 2) / 35;
            int by = (mouseY - guiY - 2) / 35;
            int buttonOver = (bx + by * 5) % 20;

            if(buttonOver < YabbaRegistry.ALL_MODELS.size())
            {
                new MessageSelectModel(YabbaRegistry.ALL_MODELS.get(buttonOver).getName()).sendToServer();
                mc.thePlayer.closeScreen();
            }
        }
    }
}