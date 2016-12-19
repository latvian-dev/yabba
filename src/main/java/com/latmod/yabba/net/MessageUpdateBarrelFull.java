package com.latmod.yabba.net;

import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.tile.TileBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageUpdateBarrelFull implements IMessage, IMessageHandler<MessageUpdateBarrelFull, IMessage>
{
    private int posX, posY, posZ, itemCount;
    private ItemStack storedItem;
    private NBTTagCompound upgrades;
    private String variant;

    public MessageUpdateBarrelFull()
    {
    }

    public MessageUpdateBarrelFull(TileBarrel tile)
    {
        posX = tile.getPos().getX();
        posY = tile.getPos().getY();
        posZ = tile.getPos().getZ();
        storedItem = tile.barrel.getStackInSlot(0);
        itemCount = tile.barrel.getItemCount();
        upgrades = tile.barrel.getUpgradeNBT();
        variant = tile.barrel.getVariant().getName();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        posX = buf.readInt();
        posY = buf.readInt();
        posZ = buf.readInt();
        storedItem = ByteBufUtils.readItemStack(buf);
        itemCount = buf.readInt();
        upgrades = ByteBufUtils.readTag(buf);
        variant = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(posX);
        buf.writeInt(posY);
        buf.writeInt(posZ);
        ByteBufUtils.writeItemStack(buf, storedItem);
        buf.writeInt(itemCount);
        ByteBufUtils.writeTag(buf, upgrades);
        ByteBufUtils.writeUTF8String(buf, variant);
    }

    @Override
    public IMessage onMessage(MessageUpdateBarrelFull message, MessageContext ctx)
    {
        TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));

        if(tile instanceof TileBarrel)
        {
            TileBarrel barrel = (TileBarrel) tile;
            boolean updateVariant = !barrel.barrel.getVariant().getName().equals(message.variant);
            barrel.barrel.setStackInSlot(0, message.storedItem);
            barrel.barrel.setItemCount(message.itemCount);
            barrel.barrel.setUpgradeNBT(message.upgrades);
            barrel.barrel.setVariant(YabbaRegistry.INSTANCE.getVariant(message.variant));
            barrel.clearCachedData();

            if(updateVariant)
            {
                IBlockState state = tile.getWorld().getBlockState(tile.getPos());
                tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 8);
            }
        }

        return null;
    }
}