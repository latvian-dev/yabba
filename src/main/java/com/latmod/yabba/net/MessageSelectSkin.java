package com.latmod.yabba.net;

import com.latmod.yabba.item.ItemPainter;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageSelectSkin implements IMessage, IMessageHandler<MessageSelectSkin, IMessage>
{
    private int skinId;

    public MessageSelectSkin()
    {
    }

    public MessageSelectSkin(int id)
    {
        skinId = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        skinId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(skinId);
    }

    @Override
    public IMessage onMessage(MessageSelectSkin message, MessageContext ctx)
    {
        ItemStack stack = ctx.getServerHandler().playerEntity.getHeldItem(EnumHand.MAIN_HAND);

        if(stack != null && stack.getItem() instanceof ItemPainter)
        {
            ItemPainter.setSkin(stack, message.skinId);
        }

        return null;
    }
}