package com.latmod.yabba.client;

import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.google.common.base.Function;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.ITier;
import com.latmod.yabba.util.Tier;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class BarrelModel implements IModel
{
    private static class BarrelForModel implements IBarrel
    {
        private final IBarrelModel model;
        private final IBarrelSkin skin;

        private BarrelForModel(IBarrelModel m, IBarrelSkin s)
        {
            model = m;
            skin = s;
        }

        @Override
        public ITier getTier()
        {
            return Tier.WOOD;
        }

        @Override
        public int getFlags()
        {
            return 0;
        }

        @Override
        public boolean getFlag(int flag)
        {
            return false;
        }

        @Override
        public int getItemCount()
        {
            return 0;
        }

        @Override
        public IBarrelModel getModel()
        {
            return model;
        }

        @Override
        public IBarrelSkin getSkin()
        {
            return skin;
        }

        @Override
        public NBTTagCompound getUpgradeNBT()
        {
            return new NBTTagCompound();
        }

        @Nullable
        @Override
        public NBTTagList getUpgradeNames()
        {
            return null;
        }

        @Override
        public int getFreeSpace()
        {
            return 0;
        }

        @Override
        public int getSlots()
        {
            return 0;
        }

        @Override
        public ItemStack getStackInSlot(int slot)
        {
            return null;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            return null;
        }
    }

    public final IBarrel barrel;
    public Collection<ResourceLocation> textures;

    public BarrelModel(String v)
    {
        Map<String, String> map = LMStringUtils.parse(LMStringUtils.TEMP_MAP, v);
        IBarrelModel model = YabbaRegistry.INSTANCE.getModel(map.get("model"));
        IBarrelSkin skin = YabbaRegistry.INSTANCE.getSkin(map.get("skin"));
        barrel = new BarrelForModel(model, skin);

        Collection<ResourceLocation> tex = skin.getTextures().getTextures();
        tex.addAll(model.getExtraTextures());
        textures = Collections.unmodifiableCollection(new ArrayList<>(tex));
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        return textures;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        List<List<BakedQuad>> quads = new ArrayList<>(ModelRotation.values().length);

        for(ModelRotation rotation : ModelRotation.values())
        {
            quads.add(barrel.getModel().buildModel(barrel, rotation, bakedTextureGetter));
        }

        List<BakedQuad> noStateQuads = barrel.getModel().buildItemModel(barrel, bakedTextureGetter);

        if(noStateQuads == null)
        {
            noStateQuads = quads.get(0);
        }

        return new BarrelVariantBakedModel(bakedTextureGetter.apply(barrel.getSkin().getTextures().getTexture(EnumFacing.NORTH)), quads, noStateQuads);
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }
}