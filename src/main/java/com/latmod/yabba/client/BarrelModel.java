package com.latmod.yabba.client;

import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.function.Function;

/**
 * @author LatvianModder
 */
public class BarrelModel implements IModel
{
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return EnumBarrelModel.ALL_MODEL_LOCATIONS;
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return YabbaClient.TEXTURES;
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		return new BakedBarrelBlockModel(format, bakedTextureGetter);
	}
}