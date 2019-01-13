package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.client.ModelBase;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.block.BlockDecorativeBlock;
import com.latmod.yabba.tile.IBakedModelBarrel;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author LatvianModder
 */
public class BakedBarrelBlockModel extends ModelBase
{
	private final VertexFormat format;
	private final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
	private final Map<EnumBarrelModel, IModel> baseModels;
	private final Map<EnumBarrelModel, IModel> cutoutModels;
	private final Map<BarrelBlockModelKey, BarrelBlockModelVariant> cache;
	private final Map<BarrelLook, BakedBarrelItemModel> itemModels;

	private final ItemOverrideList itemOverrideList = new ItemOverrideList(Collections.emptyList())
	{
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
		{
			BarrelLook look = BarrelLook.DEFAULT;

			if (BlockUtils.hasData(stack))
			{
				NBTTagCompound data = BlockUtils.getData(stack);
				look = BarrelLook.get(EnumBarrelModel.getFromNBTName(data.getString("Model")), data.getString("Skin"));
			}

			BakedBarrelItemModel bakedModel = itemModels.get(look);

			if (bakedModel == null)
			{
				BarrelBlockModelVariant variant = getVariant(new BarrelBlockModelKey(look, EnumFacing.NORTH.getHorizontalIndex()));
				bakedModel = new BakedBarrelItemModel();

				for (int r = 0; r < 7; r++)
				{
					ArrayList<BakedQuad> quads = new ArrayList<>();
					quads.addAll(variant.rotations[0][r].solidQuads);
					quads.addAll(variant.rotations[0][r].cutoutQuads);
					quads.addAll(variant.rotations[0][r].translucentQuads);
					bakedModel.quads.add(optimize(quads));
				}

				itemModels.put(look, bakedModel);
			}

			return bakedModel;
		}
	};

	public BakedBarrelBlockModel(VertexFormat f, Function<ResourceLocation, TextureAtlasSprite> t)
	{
		super(t.apply(new ResourceLocation("blocks/planks_oak")));
		format = f;
		bakedTextureGetter = t;
		baseModels = new EnumMap<>(EnumBarrelModel.class);
		cutoutModels = new EnumMap<>(EnumBarrelModel.class);
		cache = new HashMap<>();
		itemModels = new HashMap<>();

		for (EnumBarrelModel model : EnumBarrelModel.NAME_MAP)
		{
			baseModels.put(model, ModelLoaderRegistry.getModelOrMissing(model.getBaseModel()).uvlock(true).smoothLighting(true));//.smoothLighting(false);

			if (model.getCutoutModel() != null)
			{
				cutoutModels.put(model, ModelLoaderRegistry.getModelOrMissing(model.getCutoutModel()).uvlock(true).smoothLighting(true));//.smoothLighting(false);
			}
		}
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return true;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		if (state == null)
		{
			return Collections.emptyList();
		}

		IBakedModelBarrel barrel = null;

		if (state instanceof IExtendedBlockState)
		{
			barrel = ((IExtendedBlockState) state).getValue(BlockDecorativeBlock.BARREL);
		}

		TileEntity tileEntity = barrel == null ? null : barrel.getBarrelTileEntity();

		if (tileEntity == null)
		{
			return Collections.emptyList();
		}

		if (side == null)
		{
			boolean render = false;

			for (EnumFacing side1 : EnumFacing.VALUES)
			{
				if (state.shouldSideBeRendered(tileEntity.getWorld(), tileEntity.getPos(), side1))
				{
					render = true;
					break;
				}
			}

			if (!render)
			{
				return Collections.emptyList();
			}
		}

		EnumFacing facing = barrel.getBarrelRotation();
		BarrelBlockModelVariant variant = getVariant(new BarrelBlockModelKey(barrel.getLook(), facing.getHorizontalIndex()));
		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();

		if (layer == BlockRenderLayer.SOLID)
		{
			return variant.rotations[facing.getOpposite().getHorizontalIndex()][side == null ? 6 : side.getIndex()].solidQuads;
		}
		else if (layer == BlockRenderLayer.CUTOUT)
		{
			return variant.rotations[facing.getOpposite().getHorizontalIndex()][side == null ? 6 : side.getIndex()].cutoutQuads;
		}
		else if (layer == BlockRenderLayer.TRANSLUCENT)
		{
			return variant.rotations[facing.getOpposite().getHorizontalIndex()][side == null ? 6 : side.getIndex()].translucentQuads;
		}

		return Collections.emptyList();
	}

	private BarrelBlockModelVariant getVariant(BarrelBlockModelKey key)
	{
		BarrelBlockModelVariant variant = cache.get(key);

		if (variant != null)
		{
			return variant;
		}

		variant = new BarrelBlockModelVariant();
		BarrelSkin skin = key.look.getSkin();
		IModel m = baseModels.get(key.look.model).retexture(skin.skinMap.textures);

		for (int f = 0; f < 7; f++)
		{
			for (int i = 0; i < variant.rotations.length; i++)
			{
				variant.rotations[i][f] = new BarrelBlockModelVariant.Quads();
				List<BakedQuad> list = m.bake(BarrelBlockModelKey.ROTATIONS[i], format, bakedTextureGetter).getQuads(null, f == 6 ? null : EnumFacing.VALUES[f], 0L);

				if (skin.layer == null || skin.layer == BlockRenderLayer.SOLID)
				{
					variant.rotations[i][f].solidQuads.addAll(list);
				}
				else if (skin.layer == BlockRenderLayer.CUTOUT)
				{
					variant.rotations[i][f].cutoutQuads.addAll(list);
				}
				else if (skin.layer == BlockRenderLayer.TRANSLUCENT)
				{
					variant.rotations[i][f].translucentQuads.addAll(list);
				}
			}

			for (int i = 0; i < variant.rotations.length; i++)
			{
				variant.rotations[i][f].solidQuads = optimize(variant.rotations[i][f].solidQuads);
				variant.rotations[i][f].cutoutQuads = optimize(variant.rotations[i][f].cutoutQuads);
				variant.rotations[i][f].translucentQuads = optimize(variant.rotations[i][f].translucentQuads);
			}
		}

		cache.put(key, variant);
		return variant;
	}

	private static List<BakedQuad> optimize(List<BakedQuad> l)
	{
		return l.isEmpty() ? Collections.emptyList() : l.size() == 1 ? Collections.singletonList(l.get(0)) : Arrays.asList(l.toArray(new BakedQuad[0]));
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return itemOverrideList;
	}
}