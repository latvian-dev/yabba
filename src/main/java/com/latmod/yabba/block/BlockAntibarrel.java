package com.latmod.yabba.block;

import com.latmod.yabba.tile.TileAntibarrel;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class BlockAntibarrel extends BlockBarrelBase
{
    public BlockAntibarrel()
    {
        super("antibarrel", Material.ROCK, MapColor.NETHERRACK);
        setHardness(4F);
        setResistance(1000F);
    }

    @Override
    public void dropItem(ItemStack itemStack, @Nullable TileEntity tile)
    {
        if(tile instanceof TileAntibarrel)
        {
            NBTTagCompound nbttagcompound = tile.writeToNBT(new NBTTagCompound());
            itemStack.setTagInfo("BlockEntityTag", nbttagcompound);
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            NBTTagList nbttaglist = new NBTTagList();
            nbttaglist.appendTag(new NBTTagString("(+NBT)"));
            nbttagcompound1.setTag("Lore", nbttaglist);
            itemStack.setTagInfo("display", nbttagcompound1);
        }
    }

    @Override
    public void placeFromItem(ItemStack stack, @Nullable TileEntity tile)
    {
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileAntibarrel();
    }
}