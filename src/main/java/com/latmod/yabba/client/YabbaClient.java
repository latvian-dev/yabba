package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.IconSet;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.block.BlockItemBarrel;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.client.gui.GuiSelectModel;
import com.latmod.yabba.client.gui.GuiSelectSkin;
import com.latmod.yabba.item.YabbaItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class YabbaClient extends YabbaCommon
{
	public static final Collection<ResourceLocation> TEXTURES = new HashSet<>();
	private static final Map<ResourceLocation, BarrelModel> MODELS = new HashMap<>();
	private static final Map<IBlockState, BarrelSkin> SKINS = new HashMap<>();
	public static final List<BarrelModel> ALL_MODELS = new ArrayList<>();
	public static final List<BarrelSkin> ALL_SKINS = new ArrayList<>();
	private static BarrelModel DEFAULT_MODEL;
	private static BarrelSkin DEFAULT_SKIN;
	public static ItemStack STACKS_FOR_GUI[][];

	public static void loadModelsAndSkins()
	{
		TEXTURES.clear();
		MODELS.clear();
		SKINS.clear();
		ALL_MODELS.clear();
		ALL_SKINS.clear();

		IResourceManager manager = ClientUtils.MC.getResourceManager();

		for (String domain : manager.getResourceDomains())
		{
			try
			{
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "yabba_models/_index.json")))
				{
					try
					{
						for (JsonElement element : JsonUtils.fromJson(resource).getAsJsonArray())
						{
							JsonObject modelFile = JsonUtils.fromJson(manager.getResource(new ResourceLocation(domain, "yabba_models/" + element.getAsString() + ".json"))).getAsJsonObject();
							BarrelModel model = new BarrelModel(new ResourceLocation(domain, element.getAsString()), modelFile);
							MODELS.put(model.id, model);

							for (IconSet iconSet : model.textures.values())
							{
								TEXTURES.addAll(iconSet.getTextures());
							}
						}
					}
					catch (Exception ex1)
					{
						if (CommonUtils.DEV_ENV)
						{
							ex1.printStackTrace();
						}
					}
				}
			}
			catch (Exception ex)
			{
			}
		}

		for (Block block : Block.REGISTRY)
		{
			try
			{
				NonNullList<ItemStack> stacks = NonNullList.create();
				block.getSubBlocks(CreativeTabs.SEARCH, stacks);

				for (IBlockState state : block.getBlockState().getValidStates())
				{
					if (!block.hasTileEntity(state) && state.isFullCube())
					{
						Item item = block.getItemDropped(state, MathUtils.RAND, 0);

						if (item != Items.AIR)
						{
							BarrelSkin skin = new BarrelSkin(state, new ItemStack(item, 1, block.damageDropped(state)), IconSet.of(state));
							SKINS.put(state, skin);
							TEXTURES.addAll(skin.iconSet.getTextures());
						}
					}
				}
			}
			catch (Exception ex)
			{
				if (CommonUtils.DEV_ENV)
				{
					ex.printStackTrace();
				}
			}
		}

		ALL_MODELS.addAll(MODELS.values());
		ALL_MODELS.sort(StringUtils.ID_COMPARATOR);
		DEFAULT_MODEL = MODELS.get(DEFAULT_MODEL_ID);

		if (DEFAULT_MODEL == null)
		{
			DEFAULT_MODEL = ALL_MODELS.isEmpty() ? null : ALL_MODELS.get(0);
		}

		Yabba.LOGGER.info("Models: " + ALL_MODELS.size());

		ALL_SKINS.addAll(SKINS.values());
		ALL_SKINS.sort(StringUtils.ID_COMPARATOR);

		DEFAULT_SKIN = SKINS.get(DEFAULT_SKIN_ID);

		if (DEFAULT_SKIN == null)
		{
			DEFAULT_SKIN = ALL_SKINS.isEmpty() ? null : ALL_SKINS.get(0);
		}

		Yabba.LOGGER.info("Skins: " + ALL_SKINS.size());

		STACKS_FOR_GUI = new ItemStack[ALL_MODELS.size()][ALL_SKINS.size()];

		for (int m = 0; m < ALL_MODELS.size(); m++)
		{
			for (int s = 0; s < ALL_SKINS.size(); s++)
			{
				STACKS_FOR_GUI[m][s] = ((BlockItemBarrel) YabbaItems.ITEM_BARREL).createStack(ALL_MODELS.get(m).id, ALL_SKINS.get(s).state, Tier.WOOD);
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

	public static BarrelSkin getSkin(IBlockState id)
	{
		BarrelSkin skin = SKINS.get(id);
		return skin == null ? DEFAULT_SKIN : skin;
	}

	public static BarrelModel getModel(ResourceLocation id)
	{
		BarrelModel model = MODELS.get(id);
		return model == null ? DEFAULT_MODEL : model;
	}
}