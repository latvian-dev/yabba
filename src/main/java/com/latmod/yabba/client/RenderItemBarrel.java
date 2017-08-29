package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.latmod.yabba.tile.TileItemBarrel;
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
		return barrel.itemCount / (double) barrel.getMaxItems(barrel.storedItem);
	}

	@Override
	public boolean hasIcon(TileItemBarrel barrel)
	{
		return (barrel.itemCount > 0 || barrel.isLocked.getBoolean()) && !barrel.storedItem.isEmpty();
	}

	@Override
	public void renderIcon(TileItemBarrel barrel)
	{
		IBakedModel bakedmodel = ClientUtils.MC.getRenderItem().getItemModelWithOverrides(barrel.storedItem, null, ClientUtils.MC.player);
		bakedmodel = ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
		ClientUtils.MC.getRenderItem().renderItem(barrel.storedItem, bakedmodel);
	}
}