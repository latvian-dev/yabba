package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.CachedVertexData;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.tile.TileBarrelBase;
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
public class RenderBarrel<T extends TileBarrelBase> extends TileEntitySpecialRenderer<T>
{
	private static final CachedVertexData ICON_SETTINGS[] = new CachedVertexData[5];

	static
	{
		for (int i = 0; i < 5; i++)
		{
			ICON_SETTINGS[i] = new CachedVertexData(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			ICON_SETTINGS[i].color.set(i == 4 ? Color4I.rgb(0xFF00DC) : Tier.NAME_MAP.get(i).color);
			ICON_SETTINGS[i].pos(-0.5D, -0.5D, 0D).tex(0, 0);
			ICON_SETTINGS[i].pos(-0.5D, +0.5D, 0D).tex(0, 1);
			ICON_SETTINGS[i].pos(+0.5D, +0.5D, 0D).tex(1, 1);
			ICON_SETTINGS[i].pos(+0.5D, -0.5D, 0D).tex(1, 0);
		}
	}

	@Override
	public void render(T barrel, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if (barrel.isInvalid())
		{
			return;
		}

		boolean hasIcon = hasIcon(barrel);
		boolean isSneaking = ClientUtils.MC.player.isSneaking();
		RayTraceResult ray = ClientUtils.MC.objectMouseOver;
		boolean mouseOver = ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK && ray.getBlockPos().equals(barrel.getPos());

		if (!hasIcon && !isSneaking)
		{
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.glNormal3f(0F, 1F, 0F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.rotate(barrel.getRotationAngleY(), 0F, 1F, 0F);
		GlStateManager.rotate(barrel.getRotationAngleX(), 1F, 0F, 0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		GlStateManager.color(1F, 1F, 1F, 1F);
		setLightmapDisabled(true);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.depthMask(true);

		BarrelModel model = YabbaClient.getModel(barrel.model);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		if (mouseOver || YabbaClientConfig.general.always_display_data.get(barrel.alwaysDisplayData.getBoolean()))
		{
			boolean isCreative = barrel.tier.creative();
			float textDistance = model.textDistance;
			boolean infinite = isCreative || barrel.tier.infiniteCapacity();

			if (hasIcon)
			{
				if (!infinite && !isSneaking && YabbaClientConfig.general.display_bar.get(barrel.displayBar.getBoolean()))
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
					double filled = MathHelper.clamp(getFilled(barrel), 0D, 1D);

					Color4I colBorder = YabbaClientConfig.bar_color.getBorderColor();
					Color4I colFree = YabbaClientConfig.bar_color.getFreeColor();
					Color4I colFilled = YabbaClientConfig.bar_color.getFilledColor();
					rect(buffer, bx, by, textDistance, b, bh, colBorder);
					rect(buffer, 1D - b - bx, by, textDistance, b, bh, colBorder);
					rect(buffer, bx + b, by, textDistance, bw - b2, b, colBorder);
					rect(buffer, bx + b, by + bh - b, textDistance, bw - b2, b, colBorder);
					rect(buffer, bx + b, by + b, textDistance, (bw - b2) * filled, bh - b2, colFree);
					rect(buffer, bx + b + (bw - b2) * filled, by + b, textDistance, (bw - b2) * (1D - filled), bh - b2, colFilled);
					tessellator.draw();
					GlStateManager.enableTexture2D();
					GlStateManager.popMatrix();
				}
				else
				{
					GlStateManager.pushMatrix();
					GlStateManager.translate(0.5F, 0.075F, textDistance);
					String s1 = barrel.getItemDisplayCount(isSneaking, isCreative, infinite);
					int sw = getFontRenderer().getStringWidth(s1);
					float f = 1F / (float) Math.max((sw + 10), 64);
					GlStateManager.scale(f, f, 1F);
					getFontRenderer().drawString(s1, -sw / 2, 0, 0xFFFFFFFF);
					GlStateManager.popMatrix();
				}

				GlStateManager.pushMatrix();
				GlStateManager.translate(0.5F, 0.80F, textDistance);
				String s2 = barrel.getItemDisplayName();
				int sw1 = getFontRenderer().getStringWidth(s2);
				float f1 = 1F / (float) Math.max((sw1 + 10), 64);
				GlStateManager.scale(f1, f1, 1F);
				getFontRenderer().drawString(s2, -sw1 / 2, 0, 0xFFFFFFFF);
				GlStateManager.popMatrix();
			}
		}

		if (hasIcon)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5F, 0.5F, model.iconDistance);
			ClientUtils.MC.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			ClientUtils.MC.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1F, 1F, 1F, 1F);
			renderIcon(barrel);
			GlStateManager.disableAlpha();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableLighting();
			ClientUtils.MC.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			ClientUtils.MC.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

			GlStateManager.popMatrix();
		}

		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();

		setLightmapDisabled(false);
	}

	private static void rect(BufferBuilder buffer, double x, double y, double z, double w, double h, Color4I col)
	{
		int r = col.redi();
		int g = col.greeni();
		int b = col.bluei();
		int a = col.alphai();
		buffer.pos(x, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
		buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
		buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
	}

	public double getFilled(T barrel)
	{
		return 0D;
	}

	public boolean hasIcon(T barrel)
	{
		return false;
	}

	public void renderIcon(T barrel)
	{
	}
}