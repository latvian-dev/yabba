package com.latmod.yabba.block;

import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.ITier;
import com.latmod.yabba.item.EnumUpgrade;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 12.12.2016.
 */
public class BlockBarrel extends Block
{
    public static final Map<UUID, Long> LAST_RIGHT_CLICK_MAP = new HashMap<>();

    public BlockBarrel()
    {
        super(Material.WOOD, MapColor.WOOD);
        setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
        setHardness(2.2F);
        setResistance(6F);
    }

    public ItemStack createStackWithTier(ITier tier)
    {
        ItemStack stack = new ItemStack(this);
        ((IBarrel) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).setTier(tier);
        return stack;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileBarrel();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockHorizontal.FACING);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta & 3));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
    }

    @Override
    @Deprecated
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
    }

    @Override
    @Deprecated
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation(state.getValue(BlockHorizontal.FACING)));
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        if(!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(pos);

            if(tile instanceof TileBarrel && ((TileBarrel) tile).barrel.getUpgradeData(EnumUpgrade.OBSIDIAN_SHELL) != null)
            {
                return 100000000F;
            }
        }

        return 6F;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack)
    {
        return getDefaultState().withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(side == state.getValue(BlockHorizontal.FACING))
        {
            if(!worldIn.isRemote)
            {
                TileEntity tile = worldIn.getTileEntity(pos);

                if(tile instanceof TileBarrel)
                {
                    ((TileBarrel) tile).onRightClick(playerIn, heldItem);
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if(hasTileEntity(state) && placer instanceof EntityPlayerMP)
        {
            TileEntity te = worldIn.getTileEntity(pos);

            if(te instanceof TileBarrel && stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
            {
                ((TileBarrel) te).barrel.copyFrom((IBarrel) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
                te.markDirty();
            }
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack)
    {
        if(te instanceof TileBarrel)
        {
            ItemStack itemStack = new ItemStack(this);
            ((IBarrel) itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).copyFrom(((TileBarrel) te).barrel);
            spawnAsEntity(worldIn, pos, itemStack);
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, null, stack);
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        List<ItemStack> ret = new ArrayList<>(1);
        ItemStack itemStack = new ItemStack(this);
        TileEntity te = world.getTileEntity(pos);

        if(te instanceof TileBarrel)
        {
            ((IBarrel) itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).copyFrom(((TileBarrel) te).barrel);
        }

        ret.add(itemStack);
        return ret;
    }
}