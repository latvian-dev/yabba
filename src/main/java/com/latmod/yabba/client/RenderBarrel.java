package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.client.CachedVertexData;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.tile.TileAdvancedBarrelBase;
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
public class RenderBarrel<T extends TileAdvancedBarrelBase> extends TileEntitySpecialRenderer<T>
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

		if (destroyStage >= 0)
		{
			ClientUtils.MC.getTextureManager().bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.glNormal3f(0F, 1F, 0F);
			CachedVertexData data = new CachedVertexData(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			data.cube(barrel.getAABB(barrel.getBlockState()));
			data.draw(Tessellator.getInstance(), Tessellator.getInstance().getBuffer());
			GlStateManager.popMatrix();
		}
		else
		{
			GlStateManager.color(1F, 1F, 1F, alpha);
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
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		setLightmapDisabled(true);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.depthMask(true);
		GlStateManager.enableAlpha();

		BarrelModel model = barrel.getLook().getModel();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		if (mouseOver || YabbaClientConfig.general.always_display_data.get(barrel.alwaysDisplayData.getBoolean()))
		{
			boolean isCreative = barrel.getTier().creative();
			float textDistance = model.textDistance;
			boolean infinite = isCreative || barrel.getTier().infiniteCapacity();

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
					String s1 = barrel.getItemDisplayCount(isSneaking, isCreative, infinite);
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
				String s2 = barrel.getItemDisplayName();
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
			GlStateManager.translate(0.5F, 0.5F, model.iconDistance);
			ClientUtils.MC.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			ClientUtils.MC.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1F, 1F, 1F, 1F);
			renderIcon(barrel);
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