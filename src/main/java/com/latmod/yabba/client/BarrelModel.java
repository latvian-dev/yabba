package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.client.ModelBuilder;
import com.feed_the_beast.ftblib.lib.client.SpriteSet;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.misc.TextureSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.yabba.api.BarrelSkin;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class BarrelModel
{
	public static final HashSet<EnumFacing> SET_ALL_FACES = new HashSet<>(Arrays.asList(EnumFacing.VALUES));

	public static HashSet<EnumFacing> parseFaces(JsonArray json)
	{
		HashSet<EnumFacing> set = new HashSet<>();

		for (JsonElement e : json)
		{
			String f = e.getAsString();

			switch (f)
			{
				case "x":
					set.add(EnumFacing.WEST);
					set.add(EnumFacing.EAST);
					break;
				case "y":
					set.add(EnumFacing.DOWN);
					set.add(EnumFacing.UP);
					break;
				case "z":
					set.add(EnumFacing.NORTH);
					set.add(EnumFacing.SOUTH);
					break;
				default:
					set.add(EnumFacing.byName(f));
			}
		}

		return set;
	}

	public static Vec3d parseVec(JsonElement e)
	{
		if (e.isJsonArray())
		{
			JsonArray a = e.getAsJsonArray();

			if (a.size() == 3)
			{
				return new Vec3d(a.get(0).getAsDouble(), a.get(1).getAsDouble(), a.get(2).getAsDouble());
			}
		}
		else if (e.isJsonObject())
		{
			JsonObject o = e.getAsJsonObject();

			if (o.has("x") && o.has("y") && o.has("z"))
			{
				return new Vec3d(o.get("x").getAsDouble(), o.get("y").getAsDouble(), o.get("z").getAsDouble());
			}
		}

		return Vec3d.ZERO;
	}

	public static Pair<Vec3d, Vec3d> parseVecPair(JsonElement e)
	{
		if (e.isJsonArray())
		{
			JsonArray a = e.getAsJsonArray();

			if (a.size() == 6)
			{
				double x0 = a.get(0).getAsDouble();
				double y0 = a.get(1).getAsDouble();
				double z0 = a.get(2).getAsDouble();
				double x1 = a.get(3).getAsDouble();
				double y1 = a.get(4).getAsDouble();
				double z1 = a.get(5).getAsDouble();
				return Pair.of(new Vec3d(x0, y0, z0), new Vec3d(x1, y1, z1));
			}
		}

		return Pair.of(Vec3d.ZERO, Vec3d.ZERO);
	}

	@Nullable
	public static ModelFunction parseFunction(JsonObject json)
	{
		if (json.has("add"))
		{
			switch (json.get("add").getAsString())
			{
				case "cube":
				{
					Cube cube = new Cube();
					Cube.parseCube(cube, json);
					return cube;
				}
				case "inverted_cube":
				case "cube_inverted":
				{
					InvertedCube cube = new InvertedCube();
					Cube.parseCube(cube, json);
					return cube;
				}
			}
		}
		else if (json.has("uvlock"))
		{
			return json.get("uvlock").getAsBoolean() ? SetUvlock.TRUE : SetUvlock.FALSE;
		}
		else if (json.has("shade"))
		{
			return json.get("shade").getAsBoolean() ? SetShade.TRUE : SetShade.FALSE;
		}
		else if (json.has("texture"))
		{
			return new SetTexture(json.get("texture").getAsString());
		}
		else if (json.has("offset"))
		{
			return new SetOffset(parseVec(json.get("offset")));
		}
		else if (json.has("layer"))
		{
			return new SetLayer(ClientUtils.BLOCK_RENDER_LAYER_NAME_MAP.get(json.get("layer").getAsString()));
		}

		return null;
	}

	public interface ModelFunction
	{
		void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer layer);
	}

	public enum SetUvlock implements ModelFunction
	{
		TRUE,
		FALSE;

		@Override
		public void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer layer)
		{
			builder.setUVLocked(this == TRUE);
		}
	}

	public enum SetShade implements ModelFunction
	{
		TRUE,
		FALSE;

		@Override
		public void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer layer)
		{
			builder.setShade(this == TRUE);
		}
	}

	public static class SetTexture implements ModelFunction
	{
		public final String tex;

		public SetTexture(String t)
		{
			tex = t;
		}

		@Override
		public void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer layer)
		{
			model.currentTexture = model.textureMap.get(tex);
		}
	}

	public static class SetOffset implements ModelFunction
	{
		public final Vec3d off;

		public SetOffset(Vec3d o)
		{
			off = o;
		}

		@Override
		public void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer layer)
		{
			model.offset = off;
		}
	}

	public static class SetLayer implements ModelFunction
	{
		public final BlockRenderLayer layer;

		public SetLayer(BlockRenderLayer l)
		{
			layer = l;
		}

		@Override
		public void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer _layer)
		{
			model.currentLayer = layer;
		}
	}

	public static class Cube implements ModelFunction
	{
		public Vec3d from;
		public Vec3d to;
		public HashSet<EnumFacing> faces;

		public static void parseCube(Cube c, JsonObject o)
		{
			if (o.has("from_to"))
			{
				Pair<Vec3d, Vec3d> vecs = parseVecPair(o.get("from_to"));
				c.from = vecs.getLeft();
				c.to = vecs.getRight();
			}
			else if (o.has("pos_size"))
			{
				Pair<Vec3d, Vec3d> vecs = parseVecPair(o.get("pos_size"));
				c.from = vecs.getLeft();
				c.to = c.from.add(vecs.getRight());

			}
			else if (o.has("center_size"))
			{
				Pair<Vec3d, Vec3d> vecs = parseVecPair(o.get("center_size"));
				Vec3d size = vecs.getRight().scale(0.5D);
				c.from = vecs.getLeft().subtract(size);
				c.to = vecs.getLeft().add(size);
			}

			c.faces = SET_ALL_FACES;

			if (o.has("faces"))
			{
				c.faces = parseFaces(JsonUtils.toArray(o.get("faces")));
			}
			else if (o.has("faces_except"))
			{
				c.faces = new HashSet<>(SET_ALL_FACES);
				c.faces.removeAll(parseFaces(JsonUtils.toArray(o.get("faces_except"))));
			}
		}

		@Override
		public void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer layer)
		{
			if (faces.isEmpty() || model.currentLayer != layer)
			{
				return;
			}

			builder.setTintIndex((!skin.color.isEmpty() && model.currentTexture == model.textureMap.get("skin")) ? 0 : -1);

			Vector3f fromv = new Vector3f((float) (model.offset.x + from.x), (float) (model.offset.y + from.y), (float) (model.offset.z + from.z));
			Vector3f tov = new Vector3f((float) (model.offset.x + to.x), (float) (model.offset.y + to.y), (float) (model.offset.z + to.z));

			for (EnumFacing facing : faces)
			{
				Objects.requireNonNull(builder);
				Objects.requireNonNull(model);
				Objects.requireNonNull(model.currentTexture);
				builder.addQuad(fromv, tov, facing, model.currentTexture.get(facing));
			}
		}
	}

	public static class InvertedCube extends Cube
	{
		@Override
		public void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer layer)
		{
			if (faces.isEmpty() || model.currentLayer != layer)
			{
				return;
			}

			builder.setTintIndex((!skin.color.isEmpty() && model.currentTexture == model.textureMap.get("skin")) ? 0 : -1);

			Vector3f fromv = new Vector3f((float) (model.offset.x + from.x), (float) (model.offset.y + from.y), (float) (model.offset.z + from.z));
			Vector3f tov = new Vector3f((float) (model.offset.x + to.x), (float) (model.offset.y + to.y), (float) (model.offset.z + to.z));

			for (EnumFacing facing : faces)
			{
				builder.addQuad(tov, fromv, facing, model.currentTexture.get(facing));
			}
		}
	}

	public final String id;
	public final Map<String, TextureSet> textures;
	public final List<ModelFunction> model;
	public final List<ModelFunction> itemModel;
	public Map<String, SpriteSet> textureMap;
	public SpriteSet currentTexture;
	public Vec3d offset;
	public final float textDistance;
	public final float iconDistance;
	private final String unlocalizedName;
	public BlockRenderLayer currentLayer;
	public Icon icon = Icon.EMPTY;

	public BarrelModel(ResourceLocation _id, JsonObject json)
	{
		id = _id.toString();
		unlocalizedName = _id.getResourceDomain() + ".yabba_model." + _id.getResourcePath();

		if (json.has("textures"))
		{
			textures = new HashMap<>();
			for (Map.Entry<String, JsonElement> entry : json.get("textures").getAsJsonObject().entrySet())
			{
				textures.put(entry.getKey(), TextureSet.of(entry.getValue()));
			}
		}
		else
		{
			textures = Collections.emptyMap();
		}

		model = new ArrayList<>();

		for (JsonElement e : json.get("model").getAsJsonArray())
		{
			if (e.isJsonObject())
			{
				ModelFunction func = parseFunction(e.getAsJsonObject());

				if (func != null)
				{
					model.add(func);
				}
			}
		}

		if (json.has("item_model"))
		{
			itemModel = new ArrayList<>();

			for (JsonElement e : json.get("item_model").getAsJsonArray())
			{
				if (e.isJsonObject())
				{
					ModelFunction func = parseFunction(e.getAsJsonObject());

					if (func != null)
					{
						itemModel.add(func);
					}
				}
			}
		}
		else
		{
			itemModel = model;
		}

		textDistance = (json.has("text_distance") ? json.get("text_distance").getAsFloat() : -0.08F) / 16F;
		iconDistance = (json.has("icon_distance") ? json.get("icon_distance").getAsFloat() : 0.64F) / 16F;
	}

	public int hashCode()
	{
		return id.hashCode();
	}

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (o instanceof BarrelModel)
		{
			return id.equals(((BarrelModel) o).id);
		}

		return false;
	}

	public String toString()
	{
		return I18n.format(unlocalizedName);
	}

	public List<BakedQuad> buildModel(VertexFormat format, ModelRotation rotation, BarrelSkin skin, BlockRenderLayer layer)
	{
		currentTexture = textureMap.get("skin");

		if (currentTexture == null || currentTexture == SpriteSet.EMPTY)
		{
			return Collections.emptyList();
		}

		offset = Vec3d.ZERO;
		currentLayer = skin.layer;
		ModelBuilder builder = new ModelBuilder(format, rotation);

		for (ModelFunction func : model)
		{
			func.apply(builder, this, skin, layer);
		}

		currentTexture = SpriteSet.EMPTY;
		offset = Vec3d.ZERO;
		return builder.getQuads().isEmpty() ? Collections.emptyList() : builder.getQuads();
	}

	public List<BakedQuad> buildItemModel(VertexFormat format, BarrelSkin skin)
	{
		currentTexture = textureMap.get("skin");

		if (currentTexture == null || currentTexture == SpriteSet.EMPTY)
		{
			return Collections.emptyList();
		}

		offset = Vec3d.ZERO;
		currentLayer = skin.layer;
		ModelBuilder builder = new ModelBuilder(format, ModelRotation.X0_Y0);

		for (ModelFunction func : itemModel)
		{
			func.apply(builder, this, skin, BlockRenderLayer.SOLID);
		}

		currentTexture = textureMap.get("skin");
		offset = Vec3d.ZERO;
		currentLayer = skin.layer;

		for (ModelFunction func : itemModel)
		{
			func.apply(builder, this, skin, BlockRenderLayer.CUTOUT);
		}

		currentTexture = textureMap.get("skin");
		offset = Vec3d.ZERO;
		currentLayer = skin.layer;

		for (ModelFunction func : itemModel)
		{
			func.apply(builder, this, skin, BlockRenderLayer.TRANSLUCENT);
		}

		currentTexture = SpriteSet.EMPTY;
		offset = Vec3d.ZERO;
		return builder.getQuads().isEmpty() ? Collections.emptyList() : builder.getQuads();
	}
}