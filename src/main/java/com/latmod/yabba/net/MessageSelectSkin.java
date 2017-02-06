package com.latmod.yabba.net;

import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.latmod.yabba.item.ItemPainter;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageSelectSkin extends MessageToServer<MessageSelectSkin>
{
    private String skinId;

    public MessageSelectSkin()
    {
    }

    public MessageSelectSkin(String id)
    {
        skinId = id;
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return YabbaNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        skinId = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, skinId);
    }

    @Override
    public void onMessage(MessageSelectSkin message, EntityPlayer player)
    {
        ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

        if(stack != null && stack.getItem() instanceof ItemPainter)
        {
            ItemPainter.setSkin(stack, message.skinId);
        }
    }
}