package com.latmod.yabba.net;

import com.latmod.yabba.tile.TileBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageRequestBarrelUpdate implements IMessage, IMessageHandler<MessageRequestBarrelUpdate, IMessage>
{
    private int posX, posY, posZ;

    public MessageRequestBarrelUpdate()
    {
    }

    public MessageRequestBarrelUpdate(TileBarrel tile)
    {
        posX = tile.getPos().getX();
        posY = tile.getPos().getY();
        posZ = tile.getPos().getZ();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        posX = buf.readInt();
        posY = buf.readInt();
        posZ = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(posX);
        buf.writeInt(posY);
        buf.writeInt(posZ);
    }

    @Override
    public IMessage onMessage(MessageRequestBarrelUpdate message, MessageContext ctx)
    {
        TileEntity tile = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));

        if(tile instanceof TileBarrel)
        {
            return new MessageUpdateBarrel((TileBarrel) tile);
        }

        return null;
    }
}