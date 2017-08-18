package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.TextureSet;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.api.BarrelSkin;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author LatvianModder
 */
public class BarrelModelLoader implements IModel, ICustomModelLoader
{
	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation(Yabba.MOD_ID + ":barrel#normal");

	public enum StateMapper implements IStateMapper
	{
		INSTANCE;

		@Override
		public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
		{
			Map<IBlockState, ModelResourceLocation> map = new HashMap<>();

			for (IBlockState state : blockIn.getBlockState().getValidStates())
			{
				map.put(state, MODEL_LOCATION);
			}

			return map;
		}
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return modelLocation.getResourceDomain().equals(Yabba.MOD_ID) && modelLocation.getResourcePath().equals("barrel");
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		return this;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		YabbaClient.loadModelsAndSkins();
	}

	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return Collections.emptyList();
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return YabbaClient.TEXTURES;
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		TextureAtlasSprite particle = bakedTextureGetter.apply(new ResourceLocation("blocks/planks_oak"));
		Map<BarrelModelKey, BarrelModelVariant> map = new HashMap<>();

		for (BarrelSkin skin : YabbaClient.ALL_SKINS)
		{
			skin.spriteSet = skin.textures.getSpriteSet(bakedTextureGetter);
		}

		for (BarrelModel model : YabbaClient.ALL_MODELS)
		{
			model.textureMap = new HashMap<>();

			for (Map.Entry<String, TextureSet> entry : model.textures.entrySet())
			{
				model.textureMap.put(entry.getKey(), entry.getValue().getSpriteSet(bakedTextureGetter));
			}

			for (BarrelSkin skin : YabbaClient.ALL_SKINS)
			{
				List<List<BakedQuad>> quads = new ArrayList<>(ModelRotation.values().length);
				model.textureMap.put("skin", skin.spriteSet);

				for (ModelRotation rotation : ModelRotation.values())
				{
					quads.add(model.buildModel(format, rotation));
				}

				List<BakedQuad> itemQuads = model.buildItemModel(format);
				map.put(BarrelModelKey.get(model, skin), new BarrelModelVariant(quads, new BakedBarrelItemModel(particle, itemQuads.isEmpty() ? quads.get(0) : itemQuads)));
			}
		}

		return new BakedBarrelBlockModel(particle, map);
	}
}