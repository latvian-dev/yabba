package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.client.ModelBase;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.block.BlockAdvancedBarrelBase;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class BakedBarrelBlockModel extends ModelBase
{
	private final VertexFormat format;
	private final Map<BarrelLook, IBakedModel> itemModels;
	private final Map<BarrelBlockModelKey, BarrelBlockModelVariant> blockModels;
	private static final List<BakedQuad>[] EMPTY = new List[7];

	static
	{
		Arrays.fill(EMPTY, Collections.emptyList());
	}

	private final ItemOverrideList itemOverrideList = new ItemOverrideList(Collections.emptyList())
	{
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
		{
			BarrelLook look = BarrelLook.DEFAULT;

			if (stack.hasTagCompound())
			{
				NBTTagCompound data = stack.getTagCompound().getCompoundTag("BlockEntityTag");
				look = BarrelLook.get(EnumBarrelModel.getFromNBTName(data.getString("Model")), data.getString("Skin"));
			}

			IBakedModel bakedModel = itemModels.get(look);

			if (bakedModel == null)
			{
				BarrelModel model = look.getModel();
				BarrelSkin skin = look.getSkin();
				model.textureMap.put("skin", skin.spriteSet);
				List<BakedQuad>[] quads = new List[7];
				for (int i = 0; i < 7; i++)
				{
					quads[i] = model.buildItemModel(format, skin, i == 6 ? null : EnumFacing.VALUES[i]);
				}

				bakedModel = new BakedBarrelItemModel(optimize(quads));
				itemModels.put(look, bakedModel);
			}

			return bakedModel;
		}
	};

	public BakedBarrelBlockModel(TextureAtlasSprite p, VertexFormat f)
	{
		super(p);
		format = f;
		itemModels = new HashMap<>();
		blockModels = new HashMap<>();
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return false;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		BarrelLook look = BarrelLook.DEFAULT;

		if (state != null)
		{
			String skin = null;

			if (state instanceof IExtendedBlockState)
			{
				skin = ((IExtendedBlockState) state).getValue(BlockAdvancedBarrelBase.SKIN);
			}

			look = BarrelLook.get(state.getValue(BlockAdvancedBarrelBase.MODEL), skin);
		}

		BarrelBlockModelKey key = new BarrelBlockModelKey(look, state.getValue(BlockAdvancedBarrelBase.FACING).getHorizontalIndex());
		BarrelBlockModelVariant variant = blockModels.get(key);

		if (variant == null)
		{
			BarrelModel model = key.look.getModel();
			BarrelSkin skin = key.look.getSkin();
			ModelRotation rotation = BarrelBlockModelKey.ROTATIONS[state.getValue(BlockAdvancedBarrelBase.FACING).getOpposite().getHorizontalIndex()];
			model.textureMap.put("skin", skin.spriteSet);

			List<BakedQuad>[] solidQuads = new List[7];
			List<BakedQuad>[] cutoutQuads = new List[7];
			List<BakedQuad>[] translucentQuads = new List[7];

			for (int i = 0; i < 7; i++)
			{
				EnumFacing side1 = i == 6 ? null : EnumFacing.VALUES[i];
				solidQuads[i] = model.buildModel(format, rotation, skin, BlockRenderLayer.SOLID, side1);
				cutoutQuads[i] = model.buildModel(format, rotation, skin, BlockRenderLayer.CUTOUT, side1);
				translucentQuads[i] = model.buildModel(format, rotation, skin, BlockRenderLayer.TRANSLUCENT, side1);
			}

			variant = new BarrelBlockModelVariant(optimize(solidQuads), optimize(cutoutQuads), optimize(translucentQuads));
			blockModels.put(key, variant);
		}

		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();

		if (layer == null)
		{
			layer = key.look.getSkin().layer;
		}

		int sidei = 6;

		if (side != null)
		{
			sidei = side.getIndex();
		}

		if (layer == BlockRenderLayer.SOLID)
		{
			return variant.solidQuads[sidei];
		}
		else if (layer == BlockRenderLayer.TRANSLUCENT)
		{
			return variant.translucentQuads[sidei];
		}
		else
		{
			return variant.cutoutQuads[sidei];
		}
	}

	private static List<BakedQuad>[] optimize(List<BakedQuad>[] lists)
	{
		for (List<BakedQuad> list : lists)
		{
			if (!list.isEmpty())
			{
				return lists;
			}
		}

		return EMPTY;
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return itemOverrideList;
	}
}