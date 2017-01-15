package com.latmod.yabba.client.gui;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.client.YabbaClient;
import com.latmod.yabba.net.MessageSelectSkin;
import com.latmod.yabba.net.YabbaNetHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by LatvianModder on 05.01.2017.
 */
public class GuiSelectSkin extends GuiScreen
{
    public static final GuiSelectSkin INSTANCE = new GuiSelectSkin();
    private static final ResourceLocation TEXTURE = new ResourceLocation(Yabba.MOD_ID, "textures/gui/skin.png");
    private static final int WIDTH = 193;
    private static final int HEIGHT = 155;

    private static abstract class Button
    {
        int posX, posY, width, height;
        String mouseOverText = "";

        private Button(int x, int y, int w, int h)
        {
            posX = x;
            posY = y;
            width = w;
            height = h;
        }

        boolean isMouseOver(int rmouseX, int rmouseY)
        {
            return rmouseX >= posX && rmouseY >= posY && rmouseX < posX + width && rmouseY < posY + height;
        }

        public abstract void onClicked(int button);

        public void drawButton(int rmouseX, int rmouseY)
        {
        }
    }

    private static final Button BUTTON_SELECT_SEARCH_BAR = new Button(3, 3, 187, 12)
    {
        @Override
        public void onClicked(int button)
        {
            INSTANCE.searchSelected = true;

            if(button != 0)
            {
                INSTANCE.searchBar = "";
                INSTANCE.updateVisibleSkins = true;
            }
        }
    };

    private static final Button BUTTON_PREV_PAGE = new Button(2, 135, 18, 18)
    {
        @Override
        public void onClicked(int button)
        {
            INSTANCE.page--;
            INSTANCE.updateVisibleSkins = true;
        }

        @Override
        public void drawButton(int rmouseX, int rmouseY)
        {
            if(isMouseOver(rmouseX, rmouseY))
            {
                INSTANCE.mc.getTextureManager().bindTexture(TEXTURE);
                INSTANCE.drawTexturedModalRect(posX, posY, 194, 38, width, height);
            }
        }
    };

    private static final Button BUTTON_NEXT_PAGE = new Button(154, 135, 18, 18)
    {
        @Override
        public void onClicked(int button)
        {
            INSTANCE.page++;
            INSTANCE.updateVisibleSkins = true;
        }

        @Override
        public void drawButton(int rmouseX, int rmouseY)
        {
            if(isMouseOver(rmouseX, rmouseY))
            {
                INSTANCE.mc.getTextureManager().bindTexture(TEXTURE);
                INSTANCE.drawTexturedModalRect(posX, posY, 194, 57, width, height);
            }
        }
    };

    private static final Button BUTTON_SAVE = new Button(173, 135, 18, 18)
    {
        @Override
        public void onClicked(int button)
        {
            YabbaNetHandler.NET.sendToServer(new MessageSelectSkin(YabbaRegistry.INSTANCE.getSkinId(INSTANCE.selectedSkin.skin.getName())));
            INSTANCE.mc.thePlayer.closeScreen();
        }

        @Override
        public void drawButton(int rmouseX, int rmouseY)
        {
            if(isMouseOver(rmouseX, rmouseY))
            {
                INSTANCE.mc.getTextureManager().bindTexture(TEXTURE);
                INSTANCE.drawTexturedModalRect(posX, posY, 194, 19, width, height);
            }
        }
    };

    private static class Skin extends Button
    {
        private final int index;
        private final IBarrelSkin skin;
        private final String spriteName;

        private Skin(int i, IBarrelSkin s)
        {
            super(0, 0, 18, 18);
            index = i;
            skin = s;
            mouseOverText = s.getDisplayName();
            spriteName = skin.getTextures().getTexture(EnumFacing.NORTH).toString();
        }

        @Override
        public void onClicked(int button)
        {
            INSTANCE.selectedSkin = this;
        }

        @Override
        public void drawButton(int rmouseX, int rmouseY)
        {
            if(INSTANCE.selectedSkin == this || isMouseOver(rmouseX, rmouseY))
            {
                INSTANCE.mc.getTextureManager().bindTexture(TEXTURE);
                INSTANCE.drawTexturedModalRect(posX, posY, 194, 0, width, height);
                INSTANCE.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            }

            TextureAtlasSprite sprite = INSTANCE.mc.getTextureMapBlocks().getAtlasSprite(spriteName);
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(posX + 1D, posY + 17D, 0D).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            buffer.pos(posX + 17D, posY + 17D, 0D).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            buffer.pos(posX + 17D, posY + 1D, 0D).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            buffer.pos(posX + 1D, posY + 1D, 0D).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            tessellator.draw();
        }
    }

    private static final List<Skin> ALL_SKINS = new ArrayList<>();

    public static void initSkins()
    {
        ALL_SKINS.clear();

        for(int i = 0; i < YabbaRegistry.ALL_SKINS.size(); i++)
        {
            ALL_SKINS.add(new Skin(i, YabbaRegistry.ALL_SKINS.get(i)));
        }
    }

    private int guiX, guiY;
    private List<Skin> visibleSkins = new ArrayList<>();
    private String searchBar = "";
    private boolean searchSelected = false, updateVisibleSkins = true;
    private Skin selectedSkin;
    private int page;
    private List<Button> buttons = new ArrayList<>();

