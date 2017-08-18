package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.client.ModelBase;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.block.BlockStorageBarrelBase;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class BakedBarrelBlockModel extends ModelBase
{
	private final Map<BarrelModelKey, BarrelModelVariant> map;
	private final BarrelModelVariant defaultModelVariant;
	private final ItemOverrideList itemOverrideList = new ItemOverrideList(new ArrayList<>())
	{
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
		{
			if (stack.hasTagCompound())
			{
				NBTTagCompound data = stack.getTagCompound().getCompoundTag("BlockEntityTag");
				BarrelModelKey key = BarrelModelKey.get(data.getString("Model"), data.getString("Skin"));

				if (key != BarrelModelKey.DEFAULT)
				{
					BarrelModelVariant v = map.get(key);

					if (v != null)
					{
						return v.itemModel;
					}
				}
			}

			return defaultModelVariant.itemModel;
		}
	};

	public BakedBarrelBlockModel(TextureAtlasSprite p, Map<BarrelModelKey, BarrelModelVariant> m)
	{
		super(p);
		map = m;
		defaultModelVariant = map.get(BarrelModelKey.DEFAULT);
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		if (state instanceof IExtendedBlockState)
		{
			IExtendedBlockState statex = (IExtendedBlockState) state;
			BarrelModel model = YabbaClient.getModel(statex.getValue(BlockStorageBarrelBase.MODEL));
			BarrelSkin skin = YabbaClient.getSkin(statex.getValue(BlockStorageBarrelBase.SKIN));
			BarrelModelVariant value = map.get(BarrelModelKey.get(model.id, skin.id));

			if (value != null && MinecraftForgeClient.getRenderLayer() == ClientUtils.getStrongest(model.layer, skin.layer))
			{
				return value.getQuads(state.getValue(BlockStorageBarrelBase.ROTATION).getModelRotationIndexFromFacing(state.getValue(BlockHorizontal.FACING)));
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