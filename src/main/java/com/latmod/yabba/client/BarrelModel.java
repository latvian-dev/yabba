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
import com.latmod.yabba.util.EnumBarrelModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
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
import java.util.Set;

/**
 * @author LatvianModder
 */
public class BarrelModel
{
	public static final Set<EnumFacing> SET_ALL_FACES = new HashSet<>(Arrays.asList(EnumFacing.VALUES));

	public static Set<EnumFacing> parseFaces(JsonArray json)
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

		return set.isEmpty() ? Collections.emptySet() : set;
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
		default void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer layer, @Nullable EnumFacing side)
		{
			applyFunction(builder, model);
		}

		default void applyFunction(ModelBuilder builder, BarrelModel model)
		{
		}
	}

	public enum SetUvlock implements ModelFunction
	{
		TRUE,
		FALSE;

		@Override
		public void applyFunction(ModelBuilder builder, BarrelModel model)
		{
			builder.setUVLocked(this == TRUE);
		}
	}

	public enum SetShade implements ModelFunction
	{
		TRUE,
		FALSE;

		@Override
		public void applyFunction(ModelBuilder builder, BarrelModel model)
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
		public void applyFunction(ModelBuilder builder, BarrelModel model)
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
		public void applyFunction(ModelBuilder builder, BarrelModel model)
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
		public void applyFunction(ModelBuilder builder, BarrelModel model)
		{
			model.currentLayer = layer;
		}
	}

	public static class Cube implements ModelFunction
	{
		public Vec3d from;
		public Vec3d to;
		public Set<EnumFacing> faces;
		public Set<EnumFacing> cull;

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

			c.cull = Collections.emptySet();

			if (o.has("cull"))
			{
				c.cull = parseFaces(JsonUtils.toArray(o.get("cull")));
			}
			else if (o.has("cull_except"))
			{
				c.cull = new HashSet<>(SET_ALL_FACES);
				c.cull.removeAll(parseFaces(JsonUtils.toArray(o.get("cull_except"))));
			}
		}

		@Override
		public void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer layer, @Nullable EnumFacing side)
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
				if (drawFace(side, facing))
				{
					builder.addQuad(fromv, tov, facing, model.currentTexture.get(facing));
				}
			}
		}

		protected boolean drawFace(@Nullable EnumFacing side, EnumFacing facing)
		{
			return side == null;
		}
	}

	public static class InvertedCube extends Cube
	{
		@Override
		public void apply(ModelBuilder builder, BarrelModel model, BarrelSkin skin, BlockRenderLayer layer, @Nullable EnumFacing side)
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
				if (!drawFace(side, facing))
				{
					builder.addQuad(tov, fromv, facing, model.currentTexture.get(facing));
				}
			}
		}
	}

	public final EnumBarrelModel id;
	public final Map<String, TextureSet> textures;
	public final List<ModelFunction> model;
	public final List<ModelFunction> itemModel;
	public Map<String, SpriteSet> textureMap;
	public SpriteSet currentTexture;
	public Vec3d offset;
	public final float textDistance;
	public final float iconDistance;
	public BlockRenderLayer currentLayer;
	public Icon icon = Icon.EMPTY;

	public BarrelModel(EnumBarrelModel _id, JsonObject json)
	{
		id = _id;

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
		return I18n.format(id.getTranslationKey());
	}

	public List<BakedQuad> buildModel(VertexFormat format, ModelRotation rotation, BarrelSkin skin, BlockRenderLayer layer, @Nullable EnumFacing side)
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
			func.apply(builder, this, skin, layer, side);
		}

		currentTexture = SpriteSet.EMPTY;
		offset = Vec3d.ZERO;
		return builder.getQuads().isEmpty() ? Collections.emptyList() : Arrays.asList(builder.getQuads().toArray(new BakedQuad[0]));
	}

	public List<BakedQuad> buildItemModel(VertexFormat format, BarrelSkin skin, @Nullable EnumFacing side)
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
			func.apply(builder, this, skin, BlockRenderLayer.SOLID, side);
		}

		currentTexture = textureMap.get("skin");
		offset = Vec3d.ZERO;
		currentLayer = skin.layer;

		for (ModelFunction func : itemModel)
		{
			func.apply(builder, this, skin, BlockRenderLayer.CUTOUT, side);
		}

		currentTexture = textureMap.get("skin");
		offset = Vec3d.ZERO;
		currentLayer = skin.layer;

		for (ModelFunction func : itemModel)
		{
			func.apply(builder, this, skin, BlockRenderLayer.TRANSLUCENT, side);
		}

		currentTexture = SpriteSet.EMPTY;
		offset = Vec3d.ZERO;
		return builder.getQuads().isEmpty() ? Collections.emptyList() : Arrays.asList(builder.getQuads().toArray(new BakedQuad[0]));
	}
}