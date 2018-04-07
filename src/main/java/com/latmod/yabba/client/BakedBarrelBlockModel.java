package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.lib.client.ModelBase;
import com.latmod.yabba.Yabba;
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
	private final Map<BarrelLook, BarrelModelVariant> map;
	private BarrelModelVariant defaultModelVariant;
	private final ItemOverrideList itemOverrideList = new ItemOverrideList(new ArrayList<>())
	{
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
		{
			BarrelLook key = BarrelLook.DEFAULT;

			if (stack.hasTagCompound())
			{
				NBTTagCompound data = stack.getTagCompound().getCompoundTag("BlockEntityTag");
				key = BarrelLook.get(data.getString("Model"), data.getString("Skin"));
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

	private BarrelModelVariant get(BarrelLook key)
	{
		if (key == BarrelLook.DEFAULT && defaultModelVariant != null)
		{
			return defaultModelVariant;
		}

		BarrelModelVariant variant = map.get(key);

		if (variant == null)
		{
			List<List<BakedQuad>> quads = new ArrayList<>(ModelRotation.values().length);
			BarrelModel model = key.getModel();
			BarrelSkin skin = key.getSkin();
			model.textureMap.put("skin", skin.spriteSet);

			for (ModelRotation rotation : ModelRotation.values())
			{
				quads.add(model.buildModel(format, rotation, skin));
			}

			List<BakedQuad> itemQuads = model.buildItemModel(format, skin);
			variant = new BarrelModelVariant(quads, new BakedBarrelItemModel(getParticleTexture(), itemQuads.isEmpty() ? quads.get(0) : itemQuads));
			map.put(key, variant);

			if (key == BarrelLook.DEFAULT)
			{
				defaultModelVariant = variant;
			}

			if (FTBLibConfig.debugging.print_more_info)
			{
				Yabba.LOGGER.info("Created cached model for " + model.id + ":" + skin.id);
			}
		}

		return variant;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		if (side == null && state instanceof IExtendedBlockState)
		{
			IExtendedBlockState statex = (IExtendedBlockState) state;
			BarrelLook look = statex.getValue(BlockAdvancedBarrelBase.LOOK);

			if (look == null)
			{
				look = BarrelLook.DEFAULT;
			}

			if (MinecraftForgeClient.getRenderLayer() == look.getLayer())
			{
				return get(look).getQuads(state.getValue(BlockAdvancedBarrelBase.ROTATION).getModelRotationIndexFromFacing(state.getValue(BlockHorizontal.FACING)));
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