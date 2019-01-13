package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.latmod.yabba.tile.Barrel;
import com.latmod.yabba.tile.BarrelContent;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.GL11;

/**
 * @author LatvianModder
 */
public class RenderBarrel<T extends TileBarrel, C extends BarrelContent> extends TileEntitySpecialRenderer<T>
{
	@Override
	public void render(T tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if (tile.isInvalid())
		{
			return;
		}

		Barrel barrel = tile.barrel;

		boolean hasIcon = hasIcon((C) tile.barrel.content);
		boolean isSneaking = ClientUtils.MC.player.isSneaking();
		RayTraceResult ray = ClientUtils.MC.objectMouseOver;
		boolean mouseOver = ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK && ray.getBlockPos().equals(tile.getPos());

		if (!hasIcon && !isSneaking)
		{
			return;
		}

		GlStateManager.color(1F, 1F, 1F, alpha);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.glNormal3f(0F, 1F, 0F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.rotate(tile.getRotationAngleY(), 0F, 1F, 0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		setLightmapDisabled(true);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.depthMask(true);
		GlStateManager.enableAlpha();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		if (mouseOver || YabbaClientConfig.general.always_display_data.get(barrel.alwaysDisplayData))
		{
			boolean isCreative = barrel.isCreative();
			boolean infinite = isCreative || barrel.getTier().infiniteCapacity();

			if (hasIcon)
			{
				float textDistance = barrel.getLook().model.textDistance / 16F;

				if (!infinite && !isSneaking && YabbaClientConfig.general.display_bar.get(barrel.displayBar))
				{
					GlStateManager.pushMatrix();
					GlStateManager.disableTexture2D();
					buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
					double b = 0.02D;
					double b2 = b * 2D;
					double bx = 0.0625D;
					double by = 0.0625D;
					double bw = 1D - bx * 2D;
					double bh = 0.15D;
					double filled = MathHelper.clamp(getFilled((C) tile.barrel.content), 0D, 1D);

					Color4I colBorder = YabbaClientConfig.bar_color.getBorderColor();
					Color4I colFree = YabbaClientConfig.bar_color.getFreeColor();
					Color4I colFilled = YabbaClientConfig.bar_color.getFilledColor();
					rect(buffer, bx, by, textDistance, b, bh, colBorder, alpha);
					rect(buffer, 1D - b - bx, by, textDistance, b, bh, colBorder, alpha);
					rect(buffer, bx + b, by, textDistance, bw - b2, b, colBorder, alpha);
					rect(buffer, bx + b, by + bh - b, textDistance, bw - b2, b, colBorder, alpha);
					rect(buffer, bx + b, by + b, textDistance, (bw - b2) * filled, bh - b2, colFree, alpha);
					rect(buffer, bx + b + (bw - b2) * filled, by + b, textDistance, (bw - b2) * (1D - filled), bh - b2, colFilled, alpha);
					tessellator.draw();
					GlStateManager.enableTexture2D();
					GlStateManager.popMatrix();
				}
				else
				{
					GlStateManager.pushMatrix();
					GlStateManager.translate(0.5F, 0.075F, textDistance);
					String s1 = barrel.content.getItemDisplayCount(isSneaking, isCreative, infinite);
					int sw = getFontRenderer().getStringWidth(s1);
					float f = 1F / (float) Math.max((sw + 10), 64);
					GlStateManager.scale(f, f, 1F);
					getFontRenderer().drawString(s1, -sw / 2, 0, 0xFFFFFFFF);
					GlStateManager.popMatrix();
				}

				GlStateManager.pushMatrix();
				GlStateManager.translate(0.5F, 0.80F, textDistance);
				boolean flag = getFontRenderer().getUnicodeFlag();
				getFontRenderer().setUnicodeFlag(true);
				String s2 = barrel.content.getItemDisplayName();
				int sw1 = getFontRenderer().getStringWidth(s2);
				float f1 = 1F / (float) Math.max((sw1 + 10), 64);
				GlStateManager.scale(f1, f1, 1F);
				getFontRenderer().drawString(s2, -sw1 / 2, 0, 0xFFFFFFFF);
				getFontRenderer().setUnicodeFlag(flag);
				GlStateManager.popMatrix();
			}
		}

		setLightmapDisabled(false);

		if (hasIcon)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5F, 0.5F, barrel.getLook().model.iconDistance / 16F);
			ClientUtils.MC.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			ClientUtils.MC.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1F, 1F, 1F, 1F);
			renderIcon((C) tile.barrel.content);
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableLighting();
			ClientUtils.MC.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			ClientUtils.MC.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

			GlStateManager.popMatrix();
		}

		GlStateManager.enableLighting();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();
	}

	private static void rect(BufferBuilder buffer, double x, double y, double z, double w, double h, Color4I col, float alpha)
	{
		int r = col.redi();
		int g = col.greeni();
		int b = col.bluei();
		int a = (int) (col.alphai() * alpha);
		buffer.pos(x, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
		buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
		buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
	}

	public double getFilled(C content)
	{
		return 0D;
	}

	public boolean hasIcon(C content)
	{
		return false;
	}

	public void renderIcon(C content)
	{
	}
}