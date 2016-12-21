package com.latmod.yabba.client;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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

        registerBarrel(YabbaItems.BARREL);
        registerBarrel(YabbaItems.CRATE);

        registerModel(Item.getItemFromBlock(YabbaItems.ANTIBARREL), 0, "antibarrel", "inventory");

        for(EnumUpgrade type : EnumUpgrade.VALUES)
        {
            registerModel(YabbaItems.UPGRADE, type.metadata, "upgrade/" + type.getUpgradeName(), "inventory");
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());
    }

    private void registerBarrel(BlockBarrel block)
    {
        Item item = Item.getItemFromBlock(block);

        ResourceLocation[] variants = new ResourceLocation[YabbaRegistry.ALL_BARRELS.size()];

        for(int i = 0; i < variants.length; i++)
        {
            variants[i] = new ModelResourceLocation(block.getRegistryName(), "facing=north,variant=" + YabbaRegistry.ALL_BARRELS.get(i).getName());
        }

        ModelLoader.registerItemVariants(item, variants);
        ModelLoader.setCustomMeshDefinition(item, new BarrelItemMeshDefinition(block.getRegistryName()));
    }

    private void registerModel(@Nullable Item item, int meta, String id, String v)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Yabba.MOD_ID, id), v));
    }
}