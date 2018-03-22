package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.client.ForgeHooksClient;

/**
 * @author LatvianModder
 */
public class RenderItemBarrel extends RenderBarrel<TileItemBarrel>
{
	@Override
	public double getFilled(TileItemBarrel barrel)
	{
		if (barrel.getItemCount() <= 0)
		{
			return 0D;
		}

		return barrel.getItemCount() / (double) barrel.getMaxItems(barrel.getStoredItemType());
	}

	@Override
	public boolean hasIcon(TileItemBarrel barrel)
	{
		return (barrel.getItemCount() > 0 || barrel.isLocked()) && !barrel.getStoredItemType().isEmpty();
	}

	@Override
	public void renderIcon(TileItemBarrel barrel)
	{
		IBakedModel bakedmodel = ClientUtils.MC.getRenderItem().getItemModelWithOverrides(barrel.getStoredItemType(), barrel.getWorld(), ClientUtils.MC.player);
		GlStateManager.scale(0.4F, -0.4F, -0.02F);
		bakedmodel = ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
		ClientUtils.MC.getRenderItem().renderItem(barrel.getStoredItemType(), bakedmodel);
	}
}