package com.latmod.yabba.client;

import com.latmod.yabba.tile.ItemBarrel;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.client.ForgeHooksClient;

/**
 * @author LatvianModder
 */
public class RenderItemBarrel extends RenderBarrel<TileItemBarrel, ItemBarrel>
{
	@Override
	public double getFilled(ItemBarrel b)
	{
		if (b.count <= 0)
		{
			return 0D;
		}

		return b.count / (double) b.getMaxItems(b.type);
	}

	@Override
	public boolean hasIcon(ItemBarrel b)
	{
		return (b.count > 0 || b.barrel.isLocked()) && !b.type.isEmpty();
	}

	@Override
	public void renderIcon(ItemBarrel b)
	{
		IBakedModel bakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(b.type, b.barrel.block.getBarrelTileEntity().getWorld(), Minecraft.getMinecraft().player);
		GlStateManager.scale(0.4F, -0.4F, -0.02F);
		bakedmodel = ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
		Minecraft.getMinecraft().getRenderItem().renderItem(b.type, bakedmodel);
	}
}