package com.latmod.yabba.client;

import com.latmod.yabba.net.MessageRequestBarrelUpdate;
import com.latmod.yabba.net.YabbaNetHandler;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.GL11;

/**
 * Created by LatvianModder on 13.12.2016.
 */
public class RenderBarrel extends TileEntitySpecialRenderer<TileBarrel>
{
    @Override
    public void renderTileEntityAt(TileBarrel te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if(te.isInvalid())
        {
            return;
        }

        if(te.requestClientUpdate)
        {
            YabbaNetHandler.NET.sendToServer(new MessageRequestBarrelUpdate(te));
            te.requestClientUpdate = false;
        }

        ItemStack stack = te.barrel.getStackInSlot(0);

        if(stack == null)
        {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0F, 1F, 0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(180F, 0F, 0F, 1F);
        GlStateManager.rotate(te.getRotationAngle(), 0F, 1F, 0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        GlStateManager.color(1F, 1F, 1F, 1F);
        func_190053_a(true);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(true);

        if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mc.objectMouseOver.getBlockPos().equals(te.getPos()))
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5F, 0.075F, -0.005F);

            String s1 = te.getItemDisplayCount();
            int sw = getFontRenderer().getStringWidth(s1);
            float f = 1F / (float) Math.max((sw + 10), 64);
            GlStateManager.scale(f, f, f);
            getFontRenderer().drawString(s1, -sw / 2, 0, 0xFFFFFFFF);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5F, 0.80F, -0.005F);
            s1 = te.getItemDisplayName();
            sw = getFontRenderer().getStringWidth(s1);
            f = 1F / (float) Math.max((sw + 10), 64);
            GlStateManager.scale(f, f, f);
            getFontRenderer().drawString(s1, -sw / 2, 0, 0xFFFFFFFF);
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5F, 0.5F, 0.04F);
        GlStateManager.scale(0.4F, -0.4F, -0.036F);

        RenderItem itemRender = mc.getRenderItem();
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 1F);
        IBakedModel bakedmodel = itemRender.getItemModelWithOverrides(stack, null, mc.thePlayer);
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        itemRender.renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();

        func_190053_a(false);
    }
}