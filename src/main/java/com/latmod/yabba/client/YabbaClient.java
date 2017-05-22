package com.latmod.yabba.client;

import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.Tier;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.client.gui.GuiSelectModel;
import com.latmod.yabba.client.gui.GuiSelectSkin;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * @author LatvianModder
 */
public class YabbaClient extends YabbaCommon
{
    public static ItemStack STACKS_FOR_GUI[][];

    @Override
    public void preInit()
    {
        super.preInit();
        ModelLoader.setCustomStateMapper(YabbaItems.BARREL, new StateMap.Builder().ignore(BlockHorizontal.FACING, BlockBarrel.MODEL, BlockBarrel.SKIN).build());
        ModelLoaderRegistry.registerLoader(new YabbaModels());
        Item barrelItem = Item.getItemFromBlock(YabbaItems.BARREL);
        ModelResourceLocation barrelItemModel = new ModelResourceLocation(YabbaItems.BARREL.getRegistryName(), "normal");
        ModelLoader.registerItemVariants(barrelItem, barrelItemModel);
        ModelLoader.setCustomMeshDefinition(barrelItem, stack -> barrelItemModel);

        registerModel(Item.getItemFromBlock(YabbaItems.ANTIBARREL), 0, "antibarrel", "inventory");

        for(EnumUpgrade type : EnumUpgrade.VALUES)
        {
            registerModel(YabbaItems.UPGRADE, type.metadata, "upgrade/" + type.getName(), "inventory");
        }

        registerModel(YabbaItems.PAINTER, 0, "painter", "inventory");
        registerModel(YabbaItems.HAMMER, 0, "hammer", "inventory");

        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());

        STACKS_FOR_GUI = new ItemStack[YabbaRegistry.ALL_MODELS.size()][YabbaRegistry.ALL_SKINS.size()];

        for(int m = 0; m < YabbaRegistry.ALL_MODELS.size(); m++)
        {
            for(int s = 0; s < YabbaRegistry.ALL_SKINS.size(); s++)
            {
                STACKS_FOR_GUI[m][s] = YabbaItems.BARREL.createStack(YabbaRegistry.ALL_MODELS.get(m).getName(), YabbaRegistry.ALL_SKINS.get(s).getName(), Tier.WOOD);
            }
        }
    }

    private void registerModel(Item item, int meta, String id, String v)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Yabba.MOD_ID + ':' + id + '#' + v));
    }

    @Override
    public void openModelGui()
    {
        new GuiSelectModel().openGui();
    }

    @Override
    public void openSkinGui()
    {
        new GuiSelectSkin().openGui();
    }
}