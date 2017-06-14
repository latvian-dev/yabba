package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.IconSet;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.Tier;
import com.latmod.yabba.api.events.YabbaModelsEvent;
import com.latmod.yabba.api.events.YabbaSkinsEvent;
import com.latmod.yabba.client.gui.GuiSelectModel;
import com.latmod.yabba.client.gui.GuiSelectSkin;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.BarrelSkin;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class YabbaClient extends YabbaCommon implements YabbaModelsEvent.YabbaModelRegistry, YabbaSkinsEvent.YabbaSkinRegistry
{
	public static final ModelResourceLocation ITEM_BARREL_MODEL = new ModelResourceLocation(YabbaItems.BARREL.getRegistryName(), "normal");
	private static final Map<String, IBarrelModel> MODELS = new HashMap<>();
	private static final Map<String, IBarrelSkin> SKINS = new HashMap<>();
	public static final List<IBarrelModel> ALL_MODELS = new ArrayList<>();
	public static final List<IBarrelSkin> ALL_SKINS = new ArrayList<>();
	public static ItemStack STACKS_FOR_GUI[][];
	private static IBarrelModel DEFAULT_MODEL;
	private static IBarrelSkin DEFAULT_SKIN;

	@Override
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.post(new YabbaModelsEvent(this));
		addModel(new ModelBarrel());
		ALL_MODELS.addAll(MODELS.values());
		ALL_MODELS.sort(StringUtils.ID_COMPARATOR);
		DEFAULT_MODEL = getModel(DEFAULT_MODEL_ID);
		Yabba.LOGGER.info("Models: " + ALL_MODELS.size());

		MinecraftForge.EVENT_BUS.post(new YabbaSkinsEvent(this));
		addSkin(DEFAULT_SKIN_STATE, "all=blocks/planks_oak");
		ALL_SKINS.addAll(SKINS.values());
		ALL_SKINS.sort(StringUtils.ID_COMPARATOR);
		DEFAULT_SKIN = getSkin(DEFAULT_SKIN_ID);
		Yabba.LOGGER.info("Skins: " + ALL_SKINS.size());

		super.preInit();

		ModelLoaderRegistry.registerLoader(new BarrelModelLoader());
		BarrelModelLoader.loadFor(YabbaItems.BARREL);

		registerModel(Item.getItemFromBlock(YabbaItems.ANTIBARREL), 0, "antibarrel", "inventory");

		for (EnumUpgrade type : EnumUpgrade.VALUES)
		{
			registerModel(YabbaItems.UPGRADE, type.metadata, "upgrade/" + type.getName(), "inventory");
		}

		registerModel(YabbaItems.PAINTER, 0, "painter", "inventory");
		registerModel(YabbaItems.HAMMER, 0, "hammer", "inventory");

		ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());

		STACKS_FOR_GUI = new ItemStack[ALL_MODELS.size()][ALL_SKINS.size()];

		for (int m = 0; m < ALL_MODELS.size(); m++)
		{
			for (int s = 0; s < ALL_SKINS.size(); s++)
			{
				STACKS_FOR_GUI[m][s] = YabbaItems.BARREL.createStack(ALL_MODELS.get(m).getName(), ALL_SKINS.get(s).getName(), Tier.WOOD);
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

	@Override
	public void addSkin(IBarrelSkin skin)
	{
		SKINS.put(skin.getName(), skin);
	}

	@Override
	public IBarrelSkin addSkin(IBlockState parentState, String icons, String uname)
	{
		IBarrelSkin skin = new BarrelSkin(false, parentState, new IconSet(icons), uname);
		addSkin(skin);
		return skin;
	}

	@Override
	public void addModel(IBarrelModel model)
	{
		MODELS.put(model.getName(), model);
	}

	public static IBarrelSkin getSkin(String id)
	{
		IBarrelSkin skin = id.isEmpty() ? null : SKINS.get(id);
		return skin == null ? DEFAULT_SKIN : skin;
	}

	public static IBarrelModel getModel(String id)
	{
		IBarrelModel model = id.isEmpty() ? null : MODELS.get(id);
		return model == null ? DEFAULT_MODEL : model;
	}
}