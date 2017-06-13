package com.latmod.yabba.api;

import com.google.common.base.Function;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public interface IBarrelModel extends IStringSerializable
{
	Collection<ResourceLocation> getExtraTextures();

	@SideOnly(Side.CLIENT)
	List<BakedQuad> buildModel(VertexFormat format, IBarrelSkin skin, ModelRotation rotation, Function<ResourceLocation, TextureAtlasSprite> textureAtlas);

	@Nullable
	@SideOnly(Side.CLIENT)
	default List<BakedQuad> buildItemModel(VertexFormat format, IBarrelSkin skin, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
	{
		return null;
	}

	default float getTextDistance()
	{
		return -0.005F;
	}

	default float getItemDistance()
	{
		return 0.04F;
	}
}