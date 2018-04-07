package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.client.ModelBase;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.block.BlockAdvancedBarrelBase;
import com.latmod.yabba.util.BarrelLook;
import net.minecraft.block.BlockHorizontal;
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
import java.util.ArrayList;
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

	private final ItemOverrideList itemOverrideList = new ItemOverrideList(new ArrayList<>())
	{
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
		{
			BarrelLook look = BarrelLook.DEFAULT;

			if (stack.hasTagCompound())
			{
				NBTTagCompound data = stack.getTagCompound().getCompoundTag("BlockEntityTag");
				look = BarrelLook.get(data.getString("Model"), data.getString("Skin"));
			}

			IBakedModel bakedModel = itemModels.get(look);

			if (bakedModel == null)
			{
				BarrelModel model = look.getModel();
				BarrelSkin skin = look.getSkin();
				model.textureMap.put("skin", skin.spriteSet);
				bakedModel = new BakedBarrelItemModel(model.buildItemModel(format, skin));
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
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		if (side == null && state instanceof IExtendedBlockState)
		{
			BarrelLook look = ((IExtendedBlockState) state).getValue(BlockAdvancedBarrelBase.LOOK);

			if (look == null)
			{
				look = BarrelLook.DEFAULT;
			}

			BarrelBlockModelKey key = new BarrelBlockModelKey(look, state.getValue(BlockAdvancedBarrelBase.ROTATION).getModelRotationIndexFromFacing(state.getValue(BlockHorizontal.FACING)));
			BarrelBlockModelVariant variant = blockModels.get(key);

			if (variant == null)
			{
				BarrelModel model = key.look.getModel();
				BarrelSkin skin = key.look.getSkin();
				ModelRotation rotation = BarrelBlockModelKey.ROTATIONS[key.rotation];
				model.textureMap.put("skin", skin.spriteSet);
				List<BakedQuad> solidQuads = model.buildModel(format, rotation, skin, BlockRenderLayer.SOLID);
				List<BakedQuad> cutoutQuads = model.buildModel(format, rotation, skin, BlockRenderLayer.CUTOUT);
				List<BakedQuad> translucentQuads = model.buildModel(format, rotation, skin, BlockRenderLayer.TRANSLUCENT);
				variant = new BarrelBlockModelVariant(solidQuads, cutoutQuads, translucentQuads);
				blockModels.put(key, variant);
			}

			BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();

			if (layer == null)
			{
				layer = key.look.getSkin().layer;
			}

			if (layer == BlockRenderLayer.SOLID)
			{
				return variant.solidQuads;
			}
			else if (layer == BlockRenderLayer.TRANSLUCENT)
			{
				return variant.translucentQuads;
			}
			else
			{
				return variant.cutoutQuads;
			}
		}

		return Collections.emptyList();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return itemOverrideList;
	}
}