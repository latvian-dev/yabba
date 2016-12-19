package com.latmod.yabba.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

/**
 * Created by LatvianModder on 19.12.2016.
 */
public class BlockCrate extends BlockBarrel
{
    private boolean useParent = false;

    public BlockCrate()
    {
        useParent = true;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return !useParent || state.getValue(VARIANT).getParentState().isFullCube();
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return !useParent || state.getValue(VARIANT).getParentState().isOpaqueCube();
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        return state.getValue(VARIANT).getParentState().getBlock().canRenderInLayer(state, layer);
    }
}