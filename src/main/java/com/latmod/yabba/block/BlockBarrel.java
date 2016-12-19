package com.latmod.yabba.block;

import com.latmod.yabba.BarrelTier;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IBarrelVariant;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 12.12.2016.
 */
public class BlockBarrel extends BlockBarrelBase
{
    public static final Map<UUID, Long> LAST_RIGHT_CLICK_MAP = new HashMap<>();
    public static final PropertyBarrelVariant VARIANT = PropertyBarrelVariant.create("variant");

    public BlockBarrel()
    {
        super(Material.WOOD, MapColor.WOOD);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, YabbaRegistry.DEFAULT_VARIANT).withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
    }

    @Override
    public void dropItem(ItemStack itemStack, @Nullable TileEntity tile)
    {
        if(tile instanceof TileBarrel)
        {
            ((IBarrelModifiable) itemStack.getCapability(YabbaCommon.BARREL_CAPABILITY, null)).copyFrom(((TileBarrel) tile).barrel);
        }
    }

    @Override
    public void placeFromItem(ItemStack stack, @Nullable TileEntity tile)
    {
        if(tile instanceof TileBarrel && stack.hasCapability(YabbaCommon.BARREL_CAPABILITY, null))
        {
            ((TileBarrel) tile).barrel.copyFrom(stack.getCapability(YabbaCommon.BARREL_CAPABILITY, null));
            tile.markDirty();
        }
    }

    public ItemStack createStack(IBarrelVariant variant, BarrelTier tier)
    {
        ItemStack stack = new ItemStack(this);
        IBarrelModifiable barrel = (IBarrelModifiable) stack.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
        barrel.setVariant(variant);
        barrel.setTier(tier);
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        for(IBarrelVariant variant : YabbaRegistry.BARRELS_VALUES)
        {
            list.add(createStack(variant, YabbaCommon.TIER_WOOD));
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileBarrel();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, VARIANT, BlockHorizontal.FACING);
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
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if(tile instanceof TileBarrel)
        {
            return state.withProperty(VARIANT, ((TileBarrel) tile).barrel.getVariant());
        }

        return state;
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
    @Deprecated
    public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos)
    {
        return state.getValue(VARIANT).getParentState().getBlockHardness(worldIn, pos);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        if(!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(pos);

            if(tile != null && tile.hasCapability(YabbaCommon.BARREL_CAPABILITY, null) && tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null).getUpgradeData("ObsidianShell") != null)
            {
                return 100000000F;
            }
        }

        return world.getBlockState(pos).getValue(VARIANT).getParentState().getBlock().getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity)
    {
        return state.getValue(VARIANT).getParentState().getBlock().getSoundType(state, world, pos, entity);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack)
    {
        return getDefaultState().withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
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
}