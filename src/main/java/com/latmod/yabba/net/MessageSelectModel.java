package com.latmod.yabba.net;

import com.latmod.yabba.item.ItemHammer;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageSelectModel implements IMessage, IMessageHandler<MessageSelectModel, IMessage>
{
    private byte modelId;

    public MessageSelectModel()
    {
    }

    public MessageSelectModel(byte id)
    {
        modelId = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        modelId = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(modelId);
    }

    @Override
    public IMessage onMessage(MessageSelectModel message, MessageContext ctx)
    {
        ItemStack stack = ctx.getServerHandler().playerEntity.getHeldItem(EnumHand.MAIN_HAND);

        if(stack != null && stack.getItem() instanceof ItemHammer)
        {
            ItemHammer.setModel(stack, message.modelId);
        }

        return null;
    }
}