package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.IconSet;
import com.feed_the_beast.ftbl.lib.util.BlockPropertyString;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.common.base.Optional;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.events.YabbaModelsEvent;
import com.latmod.yabba.api.events.YabbaSkinsEvent;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.util.BarrelSkin;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public enum YabbaRegistry implements YabbaModelsEvent.YabbaModelRegistry, YabbaSkinsEvent.YabbaSkinRegistry
{
    INSTANCE;

    private static final Map<String, IBarrelModel> MODELS = new HashMap<>();
    private static final Map<String, IBarrelSkin> SKINS = new HashMap<>();
    public static final List<IBarrelModel> ALL_MODELS = new ArrayList<>();
    public static final List<IBarrelSkin> ALL_SKINS = new ArrayList<>();

    public static final IBarrelSkin DEFAULT_SKIN = INSTANCE.addSkin(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), "all=blocks/planks_oak");

    public void sendEvent()
    {
        addModel(ModelBarrel.INSTANCE);

        MinecraftForge.EVENT_BUS.post(new YabbaModelsEvent(this));
        ALL_MODELS.addAll(MODELS.values());
        ALL_MODELS.sort(StringUtils.ID_COMPARATOR);
        BlockBarrel.MODEL = new BlockPropertyString("model", MODELS.keySet())
        {
            @Override
            public Optional<String> parseValue(String value)
            {
                return Optional.of(getModel(value).getName());
            }
        };
        Yabba.LOGGER.info("Models: " + ALL_MODELS.size());

        MinecraftForge.EVENT_BUS.post(new YabbaSkinsEvent(this));
        ALL_SKINS.addAll(SKINS.values());
        ALL_SKINS.sort(StringUtils.ID_COMPARATOR);
        BlockBarrel.SKIN = new BlockPropertyString("skin", SKINS.keySet())
        {
            @Override
            public Optional<String> parseValue(String value)
            {
                return Optional.of(getSkin(value).getName());
            }
        };
        Yabba.LOGGER.info("Skins: " + ALL_SKINS.size());
    }

    @Override
    public void addSkin(IBarrelSkin skin)
    {
        SKINS.put(skin.getName(), skin);
    }

    @Override
    public IBarrelSkin addSkin(IBlockState parentState, String icons, String uname)
    {
        IBarrelSkin skin = new BarrelSkin(parentState, new IconSet(icons), uname);
        addSkin(skin);
        return skin;
    }

    @Override
    public void addModel(IBarrelModel model)
    {
        MODELS.put(model.getName(), model);
    }

    public IBarrelSkin getSkin(String id)
    {
        IBarrelSkin skin = SKINS.get(id);
        return skin == null ? DEFAULT_SKIN : skin;
    }

    public IBarrelModel getModel(String id)
    {
        IBarrelModel model = MODELS.get(id);
        return model == null ? ModelBarrel.INSTANCE : model;
    }
}