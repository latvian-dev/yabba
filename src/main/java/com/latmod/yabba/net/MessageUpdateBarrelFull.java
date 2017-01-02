package com.latmod.yabba.net;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.block.BlockBarrel;
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
    private byte model;
    private int posX, posY, posZ, itemCount, flags, skin;
    private ItemStack storedItem;
    private NBTTagCompound upgrades;

    public MessageUpdateBarrelFull()
    {
    }

    public MessageUpdateBarrelFull(TileEntity tile)
    {
        posX = tile.getPos().getX();
        posY = tile.getPos().getY();
        posZ = tile.getPos().getZ();

        IBarrel barrel = tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
        storedItem = barrel.getStackInSlot(0);
        itemCount = barrel.getItemCount();
        flags = barrel.getFlags();
        upgrades = barrel.getUpgradeNBT();
        model = YabbaRegistry.INSTANCE.getModelId(barrel.getModel().getName());
        skin = YabbaRegistry.INSTANCE.getSkinId(barrel.getSkin().getName());
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        posX = buf.readInt();
        posY = buf.readInt();
        posZ = buf.readInt();
        storedItem = ByteBufUtils.readItemStack(buf);
        itemCount = buf.readInt();
        flags = buf.readInt();
        upgrades = ByteBufUtils.readTag(buf);
        model = buf.readByte();
        skin = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(posX);
        buf.writeInt(posY);
        buf.writeInt(posZ);
        ByteBufUtils.writeItemStack(buf, storedItem);
        buf.writeInt(itemCount);
        buf.writeInt(flags);
        ByteBufUtils.writeTag(buf, upgrades);
        buf.writeByte(model);
        buf.writeInt(skin);
    }

    @Override
    public IMessage onMessage(MessageUpdateBarrelFull message, MessageContext ctx)
    {
        TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));

        if(tile != null && tile.hasCapability(YabbaCommon.BARREL_CAPABILITY, null))
        {
            IBarrelModifiable barrel = (IBarrelModifiable) tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
            IBarrelModel model = YabbaRegistry.INSTANCE.getModel(message.model, true);
            IBarrelSkin skin = YabbaRegistry.INSTANCE.getSkin(message.skin, true);
            boolean updateVariant = !barrel.getModel().equals(model) || !barrel.getSkin().equals(skin);

            barrel.setFlags(message.flags);
            barrel.setStackInSlot(0, message.storedItem);
            barrel.setItemCount(message.itemCount);
            barrel.setUpgradeNBT(message.upgrades);
            barrel.setModel(model);
            barrel.setSkin(skin);
            barrel.clearCachedData();

            if(updateVariant)
            {
                IBlockState state = tile.getWorld().getBlockState(tile.getPos());
                state = state.withProperty(BlockBarrel.MODEL, model);
                state = state.withProperty(BlockBarrel.SKIN, skin);
                tile.getWorld().setBlockState(tile.getPos(), state);
                tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 8);
            }
        }

        return null;
    }
}