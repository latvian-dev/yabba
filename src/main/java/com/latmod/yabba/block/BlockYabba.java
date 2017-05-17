package com.latmod.yabba.block;

import com.feed_the_beast.ftbl.api.block.IBlockWithItem;
import com.feed_the_beast.ftbl.lib.block.ItemBlockBase;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

/**
 * @author LatvianModder
 */
public class BlockYabba extends Block implements IBlockWithItem
{
    public BlockYabba(String id, Material blockMaterialIn, MapColor blockMapColorIn)
    {
        super(blockMaterialIn, blockMapColorIn);
        setRegistryName(Yabba.MOD_ID + ':' + id);
        setUnlocalizedName(Yabba.MOD_ID + '.' + id);
        setHardness(1.8F);
        setResistance(3F);
        setCreativeTab(YabbaCommon.TAB);
    }

    @Override
    public ItemBlock createItemBlock()
    {
        return new ItemBlockBase(this, false);
    }
}