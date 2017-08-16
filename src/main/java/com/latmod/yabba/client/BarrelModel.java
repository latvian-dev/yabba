package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.IconSet;
import com.feed_the_beast.ftbl.lib.client.ModelBuilder;
import com.feed_the_beast.ftbl.lib.client.SpriteSet;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class BarrelModel
{
	public static final HashSet<EnumFacing> SET_ALL_FACES = new HashSet<>(Arrays.asList(EnumFacing.VALUES));

	public static HashSet<EnumFacing> parseFaces(JsonElement json)
	{
		if (!json.isJsonArray())
		{
			JsonArray a = new JsonArray();
			a.add(json);
			return parseFaces(a);
		}

		HashSet<EnumFacing> set = new HashSet<>();

		for (JsonElement e : json.getAsJsonArray())
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

		return null;
	}

	public interface ModelFunction
	{
		void apply(ModelBuilder builder, BarrelModel model);
	}

	public enum SetUvlock implements ModelFunction
	{
		TRUE,
		FALSE;

		@Override
		public void apply(ModelBuilder builder, BarrelModel model)
		{
			builder.setUVLocked(this == TRUE);
		}
	}

	public enum SetShade implements ModelFunction
	{
		TRUE,
		FALSE;

		@Override
		public void apply(ModelBuilder builder, BarrelModel model)
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
		public void apply(ModelBuilder builder, BarrelModel model)
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
		public void apply(ModelBuilder builder, BarrelModel model)
		{
			model.offset = off;
		}
	}

	public static class Cube implements ModelFunction
	{
		public Vec3d from;
		public Vec3d to;
		public HashSet<EnumFacing> faces;

		public static void parseCube(Cube c, JsonObject o)
		{
			if (o.has("size"))
			{
				Vec3d size = parseVec(o.get("size"));

				if (o.has("from"))
				{
					c.from = parseVec(o.get("from"));
					c.to = c.from.add(size);
				}
				else if (o.has("center"))
				{
					size = size.scale(0.5D);
					Vec3d center = parseVec(o.get("center"));
					c.from = center.subtract(size);
					c.to = center.add(size);
				}
			}
			else
			{
				c.from = parseVec(o.get("from"));
				c.to = parseVec(o.get("to"));
			}

			c.faces = SET_ALL_FACES;

			if (o.has("faces"))
			{
				c.faces = parseFaces(o.get("faces"));
			}
			else if (o.has("faces_except"))
			{
				c.faces = new HashSet<>(SET_ALL_FACES);
				c.faces.removeAll(parseFaces(o.get("faces_except")));
			}
		}

		@Override
		public void apply(ModelBuilder builder, BarrelModel model)
		{
			float x0 = (float) (model.offset.x + from.x);
			float y0 = (float) (model.offset.y + from.y);
			float z0 = (float) (model.offset.z + from.z);
			float x1 = (float) (model.offset.x + to.x);
			float y1 = (float) (model.offset.y + to.y);
			float z1 = (float) (model.offset.z + to.z);
			builder.addCube(x0, y0, z0, x1, y1, z1, model.currentTexture);
		}
	}

	public static class InvertedCube extends Cube
	{
		@Override
		public void apply(ModelBuilder builder, BarrelModel model)
		{
			float x0 = (float) (model.offset.x + from.x);
			float y0 = (float) (model.offset.y + from.y);
			float z0 = (float) (model.offset.z + from.z);
			float x1 = (float) (model.offset.x + to.x);
			float y1 = (float) (model.offset.y + to.y);
			float z1 = (float) (model.offset.z + to.z);
			builder.addInvertedCube(x0, y0, z0, x1, y1, z1, model.currentTexture);
		}
	}

	public final ResourceLocation id;
	public final Map<String, IconSet> textures;
	public final List<ModelFunction> model;
	public final List<ModelFunction> itemModel;
	public Map<String, SpriteSet> textureMap;
	public SpriteSet currentTexture;
	public Vec3d offset;
	public final float textDistance;
	public final float iconDistance;
	private final String unlocalizedName;

	public BarrelModel(ResourceLocation _id, JsonObject json)
	{
		id = _id;
		unlocalizedName = id.getResourceDomain() + ".yabba_model." + id.getResourcePath();

		if (json.has("textures"))
		{
			textures = new HashMap<>();
			for (Map.Entry<String, JsonElement> entry : json.get("textures").getAsJsonObject().entrySet())
			{
				textures.put(entry.getKey(), IconSet.of(entry.getValue()));
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
			itemModel = Collections.emptyList();
		}

		textDistance = json.has("text_distance") ? json.get("text_distance").getAsFloat() : -0.005F;
		iconDistance = json.has("icon_distance") ? json.get("icon_distance").getAsFloat() : 0.04F;
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
		return StringUtils.translate(unlocalizedName);
	}

	public List<BakedQuad> buildModel(VertexFormat format, ModelRotation rotation)
	{
		currentTexture = textureMap.get("skin");
		offset = Vec3d.ZERO;
		ModelBuilder builder = new ModelBuilder(format, rotation);

		for (ModelFunction func : model)
		{
			func.apply(builder, this);
		}

		currentTexture = null;
		offset = Vec3d.ZERO;
		return builder.getQuads();
	}

	public List<BakedQuad> buildItemModel(VertexFormat format)
	{
		if (!itemModel.isEmpty())
		{
			currentTexture = null;
			offset = Vec3d.ZERO;
			ModelBuilder builder = new ModelBuilder(format, ModelRotation.X0_Y0);

			for (ModelFunction func : itemModel)
			{
				func.apply(builder, this);
			}

			currentTexture = null;
			offset = Vec3d.ZERO;
			return builder.getQuads();
		}

		return Collections.emptyList();
	}
}