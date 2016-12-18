package com.latmod.yabba.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public abstract class BlockBarrelBase extends Block
{
    public BlockBarrelBase(Material blockMaterialIn, MapColor blockMapColorIn)
    {
        super(blockMaterialIn, blockMapColorIn);
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack)
    {
        if(!worldIn.isRemote)
        {
            ItemStack itemStack = new ItemStack(this, 1, damageDropped(state));
            dropItem(itemStack, te);
            spawnAsEntity(worldIn, pos, itemStack);
        }
        //super.harvestBlock(worldIn, player, pos, state, null, stack);
    }


    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        List<ItemStack> ret = new ArrayList<>(1);
        ItemStack itemStack = new ItemStack(this, 1, damageDropped(state));
        dropItem(itemStack, world.getTileEntity(pos));
        ret.add(itemStack);
        return ret;
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
    {
        if(!worldIn.isRemote)
        {
            ItemStack itemStack = new ItemStack(this, 1, damageDropped(worldIn.getBlockState(pos)));
            dropItem(itemStack, worldIn.getTileEntity(pos));
            spawnAsEntity(worldIn, pos, itemStack);
        }

        super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
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
