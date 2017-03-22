package com.latmod.yabba.net;

import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrelModifiable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageUpdateBarrelItemCount extends MessageToClient<MessageUpdateBarrelItemCount>
{
    private BlockPos pos;
    private int itemCount;

    public MessageUpdateBarrelItemCount()
    {
    }

    public MessageUpdateBarrelItemCount(BlockPos p, int c)
    {
        pos = p;
        itemCount = c;
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return YabbaNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = LMNetUtils.readPos(buf);
        itemCount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        LMNetUtils.writePos(buf, pos);
        buf.writeInt(itemCount);
    }

    @Override
    public void onMessage(MessageUpdateBarrelItemCount message, EntityPlayer player)
    {
        TileEntity tile = player.world.getTileEntity(message.pos);

        if(tile != null && tile.hasCapability(YabbaCommon.BARREL_CAPABILITY, null))
        {
            IBarrelModifiable barrel = (IBarrelModifiable) tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
            barrel.setItemCount(message.itemCount);
            barrel.clearCachedData();
        }
    }
}