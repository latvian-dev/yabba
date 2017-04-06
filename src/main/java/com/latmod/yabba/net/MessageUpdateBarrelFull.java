package com.latmod.yabba.net;

import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.block.BlockBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public class MessageUpdateBarrelFull extends MessageToClient<MessageUpdateBarrelFull>
{
    private String model, skin, tier;
    private BlockPos pos;
    private int itemCount, flags;
    private ItemStack storedItem;
    private NBTTagCompound upgrades;

    public MessageUpdateBarrelFull()
    {
    }

    public MessageUpdateBarrelFull(BlockPos p, IBarrel barrel)
    {
        pos = p;
        storedItem = barrel.getStackInSlot(0);
        itemCount = barrel.getItemCount();
        flags = barrel.getFlags();
        upgrades = barrel.getUpgradeNBT();
        model = barrel.getModel().getName();
        skin = barrel.getSkin().getName();
        tier = barrel.getTier().getName();
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
        storedItem = ByteBufUtils.readItemStack(buf);
        itemCount = buf.readInt();
        flags = buf.readInt();
        upgrades = ByteBufUtils.readTag(buf);
        model = ByteBufUtils.readUTF8String(buf);
        skin = ByteBufUtils.readUTF8String(buf);
        tier = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        LMNetUtils.writePos(buf, pos);
        ByteBufUtils.writeItemStack(buf, storedItem);
        buf.writeInt(itemCount);
        buf.writeInt(flags);
        ByteBufUtils.writeTag(buf, upgrades);
        ByteBufUtils.writeUTF8String(buf, model);
        ByteBufUtils.writeUTF8String(buf, skin);
        ByteBufUtils.writeUTF8String(buf, tier);
    }

    @Override
    public void onMessage(MessageUpdateBarrelFull message, EntityPlayer player)
    {
        TileEntity tile = player.world.getTileEntity(message.pos);

        if(tile != null && tile.hasCapability(YabbaCommon.BARREL_CAPABILITY, null))
        {
            IBarrelModifiable barrel = (IBarrelModifiable) tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null);
            IBarrelModel model = YabbaRegistry.INSTANCE.getModel(message.model);
            IBarrelSkin skin = YabbaRegistry.INSTANCE.getSkin(message.skin);
            boolean updateVariant = !barrel.getModel().equals(model) || !barrel.getSkin().equals(skin);

            barrel.setFlags(message.flags);
            barrel.setStackInSlot(0, message.storedItem);
            barrel.setItemCount(message.itemCount);
            barrel.setUpgradeNBT(message.upgrades);
            barrel.setModel(model);
            barrel.setSkin(skin);
            barrel.setTier(YabbaRegistry.INSTANCE.getTier(message.tier));
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
    }
}