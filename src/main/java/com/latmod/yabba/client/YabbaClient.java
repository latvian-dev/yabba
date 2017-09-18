package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.TextureSet;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.icon.DrawableItem;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.api.YabbaSkinsEvent;
import com.latmod.yabba.block.BlockItemBarrel;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.client.gui.GuiSelectModel;
import com.latmod.yabba.client.gui.GuiSelectSkin;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class YabbaClient extends YabbaCommon
{
	public static final Collection<ResourceLocation> TEXTURES = new HashSet<>();
	private static final Map<String, BarrelModel> MODELS = new LinkedHashMap<>();
	private static final Map<String, BarrelSkin> SKINS = new HashMap<>();
	public static final List<BarrelModel> ALL_MODELS = new ArrayList<>();
	public static final List<BarrelSkin> ALL_SKINS = new ArrayList<>();
	private static BarrelModel DEFAULT_MODEL;
	private static BarrelSkin DEFAULT_SKIN;
	private static final Map<String, String> VARIABLES = new HashMap<>();

	private static final YabbaSkinsEvent.Callback REGISTER_SKIN = skin ->
	{
		SKINS.put(skin.id, skin);
		TEXTURES.addAll(skin.textures.getTextures());
	};

	private static String parseVariableString(String s)
	{
		if (!VARIABLES.isEmpty())
		{
			for (Map.Entry<String, String> entry : VARIABLES.entrySet())
			{
				s = s.replace('$' + entry.getKey(), entry.getValue());
			}
		}

		return s;
	}

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
					for (JsonElement element : JsonUtils.fromJson(resource).getAsJsonArray())
					{
						try
						{
							JsonObject modelFile = JsonUtils.fromJson(manager.getResource(new ResourceLocation(domain, "yabba_models/" + element.getAsString() + ".json"))).getAsJsonObject();
							BarrelModel model = new BarrelModel(new ResourceLocation(domain, element.getAsString()), modelFile);
							MODELS.put(model.id, model);

							for (TextureSet textureSet : model.textures.values())
							{
								TEXTURES.addAll(textureSet.getTextures());
							}
						}
						catch (Exception ex1)
						{
						}
					}
				}
			}
			catch (Exception ex)
			{
				if (!(ex instanceof FileNotFoundException))
				{
					ex.printStackTrace();
				}
			}

			try
			{
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "yabba_models/_skins.json")))
				{
					parseSkinsJson(JsonUtils.fromJson(resource).getAsJsonArray());
					VARIABLES.clear();
				}
			}
			catch (Exception ex)
			{
				if (!(ex instanceof FileNotFoundException))
				{
					ex.printStackTrace();
				}
			}
		}

		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
		{
			FluidStack stack = new FluidStack(fluid, 1000);
			String displayName = stack.getLocalizedName();
			Color4I color = Color4I.rgba(fluid.getColor(stack));

			if (color.equals(Color4I.WHITE))
			{
				color = Color4I.NONE;
			}

			ResourceLocation still = fluid.getStill(stack);

			BarrelSkin skin = new BarrelSkin(fluid.getName() + "_still", TextureSet.of("all=" + still));
			skin.displayName = StringUtils.translate("lang.fluid.still", displayName);
			skin.color = color;
			skin.layer = BlockRenderLayer.TRANSLUCENT;
			REGISTER_SKIN.addSkin(skin);

			ResourceLocation flowing = fluid.getFlowing(stack);

			if (!still.equals(flowing))
			{
				skin = new BarrelSkin(fluid.getName() + "_flowing", TextureSet.of("up&down=" + still + ",all=" + flowing));
				skin.displayName = StringUtils.translate("lang.fluid.flowing", displayName);
				skin.color = color;
				skin.layer = BlockRenderLayer.TRANSLUCENT;
				REGISTER_SKIN.addSkin(skin);
			}
		}

		new YabbaSkinsEvent(REGISTER_SKIN).post();

		for (BarrelSkin skin : SKINS.values())
		{
			if (skin.displayName.isEmpty() && skin.state != Blocks.AIR.getDefaultState())
			{
				try
				{
					skin.displayName = new ItemStack(skin.state.getBlock(), 1, skin.state.getBlock().getMetaFromState(skin.state)).getDisplayName();
				}
				catch (Exception ex)
				{
				}
			}

			if (skin.displayName.isEmpty() || skin.displayName.contains("air"))
			{
				skin.displayName = "";
			}

			if (skin.icon.isEmpty())
			{
				skin.icon = new DrawableItem(((BlockItemBarrel) YabbaItems.ITEM_BARREL).createStack(YabbaItems.ITEM_BARREL.getDefaultState(), Yabba.MOD_ID + ":block", skin.id, Tier.WOOD));
			}
		}

		ALL_MODELS.addAll(MODELS.values());
		DEFAULT_MODEL = MODELS.get(DEFAULT_MODEL_ID);

		if (DEFAULT_MODEL == null)
		{
			DEFAULT_MODEL = ALL_MODELS.isEmpty() ? null : ALL_MODELS.get(0);
		}

		Yabba.LOGGER.info("Models: " + ALL_MODELS.size());

		if (CommonUtils.DEV_ENV)
		{
			for (BarrelModel model : ALL_MODELS)
			{
				Yabba.LOGGER.info("-- " + model.id + " :: " + model);
			}
		}

		ALL_SKINS.addAll(SKINS.values());
		ALL_SKINS.sort(StringUtils.ID_COMPARATOR);

		DEFAULT_SKIN = SKINS.get(DEFAULT_SKIN_ID);

		if (DEFAULT_SKIN == null)
		{
			DEFAULT_SKIN = ALL_SKINS.isEmpty() ? null : ALL_SKINS.get(0);
		}

		Yabba.LOGGER.info("Skins: " + ALL_SKINS.size());

		if (CommonUtils.DEV_ENV)
		{
			for (BarrelSkin skin : ALL_SKINS)
			{
				Yabba.LOGGER.info("-- " + skin.id + " :: " + skin);
			}
		}
	}

	private static Iterable<List<String>> getIterator(JsonElement e)
	{
		List<List<String>> l = new ArrayList<>();

		if (e.isJsonArray())
		{
			for (JsonElement e1 : e.getAsJsonArray())
			{
				if (e1.isJsonArray())
				{
					List<String> list = new ArrayList<>();

					for (JsonElement e2 : e1.getAsJsonArray())
					{
						list.add(e2.getAsString());
					}

					l.add(list);
				}
				else
				{
					l.add(Collections.singletonList(parseVariableString(e1.getAsString())));
				}
			}

			return l;
		}

		String s = parseVariableString(e.getAsString());

		if (s.contains(".."))
		{
			String[] s1 = s.split("..");
			int min = Integer.parseInt(s1[0]);
			int max = Integer.parseInt(s1[1]);

			for (int i = min; i <= max; i++)
			{
				l.add(Collections.singletonList(Integer.toString(i)));
			}

			return l;
		}

		String[] s1 = s.split("#");
		Block block = Block.REGISTRY.getObject(new ResourceLocation(s1[0]));
		IProperty<?> property = block.getBlockState().getProperty(s1[1]);
		if (property != null)
		{
			for (Object o : property.getAllowedValues())
			{
				l.add(Collections.singletonList(property.getName(CommonUtils.cast(o))));
			}
		}

		return l;
	}

	private static void parseSkinsJson(JsonArray array)
	{
		for (JsonElement element : array)
		{
			try
			{
				if (!element.isJsonObject())
				{
					continue;
				}

				JsonObject json = element.getAsJsonObject();

				if (json.has("add"))
				{
					ResourceLocation id = new ResourceLocation(parseVariableString(json.get("add").getAsString()));
					TextureSet textures;

					if (json.has("textures"))
					{
						textures = TextureSet.of(parseVariableString(json.get("textures").getAsString()));
					}
					else
					{
						textures = TextureSet.of("all=" + id.getResourceDomain() + ":blocks/" + id.getResourcePath());
					}

					BarrelSkin skin = new BarrelSkin(id.toString(), textures);

					if (json.has("name"))
					{
						skin.displayName = JsonUtils.deserializeTextComponent(json.get("name")).getFormattedText();
					}

					if (json.has("icon"))
					{
						skin.icon = Icon.getIcon(json.get("icon"));
					}

					if (json.has("layer"))
					{
						skin.layer = ClientUtils.BLOCK_RENDER_LAYER_NAME_MAP.get(json.get("layer").getAsString());
					}

					if (json.has("color"))
					{
						skin.color = Color4I.fromJson(json.get("color"));
					}

					skin.state = CommonUtils.getStateFromName(json.has("state") ? parseVariableString(json.get("state").getAsString()) : skin.id);
					REGISTER_SKIN.addSkin(skin);
				}
				else if (json.has("set"))
				{
					VARIABLES.put(parseVariableString(json.get("set").getAsString()), parseVariableString(json.get("value").getAsString()));
				}
				else if (json.has("for"))
				{
					JsonArray a = json.get("for").getAsJsonArray();

					if (a.size() == 3)
					{
						String key = a.get(0).getAsString();

						for (List<String> value : getIterator(a.get(1)))
						{
							if (value.size() == 1)
							{
								VARIABLES.put(key, parseVariableString(value.get(0)));
							}
							else
							{
								for (int i = 0; i < value.size(); i++)
								{
									VARIABLES.put(key + "" + i, parseVariableString(value.get(i)));
								}
							}

							parseSkinsJson(JsonUtils.toArray(a.get(2)));
						}
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

	}

	@Override
	public void preInit()
	{
		super.preInit();
		YabbaClientConfig.sync();
	}

	@Override
	public void postInit()
	{
		super.postInit();
		ClientUtils.MC.getBlockColors().registerBlockColorHandler(BarrelModelLoader.INSTANCE, YabbaItems.ITEM_BARREL);
		ClientUtils.MC.getItemColors().registerItemColorHandler(BarrelModelLoader.INSTANCE, YabbaItems.ITEM_BARREL);
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

	public static BarrelSkin getSkin(String id)
	{
		if (id.isEmpty())
		{
			return DEFAULT_SKIN;
		}

		BarrelSkin skin = SKINS.get(id);
		return skin == null ? DEFAULT_SKIN : skin;
	}

	public static BarrelModel getModel(String id)
	{
		if (id.isEmpty())
		{
			return DEFAULT_MODEL;
		}

		BarrelModel model = MODELS.get(id);
		return model == null ? DEFAULT_MODEL : model;
	}
}