    private void updateVisibleSkins()
    {
        if(!buttons.isEmpty())
        {
            buttons.removeAll(visibleSkins);
        }

        visibleSkins.clear();

        List<Skin> matchingSkins = searchBar.isEmpty() ? ALL_SKINS : new ArrayList<>();

        if(!searchBar.isEmpty())
        {
            String searchBar1 = searchBar.toLowerCase(Locale.ENGLISH).replace(" ", "");

            for(Skin skin : ALL_SKINS)
            {
                if(skin.mouseOverText.toLowerCase(Locale.ENGLISH).replace(" ", "").contains(searchBar1))
                {
                    matchingSkins.add(skin);
                }
            }
        }

        if(!matchingSkins.isEmpty())
        {
            int maxPages = MathHelper.ceiling_float_int(matchingSkins.size() / 60F);

            page %= maxPages;
            if(page < 0)
            {
                page = maxPages - 1;
            }

            int size = Math.min(60, matchingSkins.size());
            for(int i = 0; i < size; i++)
            {
                if(i + page * 60 >= matchingSkins.size())
                {
                    break;
                }

                Skin skin = matchingSkins.get(i + page * 60);
                skin.posX = 2 + (i % 10) * 19;
                skin.posY = 19 + (i / 10) * 19;
                visibleSkins.add(skin);
            }

            if(buttons.isEmpty())
            {
                INSTANCE.selectedSkin = visibleSkins.get(0);
            }

            buttons.addAll(visibleSkins);
        }
    }

    @Override
    public void initGui()
    {
        guiX = (width - WIDTH) / 2;
        guiY = (height - HEIGHT) / 2;
        buttons.clear();
        updateVisibleSkins();

        buttons.add(BUTTON_SAVE);
        buttons.add(BUTTON_PREV_PAGE);
        buttons.add(BUTTON_NEXT_PAGE);
        buttons.add(BUTTON_SELECT_SEARCH_BAR);

        BUTTON_SAVE.mouseOverText = "OK";
        BUTTON_PREV_PAGE.mouseOverText = I18n.format("createWorld.customize.custom.prev");
        BUTTON_NEXT_PAGE.mouseOverText = I18n.format("createWorld.customize.custom.next");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float ticks)
    {
        int mouseWheel = Mouse.getDWheel();

        if(mouseWheel != 0)
        {
            if(mouseWheel < 0)
            {
                page++;
            }
            else
            {
                page--;
            }

            updateVisibleSkins = true;
        }

        if(updateVisibleSkins)
        {
            updateVisibleSkins();
            updateVisibleSkins = false;
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiX, guiY, 0, 0, WIDTH, HEIGHT);

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiX, guiY, 0F);
        int rmouseX = mouseX - guiX;
        int rmouseY = mouseY - guiY;

        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        for(Button button : buttons)
        {
            button.drawButton(rmouseX, rmouseY);
        }

        int models = Math.min(7, YabbaRegistry.ALL_MODELS.size());

        for(int i = 0; i < models; i++)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(22 + i * 19, 136, 32F);
            GlStateManager.enableLighting();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
            GlStateManager.color(1F, 1F, 1F, 1F);
            itemRender.renderItemAndEffectIntoGUI(YabbaClient.STACKS_FOR_GUI[i][selectedSkin.index], 0, 0);
            GlStateManager.popMatrix();
        }

        String searchBarText = searchBar;

        if(searchSelected && (System.currentTimeMillis() % 800L >= 400L))
        {
            searchBarText += '_';
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        fontRendererObj.drawString(searchBarText, 6, 5, 0xFFFFFFFF);

        for(Button button : buttons)
        {
            if(!button.mouseOverText.isEmpty() && button.isMouseOver(rmouseX, rmouseY))
            {
                drawHoveringText(Collections.singletonList(button.mouseOverText), rmouseX, rmouseY);
            }
        }

        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        searchSelected = false;
        int rmouseX = mouseX - guiX;
        int rmouseY = mouseY - guiY;

        for(Button button : buttons)
        {
            if(button.isMouseOver(rmouseX, rmouseY))
            {
                button.onClicked(mouseButton);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if(searchSelected)
        {
            switch(keyCode)
            {
                case Keyboard.KEY_ESCAPE:
                case Keyboard.KEY_RETURN:
                case Keyboard.KEY_TAB:
                    searchSelected = false;
                    break;
                case Keyboard.KEY_BACK:
                    if(!searchBar.isEmpty())
                    {
                        if(isShiftKeyDown())
                        {
                            searchBar = "";
                        }
                        else
                        {
                            searchBar = searchBar.substring(0, searchBar.length() - 1);
                        }
                        updateVisibleSkins = true;
                        break;
                    }
                    break;
                default:
                    if(typedChar >= 'a' && typedChar <= 'z' || typedChar >= 'A' && typedChar <= 'Z' || typedChar >= '0' && typedChar <= '1' || " .,/-_".indexOf(typedChar) != -1)
                    {
                        searchBar += typedChar;
                        updateVisibleSkins = true;
                    }
                    break;
            }
        }
        else if(keyCode == Keyboard.KEY_ESCAPE || mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
        {
            mc.thePlayer.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}