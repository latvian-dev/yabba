package com.latmod.yabba.net;

import com.latmod.yabba.tile.TileBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageUpdateBarrelItemCount implements IMessage, IMessageHandler<MessageUpdateBarrelItemCount, IMessage>
{
    private int posX, posY, posZ, itemCount;

    public MessageUpdateBarrelItemCount()
    {
    }

    public MessageUpdateBarrelItemCount(TileBarrel tile)
    {
        posX = tile.getPos().getX();
        posY = tile.getPos().getY();
        posZ = tile.getPos().getZ();
        itemCount = tile.barrel.getItemCount();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        posX = buf.readInt();
        posY = buf.readInt();
        posZ = buf.readInt();
        itemCount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(posX);
        buf.writeInt(posY);
        buf.writeInt(posZ);
        buf.writeInt(itemCount);
    }

    @Override
    public IMessage onMessage(MessageUpdateBarrelItemCount message, MessageContext ctx)
    {
        TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));

        if(tile instanceof TileBarrel)
        {
            TileBarrel barrel = (TileBarrel) tile;
            barrel.barrel.setItemCount(message.itemCount);
            barrel.clearCachedData();
        }

        return null;
    }
}