package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.client.ModelBuilder;
import com.latmod.yabba.block.BlockStorageBarrelBase;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class BakedBarrelBlockModel implements IBakedModel
{
	private final TextureAtlasSprite particle;
	private final Map<BarrelModelKey, BarrelModelVariant> map;
	private final BarrelModelVariant defaultModelVariant;
	private final ItemOverrideList itemOverrideList = new ItemOverrideList(new ArrayList<>())
	{
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
		{
			BarrelModelVariant v = stack.hasTagCompound() ? map.get(BarrelModelKey.get(stack.getTagCompound().getString("Model"), stack.getTagCompound().getString("Skin"))) : defaultModelVariant;
			if (v == null)
			{
				v = defaultModelVariant;
			}

			return v == null ? originalModel : v.itemModel;
		}
	};

	public BakedBarrelBlockModel(TextureAtlasSprite p, Map<BarrelModelKey, BarrelModelVariant> m)
	{
		particle = p;
		map = m;
		defaultModelVariant = map.get(BarrelModelKey.DEFAULT);
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		if (state instanceof IExtendedBlockState)
		{
			BarrelModelVariant value = map.get(BarrelModelKey.get((IExtendedBlockState) state));

			if (value != null)
			{
				return value.getQuads(state.getValue(BlockStorageBarrelBase.ROTATION).getModelRotationIndexFromFacing(state.getValue(BlockHorizontal.FACING)));
			}
		}

		return Collections.emptyList();
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return true;
	}

	@Override
	public boolean isGui3d()
	{
		return true;
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return particle;
	}

	@Deprecated
	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return itemOverrideList;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
	{
		return ModelBuilder.handlePerspective(this, cameraTransformType);
	}
}