package com.latmod.yabba.net;

import com.latmod.yabba.block.EnumTier;
import com.latmod.yabba.tile.TileBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageUpdateBarrel implements IMessage, IMessageHandler<MessageUpdateBarrel, IMessage>
{
    private int posX, posY, posZ, itemCount;
    private byte tier;
    private ItemStack storedItem;

    public MessageUpdateBarrel()
    {
    }

    public MessageUpdateBarrel(TileBarrel tile)
    {
        posX = tile.getPos().getX();
        posY = tile.getPos().getY();
        posZ = tile.getPos().getZ();
        tier = tile.barrel.getTier().getTierID();
        storedItem = tile.barrel.getStackInSlot(0);
        itemCount = tile.barrel.getItemCount();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        posX = buf.readInt();
        posY = buf.readInt();
        posZ = buf.readInt();
        tier = buf.readByte();
        storedItem = ByteBufUtils.readItemStack(buf);
        itemCount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(posX);
        buf.writeInt(posY);
        buf.writeInt(posZ);
        buf.writeByte(tier);
        ByteBufUtils.writeItemStack(buf, storedItem);
        buf.writeInt(itemCount);
    }

    @Override
    public IMessage onMessage(MessageUpdateBarrel message, MessageContext ctx)
    {
        TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));

        if(tile instanceof TileBarrel)
        {
            TileBarrel barrel = (TileBarrel) tile;
            barrel.barrel.setTier(EnumTier.VALUES[message.tier]);
            barrel.barrel.setStackInSlot(0, message.storedItem);
            barrel.barrel.setItemCount(message.itemCount);
            barrel.clearCachedData();
        }

        return null;
    }
}