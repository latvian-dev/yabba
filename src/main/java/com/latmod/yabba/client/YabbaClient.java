package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.ItemIcon;
import com.feed_the_beast.ftblib.lib.io.DataReader;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.misc.TextureSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaItems;
import com.latmod.yabba.api.BarrelSkin;
import com.latmod.yabba.api.YabbaSkinsEvent;
import com.latmod.yabba.tile.TileDecorativeBlock;
import com.latmod.yabba.util.BarrelLook;
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	private static final Map<String, BarrelSkin> SKINS = new HashMap<>();
	public static final List<BarrelSkin> ALL_SKINS = new ArrayList<>();
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
		SKINS.clear();
		ALL_SKINS.clear();

		IResourceManager manager = ClientUtils.MC.getResourceManager();

		for (EnumBarrelModel id : EnumBarrelModel.NAME_MAP)
		{
			try
			{
				JsonObject modelFile = DataReader.get(manager.getResource(new ResourceLocation(Yabba.MOD_ID, "yabba_models/" + id.getName() + ".json"))).json().getAsJsonObject();
				BarrelModel model = new BarrelModel(id, modelFile);
				id.setModel(model);

				for (TextureSet textureSet : model.textures.values())
				{
					TEXTURES.addAll(textureSet.getTextures());
				}
			}
			catch (Exception ex1)
			{
			}
		}

		for (String domain : manager.getResourceDomains())
		{
			try
			{
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "yabba_models/_skins.json")))
				{
					parseSkinsJson(DataReader.get(resource).json().getAsJsonArray());
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
				color = Icon.EMPTY;
			}

			ResourceLocation still = fluid.getStill(stack);

			BarrelSkin skin = new BarrelSkin(fluid.getName() + "_still", TextureSet.of("all=" + still));
			skin.displayName = I18n.format("lang.fluid.still", displayName);
			skin.color = color;
			skin.layer = BlockRenderLayer.TRANSLUCENT;
			REGISTER_SKIN.addSkin(skin);

			ResourceLocation flowing = fluid.getFlowing(stack);

			if (!still.equals(flowing))
			{
				skin = new BarrelSkin(fluid.getName() + "_flowing", TextureSet.of("up&down=" + still + ",all=" + flowing));
				skin.displayName = I18n.format("lang.fluid.flowing", displayName);
				skin.color = color;
				skin.layer = BlockRenderLayer.TRANSLUCENT;
				REGISTER_SKIN.addSkin(skin);
			}
		}

		new YabbaSkinsEvent(REGISTER_SKIN).post();

		for (BarrelSkin skin : SKINS.values())
		{
			if (skin.displayName.isEmpty() && skin.state != BlockUtils.AIR_STATE)
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
				TileDecorativeBlock tile = new TileDecorativeBlock();
				tile.setLook(BarrelLook.get(EnumBarrelModel.BLOCK, skin.id), false);
				ItemStack stack = new ItemStack(YabbaItems.DECORATIVE_BLOCK_ITEM);
				tile.writeToPickBlock(stack);
				skin.icon = ItemIcon.getItemIcon(stack);
			}
		}

		ALL_SKINS.addAll(SKINS.values());
		ALL_SKINS.sort(StringUtils.ID_COMPARATOR);

		DEFAULT_SKIN = SKINS.get(BarrelLook.DEFAULT_SKIN_ID);

		if (DEFAULT_SKIN == null)
		{
			DEFAULT_SKIN = ALL_SKINS.isEmpty() ? null : ALL_SKINS.get(0);
		}

		Yabba.LOGGER.info("Skins: " + ALL_SKINS.size());

		for (EnumBarrelModel id : EnumBarrelModel.NAME_MAP)
		{
			TileDecorativeBlock tile = new TileDecorativeBlock();
			tile.setLook(BarrelLook.get(id, ""), false);
			ItemStack stack = new ItemStack(YabbaItems.DECORATIVE_BLOCK_ITEM);
			tile.writeToPickBlock(stack);
			id.getModel().icon = ItemIcon.getItemIcon(stack);
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
						textures = TextureSet.of("all=" + id.getNamespace() + ":blocks/" + id.getPath());
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

					skin.state = BlockUtils.getStateFromName(json.has("state") ? parseVariableString(json.get("state").getAsString()) : skin.id);
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
		YabbaClientConfig.sync();
	}

	@Override
	public void postInit()
	{
		Block[] blocks = {YabbaItems.ITEM_BARREL, YabbaItems.DECORATIVE_BLOCK};
		ClientUtils.MC.getBlockColors().registerBlockColorHandler(BarrelModelLoader.INSTANCE, blocks);
		ClientUtils.MC.getItemColors().registerItemColorHandler(BarrelModelLoader.INSTANCE, blocks);
	}

	public static BarrelSkin getSkin(@Nullable String id)
	{
		if (id == null || id.isEmpty())
		{
			return DEFAULT_SKIN;
		}

		BarrelSkin skin = SKINS.get(id);
		return skin == null ? DEFAULT_SKIN : skin;
	}
}