package com.latmod.yabba.block;

import com.feed_the_beast.ftbl.lib.block.BlockBase;
import com.feed_the_beast.ftbl.lib.block.ItemBlockBase;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

/**
 * Created by LatvianModder on 19.01.2017.
 */
public class BlockYabba extends BlockBase
{
    public BlockYabba(String id, Material blockMaterialIn, MapColor blockMapColorIn)
    {
        super(Yabba.MOD_ID + ':' + id, blockMaterialIn, blockMapColorIn);
        setCreativeTab(YabbaCommon.TAB);
    }

    @Override
    public ItemBlock createItemBlock()
    {
        return new ItemBlockBase(this, false);
    }
}