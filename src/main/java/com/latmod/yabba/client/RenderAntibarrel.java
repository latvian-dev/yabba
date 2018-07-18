package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.latmod.yabba.tile.TileAntibarrel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.RayTraceResult;

/**
 * @author LatvianModder
 */
public class RenderAntibarrel extends TileEntitySpecialRenderer<TileAntibarrel>
{
	@Override
	public void render(TileAntibarrel barrel, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		RayTraceResult ray = ClientUtils.MC.objectMouseOver;
		boolean mouseOver = ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK && ray.getBlockPos().equals(barrel.getPos());

		if (!mouseOver || ray.sideHit.getAxis().isVertical())
		{
			return;
		}

		GlStateManager.color(1F, 1F, 1F, alpha);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.glNormal3f(0F, 1F, 0F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.rotate(ray.sideHit.getHorizontalAngle() + 180F, 0F, 1F, 0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		setLightmapDisabled(true);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.depthMask(true);
		GlStateManager.enableAlpha();

		boolean flag = getFontRenderer().getUnicodeFlag();
		getFontRenderer().setUnicodeFlag(true);

		if (ClientUtils.MC.player.isSneaking())
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5F, 0.4F, -0.05F);
			String s = I18n.format("tile.yabba.antibarrel.pick_up");
			int sw = getFontRenderer().getStringWidth(s);
			float f = 1F / (float) Math.max((sw + 10), 64);
			GlStateManager.scale(f, f, 1F);
			getFontRenderer().drawString(s, -sw / 2, 0, 0xFFFFFFFF);
			GlStateManager.popMatrix();
		}
		else
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5F, 0.3F, -0.05F);
			String s1 = I18n.format("tile.yabba.antibarrel.items_only", barrel.contents.getTotalItemCount());
			int sw1 = getFontRenderer().getStringWidth(s1);
			float f1 = 1F / (float) Math.max((sw1 + 10), 64);
			GlStateManager.scale(f1, f1, 1F);
			getFontRenderer().drawString(s1, -sw1 / 2, 0, 0xFFFFFFFF);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5F, 0.5F, -0.05F);
			String s2 = I18n.format("tile.yabba.antibarrel.types", barrel.contents.items.size());
			int sw2 = getFontRenderer().getStringWidth(s2);
			float f2 = 1F / (float) Math.max((sw2 + 10), 64);
			GlStateManager.scale(f2, f2, 1F);
			getFontRenderer().drawString(s2, -sw2 / 2, 0, 0xFFFFFFFF);
			GlStateManager.popMatrix();
		}

		getFontRenderer().setUnicodeFlag(flag);

		setLightmapDisabled(false);
		GlStateManager.enableLighting();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();
	}
}