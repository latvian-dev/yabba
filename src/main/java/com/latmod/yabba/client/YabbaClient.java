package com.latmod.yabba.client;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
        Item barrelItem = Item.getItemFromBlock(YabbaItems.BARREL);
        List<ResourceLocation> variants = new ArrayList<>();

        for(IBarrelModel model : YabbaRegistry.ALL_MODELS)
        {
            for(IBarrelSkin skin : YabbaRegistry.ALL_SKINS)
            {
                variants.add(new ModelResourceLocation(YabbaItems.BARREL.getRegistryName(), "facing=north,model=" + model.getName() + ",skin=" + skin.getName()));
            }
        }

        ModelLoader.registerItemVariants(barrelItem, variants.toArray(new ResourceLocation[variants.size()]));
        ModelLoader.setCustomMeshDefinition(barrelItem, new BarrelItemMeshDefinition(YabbaItems.BARREL.getRegistryName()));

        registerModel(Item.getItemFromBlock(YabbaItems.ANTIBARREL), 0, "antibarrel", "inventory");

        for(EnumUpgrade type : EnumUpgrade.VALUES)
        {
            registerModel(YabbaItems.UPGRADE, type.metadata, "upgrade/" + type.getUpgradeName(), "inventory");
        }

        registerModel(YabbaItems.PAINTER, 0, "painter", "inventory");

        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());
    }

    private void registerModel(@Nullable Item item, int meta, String id, String v)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Yabba.MOD_ID, id), v));
    }
}