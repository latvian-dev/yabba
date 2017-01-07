package com.latmod.yabba;

import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.ITier;
import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.events.YabbaRegistryEvent;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.util.BarrelSkin;
import com.latmod.yabba.util.IconSet;
import com.latmod.yabba.util.Tier;
import gnu.trove.map.hash.TByteObjectHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public enum YabbaRegistry implements IYabbaRegistry
{
    INSTANCE;

    private static final Map<String, IBarrelSkin> SKINS = new HashMap<>();
    private static final Map<IBlockState, IBarrelSkin> SKINS_STATE_MAP = new HashMap<>();
    private static final Map<String, ITier> TIERS = new HashMap<>();
    private static final Map<String, IBarrelModel> MODELS = new HashMap<>();
    public static final List<IBarrelModel> ALL_MODELS = new ArrayList<>();
    public static final List<IBarrelSkin> ALL_SKINS = new ArrayList<>();

    public static NBTTagCompound MODEL_NAME_ID_MAP;
    private static final TByteObjectHashMap<IBarrelModel> MODEL_ID_MAP_S = new TByteObjectHashMap<>();
    public static TByteObjectHashMap<IBarrelModel> MODEL_ID_MAP_C = new TByteObjectHashMap<>();
    private static int lastModelID = 0;

    public static NBTTagCompound SKIN_NAME_ID_MAP;
    private static final TIntObjectHashMap<IBarrelSkin> SKIN_ID_MAP_S = new TIntObjectHashMap<>();
    public static TIntObjectHashMap<IBarrelSkin> SKIN_ID_MAP_C = new TIntObjectHashMap<>();
    private static int lastSkinID = 0;

    public static final IBarrelSkin DEFAULT_SKIN = INSTANCE.addSkin(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), "all=blocks/planks_oak");

    public void sendEvent()
    {
        addTier(Tier.NONE);
        addModel(ModelBarrel.INSTANCE);

        MinecraftForge.EVENT_BUS.post(new YabbaRegistryEvent(this));
        ALL_MODELS.addAll(MODELS.values());
        ALL_SKINS.addAll(SKINS.values());

        Collections.sort(ALL_MODELS);
        Collections.sort(ALL_SKINS);

        System.out.println("YABBA Models: " + ALL_MODELS);
        System.out.println("YABBA Skins: " + ALL_SKINS);
        System.out.println("YABBA Tiers: " + TIERS.keySet());
    }

    public void loadData(File file)
    {
        MODEL_NAME_ID_MAP = new NBTTagCompound();
        SKIN_NAME_ID_MAP = new NBTTagCompound();
        lastModelID = 0;
        lastSkinID = 0;
        MODEL_ID_MAP_S.clear();
        SKIN_ID_MAP_S.clear();

        if(file.exists())
        {
            try
            {
                NBTTagCompound nbt = CompressedStreamTools.read(file);

                lastModelID = nbt.getInteger("LastModelID");
                lastSkinID = nbt.getInteger("LastSkinID");

                NBTTagCompound nbt1 = nbt.getCompoundTag("Models");

                for(String s : nbt1.getKeySet())
                {
                    MODEL_NAME_ID_MAP.setByte(getModel(s).getName(), nbt1.getByte(s));
                }

                nbt1 = nbt.getCompoundTag("Skins");

                for(String s : nbt1.getKeySet())
                {
                    SKIN_NAME_ID_MAP.setInteger(getSkin(s).getName(), nbt1.getInteger(s));
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        for(IBarrelModel model : ALL_MODELS)
        {
            MODEL_ID_MAP_S.put(getModelId(model.getName()), model);
        }

        for(IBarrelSkin skin : ALL_SKINS)
        {
            SKIN_ID_MAP_S.put(getSkinId(skin.getName()), skin);
        }
    }

    public void saveData(File file)
    {
        try
        {
            if(!file.exists())
            {
                file.createNewFile();
            }

            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("LastModelID", lastModelID);
            nbt.setInteger("LastSkinID", lastSkinID);
            nbt.setTag("Models", MODEL_NAME_ID_MAP);
            nbt.setTag("Skins", SKIN_NAME_ID_MAP);
            CompressedStreamTools.write(nbt, file);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void addSkin(IBarrelSkin skin)
    {
        SKINS.put(skin.getName(), skin);
        SKINS_STATE_MAP.put(skin.getState(), skin);
    }

    @Override
    public IBarrelSkin addSkin(IBlockState parentState, String icons)
    {
        IBarrelSkin skin = new BarrelSkin(parentState, new IconSet(icons));
        addSkin(skin);
        return skin;
    }

    @Override
    public void addTier(ITier tier)
    {
        TIERS.put(tier.getName(), tier);
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

    public IBarrelSkin getSkin(IBlockState id)
    {
        IBarrelSkin skin = SKINS_STATE_MAP.get(id);
        return skin == null ? DEFAULT_SKIN : skin;
    }

    public IBarrelSkin getSkin(int id, boolean client)
    {
        IBarrelSkin skin = (client ? SKIN_ID_MAP_C : SKIN_ID_MAP_S).get(id);
        return skin == null ? DEFAULT_SKIN : skin;
    }

    public int getSkinId(String skinId)
    {
        int id = SKIN_NAME_ID_MAP.getInteger(skinId);

        if(id == 0)
        {
            lastSkinID++;
            id = lastSkinID;
            SKIN_NAME_ID_MAP.setInteger(skinId, id);
        }

        return id;
    }

    public ITier getTier(String id)
    {
        ITier tier = TIERS.get(id);
        return tier == null ? Tier.NONE : tier;
    }

    public boolean hasSkin(String id)
    {
        return SKINS.containsKey(id);
    }

    public IBarrelModel getModel(String id)
    {
        IBarrelModel model = MODELS.get(id);
        return model == null ? ModelBarrel.INSTANCE : model;
    }

    public IBarrelModel getModel(byte id, boolean client)
    {
        IBarrelModel model = (client ? MODEL_ID_MAP_C : MODEL_ID_MAP_S).get(id);
        return model == null ? ModelBarrel.INSTANCE : model;
    }

    public byte getModelId(String modelId)
    {
        byte id = MODEL_NAME_ID_MAP.getByte(modelId);

        if(id == 0)
        {
            lastModelID++;
            id = (byte) lastModelID;
            MODEL_NAME_ID_MAP.setByte(modelId, id);
        }

        return id;
    }
}