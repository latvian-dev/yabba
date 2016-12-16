package com.latmod.yabba.client;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.block.EnumTier;
import com.latmod.yabba.item.EnumUpgrade;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.block.BlockPlanks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by LatvianModder on 06.12.2016.
 */
public class YabbaClient extends YabbaCommon
{
    @Override
    public void preInit()
    {
        super.preInit();

        for(EnumUpgrade type : EnumUpgrade.VALUES)
        {
            registerModel(YabbaItems.UPGRADE, type.metadata, "upgrade_" + type.getUpgradeName());
        }

        for(BlockPlanks.EnumType type : BlockPlanks.EnumType.values())
        {
            Item item = Item.getItemFromBlock(YabbaItems.BARRELS.get(type));
            String name = "barrel_" + type.getName();

            for(int i = 0; i < EnumTier.VALUES.length; i++)
            {
                registerModel(item, i, name);
            }
        }

        registerModel(Item.getItemFromBlock(YabbaItems.ANTIBARREL), 0, "antibarrel");

        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());
    }

    private void registerModel(Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Yabba.MOD_ID, id), "inventory"));
    }
}