package com.latmod.yabba.models;

import com.feed_the_beast.ftbl.lib.client.ModelBuilder;
import com.feed_the_beast.ftbl.lib.client.SpriteSet;
import com.google.common.base.Function;
import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ModelPanel extends ModelBase
{
	private final float height;

	public ModelPanel(String id, float h)
	{
		super(id);
		height = h;
	}

	@Override
	public Collection<ResourceLocation> getExtraTextures()
	{
		return Collections.emptyList();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<BakedQuad> buildModel(VertexFormat format, IBarrelSkin skin, ModelRotation rotation, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
	{
		ModelBuilder model = new ModelBuilder(format, rotation);
		SpriteSet spriteSet = new SpriteSet(skin.getTextures(), textureAtlas);
		model.addCube(0F, 0F, 16F - height * 16F, 16F, 16F, 16F, spriteSet);
		return model.getQuads();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<BakedQuad> buildItemModel(VertexFormat format, IBarrelSkin skin, Function<ResourceLocation, TextureAtlasSprite> textureAtlas)
	{
		ModelBuilder model = new ModelBuilder(format, ModelRotation.X0_Y0);
		SpriteSet spriteSet = new SpriteSet(skin.getTextures(), textureAtlas);
		model.addCube(0F, 0F, 8F - height * 8F, 16F, 16F, 8F + height * 8F, spriteSet);
		return model.getQuads();
	}

	@Override
	public float getTextDistance()
	{
		return 1F - height - 0.005F;
	}

	@Override
	public float getItemDistance()
	{
		return 1F - height - 0.01F;
	}
}