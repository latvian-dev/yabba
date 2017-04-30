package com.latmod.yabba.block;

import com.feed_the_beast.ftbl.lib.block.EnumRotation;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.ITier;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.PropertyBarrelModel;
import com.latmod.yabba.util.PropertyBarrelSkin;
import com.latmod.yabba.util.Tier;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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
    public static final Map<UUID, Long> LAST_CLICK_MAP = new HashMap<>();
    public static final PropertyEnum<EnumRotation> ROTATION = PropertyEnum.create("rotation", EnumRotation.class);
    public static final PropertyBarrelModel MODEL = PropertyBarrelModel.create("model");
    public static final PropertyBarrelSkin SKIN = PropertyBarrelSkin.create("skin");

    public BlockBarrel()
    {
        super("barrel", Material.WOOD, MapColor.WOOD);
        setDefaultState(blockState.getBaseState()
                .withProperty(ROTATION, EnumRotation.NORMAL)
                .withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
                .withProperty(MODEL, ModelBarrel.INSTANCE)
                .withProperty(SKIN, YabbaRegistry.DEFAULT_SKIN));
        setHardness(2F);
    }

    @Override
    public ItemBlock createItemBlock()
    {
        return new ItemBlockBarrel(this);
    }

    public static EnumFacing normalizeFacing(IBlockState state)
    {
        EnumRotation rotation = state.getValue(ROTATION);

        if(rotation == EnumRotation.FACING_DOWN)
        {
            return EnumFacing.DOWN;
        }
        else if(rotation == EnumRotation.FACING_UP)
        {
            return EnumFacing.UP;
        }

        return state.getValue(BlockHorizontal.FACING);
    }

    @Override
    public void dropItem(ItemStack itemStack, @Nullable TileEntity tile)
    {
        if(tile instanceof TileBarrel)
        {
            IBarrel barrel = tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
            ((IBarrelModifiable) itemStack.getCapability(YabbaCommon.BARREL_CAPABILITY, null)).copyFrom(barrel);
        }
    }

    @Override
    public void placeFromItem(ItemStack stack, @Nullable TileEntity tile)
    {
        if(tile instanceof TileBarrel && stack.hasCapability(YabbaCommon.BARREL_CAPABILITY, null))
        {
            IBarrelModifiable barrel = (IBarrelModifiable) tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
            barrel.copyFrom(stack.getCapability(YabbaCommon.BARREL_CAPABILITY, null));
            tile.markDirty();
        }
    }

    public ItemStack createStack(IBarrelModel model, IBarrelSkin skin, ITier tier)
    {
        ItemStack stack = new ItemStack(this);
        IBarrelModifiable barrel = (IBarrelModifiable) stack.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
        barrel.setTier(tier);
        barrel.setFlags(0);
        barrel.setModel(model);
        barrel.setSkin(skin);
        barrel.setItemCount(0);
        return stack;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntity te = world.getTileEntity(pos);

        if(te != null && te.hasCapability(YabbaCommon.BARREL_CAPABILITY, null))
        {
            IBarrel barrel = te.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
            ItemStack stack = createStack(barrel.getModel(), barrel.getSkin(), barrel.getTier());
            ((IBarrelModifiable) stack.getCapability(YabbaCommon.BARREL_CAPABILITY, null)).copyFrom(barrel);
            return stack;
        }

        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        list.add(createStack(ModelBarrel.INSTANCE, YabbaRegistry.DEFAULT_SKIN, Tier.WOOD));
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileBarrel();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockHorizontal.FACING, ROTATION, MODEL, SKIN);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta)).withProperty(ROTATION, EnumRotation.VALUES[(meta >> 2) & 3]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(ROTATION).ordinal() << 2) | state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if(tile instanceof TileBarrel)
        {
            IBarrel barrel = tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
            return state.withProperty(SKIN, barrel.getSkin()).withProperty(MODEL, barrel.getModel());
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
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        TileEntity tile = world.getTileEntity(pos);

        if(tile != null && tile.hasCapability(YabbaCommon.BARREL_CAPABILITY, null) && tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null).getFlag(IBarrel.FLAG_OBSIDIAN_SHELL))
        {
            return Float.MAX_VALUE;
        }

        return 8F;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity)
    {
        IBlockState parent = state.getValue(SKIN).getState();
        return parent.getBlock().getSoundType(parent, world, pos, entity);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        //IBlockState parent = state.getValue(SKIN).getParentState();
        //return parent.getBlock().canRenderInLayer(parent, layer);
        return layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack)
    {
        return getDefaultState().withProperty(ROTATION, EnumRotation.getRotationFromEntity(pos, placer)).withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(side == BlockBarrel.normalizeFacing(state))
        {
            if(!worldIn.isRemote)
            {
                TileEntity tile = worldIn.getTileEntity(pos);

                if(tile instanceof TileBarrel)
                {
                    Long l = LAST_CLICK_MAP.get(playerIn.getGameProfile().getId());
                    long time = worldIn.getTotalWorldTime();
                    ((TileBarrel) tile).onRightClick(playerIn, state, heldItem, hitX, hitY, hitZ, side, l == null ? Long.MAX_VALUE : (time - l));

                    if(ItemStackTools.isEmpty(heldItem) || !heldItem.hasCapability(YabbaCommon.UPGRADE_CAPABILITY, null))
                    {
                        LAST_CLICK_MAP.put(playerIn.getGameProfile().getId(), time);
                    }
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        if(side == null)
        {
            return false;
        }

        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileBarrel && ((TileBarrel) tile).canConnectRedstone(side);
    }

    @Override
    @Deprecated
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        TileEntity tile = blockAccess.getTileEntity(pos);
        return tile instanceof TileBarrel ? ((TileBarrel) tile).redstoneOutput(side) : 0;
    }

    @Override
    @Deprecated
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        TileEntity tile = blockAccess.getTileEntity(pos);
        return tile instanceof TileBarrel ? ((TileBarrel) tile).redstoneOutput(side) : 0;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if(tile != null && tile.hasCapability(YabbaCommon.BARREL_CAPABILITY, null))
        {
            IBarrel barrel = tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
            return barrel.getModel().getAABB(state, world, pos, barrel);
        }

        return FULL_BLOCK_AABB;
    }
}