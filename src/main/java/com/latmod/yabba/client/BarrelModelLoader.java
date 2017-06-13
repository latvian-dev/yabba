package com.latmod.yabba.client;

import com.google.common.base.Function;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
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
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class BarrelModelLoader implements IModel, ICustomModelLoader
{
	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation(Yabba.MOD_ID + ":barrel#normal");

	public final Collection<ResourceLocation> textures;

	public BarrelModelLoader()
	{
		textures = new HashSet<>();
	}

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

	public static void loadFor(Block block)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, MODEL_LOCATION);
		ModelLoader.setCustomStateMapper(block, StateMapper.INSTANCE);
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
		textures.clear();

		for (IBarrelModel model : YabbaClient.ALL_MODELS)
		{
			textures.addAll(model.getExtraTextures());
		}

		for (IBarrelSkin skin : YabbaClient.ALL_SKINS)
		{
			textures.addAll(skin.getTextures().getTextures());
		}
	}

	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return Collections.emptyList();
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return textures;
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		TextureAtlasSprite particle = bakedTextureGetter.apply(new ResourceLocation("blocks/planks_oak"));
		Map<BarrelModelKey, BarrelModelVariant> map = new HashMap<>();

		for (IBarrelModel model : YabbaClient.ALL_MODELS)
		{
			for (IBarrelSkin skin : YabbaClient.ALL_SKINS)
			{
				List<List<BakedQuad>> quads = new ArrayList<>(ModelRotation.values().length);

				for (ModelRotation rotation : ModelRotation.values())
				{
					quads.add(model.buildModel(format, skin, rotation, bakedTextureGetter));
				}

				List<BakedQuad> itemQuads = model.buildItemModel(format, skin, bakedTextureGetter);
				map.put(new BarrelModelKey(model, skin), new BarrelModelVariant(quads, new BakedBarrelItemModel(particle, itemQuads == null ? quads.get(0) : itemQuads)));
			}
		}

		return new BakedBarrelBlockModel(particle, map);
	}

	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}
}