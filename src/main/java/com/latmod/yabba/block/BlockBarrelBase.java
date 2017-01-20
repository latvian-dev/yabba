package com.latmod.yabba.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public abstract class BlockBarrelBase extends BlockYabba
{
    public BlockBarrelBase(String id, Material blockMaterialIn, MapColor blockMapColorIn)
    {
        super(id, blockMaterialIn, blockMapColorIn);
    }

    public abstract void dropItem(ItemStack itemStack, @Nullable TileEntity tile);

    public abstract void placeFromItem(ItemStack stack, @Nullable TileEntity tile);

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        return layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if(!worldIn.isRemote)
        {
            ItemStack itemStack = new ItemStack(this, 1, damageDropped(state));
            dropItem(itemStack, worldIn.getTileEntity(pos));
            spawnAsEntity(worldIn, pos, itemStack);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if(hasTileEntity(state) && placer instanceof EntityPlayerMP)
        {
            placeFromItem(stack, worldIn.getTileEntity(pos));
        }
    }
}
