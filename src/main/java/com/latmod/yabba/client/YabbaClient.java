package com.latmod.yabba.client;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.item.EnumUpgrade;
import com.latmod.yabba.tile.TileBarrel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 06.12.2016.
 */
public class YabbaClient extends YabbaCommon
{
    @Override
    public void preInit()
    {
        super.preInit();

        ModelLoaderRegistry.registerLoader(new YabbaModels());
        ModelLoader.setCustomStateMapper(YabbaItems.BARREL, new StateMap.Builder().ignore(BlockBarrel.VARIANT).build());
        ModelLoader.setCustomStateMapper(YabbaItems.CRATE, new YabbaModels.CrateStatemapper());

        registerModel(Item.getItemFromBlock(YabbaItems.BARREL), 0, "barrel");
        registerModel(Item.getItemFromBlock(YabbaItems.CRATE), 0, "crate");
        registerModel(Item.getItemFromBlock(YabbaItems.ANTIBARREL), 0, "antibarrel");

        for(EnumUpgrade type : EnumUpgrade.VALUES)
        {
            registerModel(YabbaItems.UPGRADE, type.metadata, "upgrade/" + type.getUpgradeName());
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());
    }

    private void registerModel(@Nullable Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Yabba.MOD_ID, id), "inventory"));
    }
}