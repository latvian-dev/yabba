package com.latmod.yabba.net;

import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.latmod.yabba.YabbaRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageSyncData extends MessageToClient<MessageSyncData>
{
    private NBTTagCompound models = YabbaRegistry.MODEL_NAME_ID_MAP;
    private NBTTagCompound skins = YabbaRegistry.SKIN_NAME_ID_MAP;

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return YabbaNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        models = ByteBufUtils.readTag(buf);
        skins = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, models);
        ByteBufUtils.writeTag(buf, skins);
    }

    @Override
    public void onMessage(MessageSyncData m, EntityPlayer player)
    {
        YabbaRegistry.MODEL_ID_MAP_C.clear();

        for(String key : m.models.getKeySet())
        {
            YabbaRegistry.MODEL_ID_MAP_C.put(m.models.getByte(key), YabbaRegistry.INSTANCE.getModel(key));
        }

        YabbaRegistry.SKIN_ID_MAP_C.clear();

        for(String key : m.skins.getKeySet())
        {
            YabbaRegistry.SKIN_ID_MAP_C.put(m.skins.getInteger(key), YabbaRegistry.INSTANCE.getSkin(key));
        }
    }
}