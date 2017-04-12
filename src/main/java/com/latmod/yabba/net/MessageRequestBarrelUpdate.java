package com.latmod.yabba.net;

import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.tile.TileBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageRequestBarrelUpdate extends MessageToServer<MessageRequestBarrelUpdate>
{
    private BlockPos pos;

    public MessageRequestBarrelUpdate()
    {
    }

    public MessageRequestBarrelUpdate(BlockPos p)
    {
        pos = p;
    }

    @Override
    public NetworkWrapper getWrapper()
    {
        return YabbaNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = NetUtils.readPos(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        NetUtils.writePos(buf, pos);
    }

    @Override
    public void onMessage(MessageRequestBarrelUpdate message, EntityPlayer player)
    {
        TileEntity tile = player.world.getTileEntity(message.pos);

        if(tile instanceof TileBarrel)
        {
            new MessageUpdateBarrelFull(tile.getPos(), tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null)).sendTo(player);
        }
    }
}