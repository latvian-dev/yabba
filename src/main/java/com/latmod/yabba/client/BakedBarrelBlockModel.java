package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.client.ModelBase;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.block.BlockStorageBarrelBase;
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
	private final Map<BarrelModelKey, BarrelModelVariant> map;
	private BarrelModelVariant defaultModelVariant;
	private final ItemOverrideList itemOverrideList = new ItemOverrideList(new ArrayList<>())
	{
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
		{
			BarrelModelKey key = BarrelModelKey.DEFAULT;

			if (stack.hasTagCompound())
			{
				NBTTagCompound data = stack.getTagCompound().getCompoundTag("BlockEntityTag");
				key = BarrelModelKey.get(data.getString("Model"), data.getString("Skin"));
			}

			return get(key).itemModel;
		}
	};

	public BakedBarrelBlockModel(TextureAtlasSprite p, VertexFormat f)
	{
		super(p);
		format = f;
		map = new HashMap<>();
	}

	private BarrelModelVariant get(BarrelModelKey key)
	{
		if (key == BarrelModelKey.DEFAULT && defaultModelVariant != null)
		{
			return defaultModelVariant;
		}

		BarrelModelVariant variant = map.get(key);

		if (variant == null)
		{
			List<List<BakedQuad>> quads = new ArrayList<>(ModelRotation.values().length);
			BarrelModel model = YabbaClient.getModel(key.model);
			BarrelSkin skin = YabbaClient.getSkin(key.skin);
			model.textureMap.put("skin", skin.spriteSet);

			for (ModelRotation rotation : ModelRotation.values())
			{
				quads.add(model.buildModel(format, rotation, skin));
			}

			List<BakedQuad> itemQuads = model.buildItemModel(format, skin);
			variant = new BarrelModelVariant(quads, new BakedBarrelItemModel(getParticleTexture(), itemQuads.isEmpty() ? quads.get(0) : itemQuads));
			map.put(key, variant);

			if (key == BarrelModelKey.DEFAULT)
			{
				defaultModelVariant = variant;
			}
		}

		return variant;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		if (state instanceof IExtendedBlockState)
		{
			IExtendedBlockState statex = (IExtendedBlockState) state;
			BarrelModel model = YabbaClient.getModel(StringUtils.emptyIfNull(statex.getValue(BlockStorageBarrelBase.MODEL)));
			BarrelSkin skin = YabbaClient.getSkin(StringUtils.emptyIfNull(statex.getValue(BlockStorageBarrelBase.SKIN)));

			if (MinecraftForgeClient.getRenderLayer() == ClientUtils.getStrongest(model.layer, skin.layer))
			{
				return get(BarrelModelKey.get(model.id, skin.id)).getQuads(state.getValue(BlockStorageBarrelBase.ROTATION).getModelRotationIndexFromFacing(state.getValue(BlockHorizontal.FACING)));
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