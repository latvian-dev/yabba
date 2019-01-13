package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.client.ModelBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BakedBarrelItemModel extends ModelBase
{
	public final List<List<BakedQuad>> quads;

	public BakedBarrelItemModel()
	{
		super(null);
		quads = new ArrayList<>(7);
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return false;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		return quads.get(side == null ? 6 : side.getIndex());
	}
}