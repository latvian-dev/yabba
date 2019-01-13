package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.block.BlockDecorativeBlock;
import com.latmod.yabba.tile.IBakedModelBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BarrelModelLoader extends StateMapperBase implements ICustomModelLoader, IBlockColor, IItemColor
{
	public static final BarrelModelLoader INSTANCE = new BarrelModelLoader();

	public static final ModelResourceLocation ID = new ModelResourceLocation(Yabba.MOD_ID + ":barrel#normal");

	@Override
	public boolean accepts(ResourceLocation id)
	{
		return ID.getNamespace().equals(id.getNamespace()) && ID.getPath().equals(id.getPath());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation)
	{
		return new BarrelModel();
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		YabbaClient.loadModelsAndSkins();
	}

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		return ID;
	}

	@Override
	public int colorMultiplier(IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex)
	{
		if (tintIndex == 0 && state instanceof IExtendedBlockState)
		{
			IBakedModelBarrel barrel = ((IExtendedBlockState) state).getValue(BlockDecorativeBlock.BARREL);

			if (barrel != null)
			{
				return barrel.getLook().getSkin().color;
			}
		}

		return 0xFFFFFFFF;
	}

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex)
	{
		if (tintIndex == 0 && BlockUtils.hasData(stack))
		{
			return YabbaClient.getSkin(BlockUtils.getData(stack).getString("Skin")).color;
		}

		return 0xFFFFFFFF;
	}
}