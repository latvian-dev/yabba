package com.latmod.yabba.net;

import com.latmod.yabba.YabbaRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageSyncData implements IMessage, IMessageHandler<MessageSyncData, IMessage>
{
    private NBTTagCompound models = YabbaRegistry.MODEL_NAME_ID_MAP;
    private NBTTagCompound skins = YabbaRegistry.SKIN_NAME_ID_MAP;

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
    public IMessage onMessage(MessageSyncData message, MessageContext ctx)
    {
        YabbaRegistry.MODEL_ID_MAP_C.clear();

        for(String key : message.models.getKeySet())
        {
            YabbaRegistry.MODEL_ID_MAP_C.put(message.models.getByte(key), YabbaRegistry.INSTANCE.getModel(key));
        }

        YabbaRegistry.SKIN_ID_MAP_C.clear();

        for(String key : message.skins.getKeySet())
        {
            YabbaRegistry.SKIN_ID_MAP_C.put(message.skins.getInteger(key), YabbaRegistry.INSTANCE.getSkin(key));
        }

        return null;
    }
}