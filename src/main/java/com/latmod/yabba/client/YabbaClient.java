package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.IconSet;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.Tier;
import com.latmod.yabba.api.events.YabbaModelsEvent;
import com.latmod.yabba.api.events.YabbaSkinsEvent;
import com.latmod.yabba.client.gui.GuiSelectModel;
import com.latmod.yabba.client.gui.GuiSelectSkin;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.util.BarrelSkin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class YabbaClient extends YabbaCommon implements YabbaModelsEvent.YabbaModelRegistry, YabbaSkinsEvent.YabbaSkinRegistry
{
	public static final Collection<ResourceLocation> TEXTURES = new HashSet<>();
	private static final Map<String, IBarrelModel> MODELS = new HashMap<>();
	private static final Map<String, IBarrelSkin> SKINS = new HashMap<>();
	public static final List<IBarrelModel> ALL_MODELS = new ArrayList<>();
	public static final List<IBarrelSkin> ALL_SKINS = new ArrayList<>();
	public static ItemStack STACKS_FOR_GUI[][];
	private static final IBarrelModel DEFAULT_MODEL = new ModelBarrel();
	private static final IBarrelSkin DEFAULT_SKIN = new BarrelSkin(false, DEFAULT_SKIN_STATE, new IconSet("all=blocks/planks_oak"), "");

	@Override
	public void loadModelsAndSkins()
	{
		TEXTURES.clear();
		MODELS.clear();
		ALL_MODELS.clear();
		SKINS.clear();
		ALL_SKINS.clear();

		MinecraftForge.EVENT_BUS.post(new YabbaModelsEvent(this));
		addModel(DEFAULT_MODEL);
		ALL_MODELS.addAll(MODELS.values());
		ALL_MODELS.sort(StringUtils.ID_COMPARATOR);
		Yabba.LOGGER.info("Models: " + ALL_MODELS.size());

		MinecraftForge.EVENT_BUS.post(new YabbaSkinsEvent(this));
		addSkin(DEFAULT_SKIN);
		ALL_SKINS.addAll(SKINS.values());
		ALL_SKINS.sort(StringUtils.ID_COMPARATOR);
		Yabba.LOGGER.info("Skins: " + ALL_SKINS.size());

		STACKS_FOR_GUI = new ItemStack[ALL_MODELS.size()][ALL_SKINS.size()];

		for (int m = 0; m < ALL_MODELS.size(); m++)
		{
			for (int s = 0; s < ALL_SKINS.size(); s++)
			{
				STACKS_FOR_GUI[m][s] = BARREL.createStack(ALL_MODELS.get(m).getName(), ALL_SKINS.get(s).getName(), Tier.WOOD);
			}
		}
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
		TEXTURES.addAll(skin.getTextures().getTextures());
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
		TEXTURES.addAll(model.getExtraTextures());
	}

	public static IBarrelSkin getSkin(@Nullable String id)
	{
		IBarrelSkin skin = (id == null || id.isEmpty()) ? null : SKINS.get(id);
		return skin == null ? DEFAULT_SKIN : skin;
	}

	public static IBarrelModel getModel(@Nullable String id)
	{
		IBarrelModel model = (id == null || id.isEmpty()) ? null : MODELS.get(id);
		return model == null ? DEFAULT_MODEL : model;
	}
}