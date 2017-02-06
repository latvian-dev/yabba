package com.latmod.yabba.tile;

import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.lib.config.BasicConfigContainer;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.latmod.yabba.FTBLibIntegration;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.events.YabbaCreateConfigEvent;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.net.MessageUpdateBarrelFull;
import com.latmod.yabba.net.MessageUpdateBarrelItemCount;
import com.latmod.yabba.util.EnumRedstoneCompMode;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 13.12.2016.
 */
public class TileBarrel extends TileEntity implements ITickable, IDeepStorageUnit
{
    public static final double BUTTON_SIZE = 0.24D;

    private BarrelTileContainer barrel;
    private String cachedItemName, cachedItemCount;
    private float cachedRotation;
    private int sendUpdate = 2;
    public boolean requestClientUpdate = true;

    public TileBarrel()
    {
        barrel = new BarrelTileContainer()
        {
            @Override
            public void markBarrelDirty(boolean full)
            {
                if(full)
                {
                    sendUpdate = 2;
                }
                else if(sendUpdate == 0)
                {
                    sendUpdate = 1;
                }
            }

            @Override
            public void clearCachedData()
            {
                cachedItemName = null;
                cachedItemCount = null;
                cachedRotation = -1F;
            }
        };

        barrel.clearCachedData();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        barrel.deserializeNBT(nbt.getCompoundTag("Barrel"));
        barrel.clearCachedData();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setTag("Barrel", barrel.serializeNBT());
        return nbt;
    }

    @Override
    public void update()
    {
        if(sendUpdate > 0)
        {
            if(sendUpdate > 1)
            {
                worldObj.markChunkDirty(pos, this);
                
                /*
                if(getBlockType() != Blocks.AIR)
                {
                    worldObj.updateComparatorOutputLevel(pos, getBlockType());
                }
                */
            }

            if(!worldObj.isRemote)
            {
                (sendUpdate > 1 ? new MessageUpdateBarrelFull(pos, barrel) : new MessageUpdateBarrelItemCount(pos, barrel.getItemCount())).sendToAllAround(worldObj.provider.getDimension(), pos, 300D);
            }

            if(barrel.getFlag(IBarrel.FLAG_REDSTONE_OUT))
            {
                worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
            }

            sendUpdate = 0;
        }

        if(!worldObj.isRemote && barrel.getFlag(IBarrel.FLAG_HOPPER) && (worldObj.getTotalWorldTime() % 8L) == (pos.hashCode() & 7))
        {
            boolean ender = barrel.getFlag(IBarrel.FLAG_HOPPER_ENDER);
            int maxItems = ender ? 64 : 8;

            if(barrel.getItemCount() > 0 && barrel.getUpgradeNBT().getBoolean("HopperDown"))
            {
                ItemStack extracted = barrel.extractItem(0, maxItems, true);

                if(extracted != null && extracted.stackSize > 0)
                {
                    TileEntity tileDown = worldObj.getTileEntity(pos.offset(EnumFacing.DOWN));

                    if(tileDown != null && tileDown.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
                    {
                        ItemStack inserted = ItemHandlerHelper.insertItem(tileDown.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP), extracted, false);
                        barrel.extractItem(0, inserted == null ? maxItems : inserted.stackSize, false);
                    }
                }
            }

            if(barrel.getUpgradeNBT().getBoolean("HopperUp"))
            {
                //FIXME
            }

            if(barrel.getUpgradeNBT().getBoolean("HopperCollect"))
            {
                AxisAlignedBB aabb = new AxisAlignedBB(pos.add(0, 1, 0), pos.add(1, 2, 1));

                if(ender)
                {
                    aabb = aabb.expand(5D, 5D, 5D);
                }

                for(EntityItem item : worldObj.getEntitiesWithinAABB(EntityItem.class, aabb, null))
                {
                    ItemStack stack = barrel.insertItem(0, item.getEntityItem().copy(), false);
                    item.setEntityItemStack(stack);

                    if(stack == null || stack.stackSize == 0)
                    {
                        item.setDead();
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        barrel.clearCachedData();
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void markDirty()
    {
        barrel.markBarrelDirty(true);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == YabbaCommon.BARREL_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if(capability == YabbaCommon.BARREL_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return (T) barrel;
        }

        return super.getCapability(capability, facing);
    }

    public String getItemDisplayName()
    {
        if(cachedItemName == null)
        {
            cachedItemName = barrel.storedItem == null ? "" : barrel.storedItem.getDisplayName();
        }

        return cachedItemName;
    }

    public String getItemDisplayCount()
    {
        if(cachedItemCount == null)
        {
            if(barrel.getFlag(IBarrel.FLAG_IS_CREATIVE))
            {
                cachedItemCount = "INF";
                return cachedItemCount;
            }

            int max = barrel.storedItem == null ? 64 : barrel.storedItem.getMaxStackSize();
            int c = barrel.getItemCount();

            if(max == 1 || c <= max)
            {
                cachedItemCount = Integer.toString(c);
            }
            else
            {
                cachedItemCount = (c / max) + "x" + max;
                int extra = c % max;
                if(extra != 0)
                {
                    cachedItemCount += "+" + extra;
                }
            }
        }

        return cachedItemCount;
    }

    public float getRotationAngle()
    {
        if(cachedRotation == -1F)
        {
            IBlockState state = worldObj.getBlockState(pos);

            if(!(state.getBlock() instanceof BlockBarrel))
            {
                return 0F;
            }

            EnumFacing facing = state.getValue(BlockHorizontal.FACING);
            cachedRotation = 0F;

            switch(facing)
            {
                case SOUTH:
                    cachedRotation = 180F;
                    break;
                case WEST:
                    cachedRotation = 270F;
                    break;
                case EAST:
                    cachedRotation = 90F;
                    break;
            }
        }

        return cachedRotation;
    }

    public void onRightClick(EntityPlayer playerIn, @Nullable ItemStack heldItem, float hitX, float y, float hitZ, EnumFacing facing, long deltaClickTime)
    {
        if(deltaClickTime <= 8)
        {
            if(barrel.storedItem != null)
            {
                for(int i = 0; i < playerIn.inventory.mainInventory.length; i++)
                {
                    ItemStack is = barrel.insertItem(0, playerIn.inventory.mainInventory[i], false);

                    if(is != playerIn.inventory.mainInventory[i])
                    {
                        playerIn.inventory.mainInventory[i].stackSize = is == null ? 0 : is.stackSize;

                        if(playerIn.inventory.mainInventory[i].stackSize <= 0)
                        {
                            playerIn.inventory.mainInventory[i] = null;
                        }
                    }
                }
            }

            playerIn.inventory.markDirty();

            if(playerIn.openContainer != null)
            {
                playerIn.openContainer.detectAndSendChanges();
            }

            return;
        }
        
        if(heldItem == null)
        {
            if(playerIn.isSneaking())
            {
                float x = 0.5F;

                switch(facing)
                {
                    case EAST:
                        x = 1F - hitZ;
                        break;
                    case WEST:
                        x = hitZ;
                        break;
                    case NORTH:
                        x = 1F - hitX;
                        break;
                    case SOUTH:
                        x = hitX;
                        break;
                }

                if(x < BUTTON_SIZE)
                {
                    IConfigTree tree = new ConfigTree();
                    MinecraftForge.EVENT_BUS.post(new YabbaCreateConfigEvent(this, barrel, tree));
                    FTBLibIntegration.API.editServerConfig((EntityPlayerMP) playerIn, null, new BasicConfigContainer(new TextComponentString("Barrel Settings"), tree));
                }
                else if(x > 1D - BUTTON_SIZE && !barrel.getFlag(IBarrel.FLAG_IS_CREATIVE))
                {
                    barrel.setFlag(IBarrel.FLAG_LOCKED, !barrel.getFlag(IBarrel.FLAG_LOCKED));

                    if(barrel.storedItem != null && barrel.itemCount == 0 && !barrel.getFlag(IBarrel.FLAG_LOCKED))
                    {
                        barrel.storedItem = null;
                    }
                }

                markDirty();
                return;
            }

            playerIn.inventory.markDirty();

            if(playerIn.openContainer != null)
            {
                playerIn.openContainer.detectAndSendChanges();
            }
        }
        else
        {
            if(heldItem.hasCapability(YabbaCommon.UPGRADE_CAPABILITY, null))
            {
                if(heldItem.getCapability(YabbaCommon.UPGRADE_CAPABILITY, null).applyOn(barrel, worldObj, heldItem, false))
                {
                    if(!heldItem.getItem().hasContainerItem(heldItem))
                    {
                        heldItem.stackSize--;
                    }

                    markDirty();
                }
            }
            else
            {
                ItemStack is = barrel.insertItem(0, heldItem, false);
                heldItem.stackSize = is == null ? 0 : is.stackSize;
            }
        }

        markDirty();
    }

    @Override
    public ItemStack getStoredItemType()
    {
        return barrel.getStackInSlot(0);
    }

    @Override
    public void setStoredItemCount(int amount)
    {
        if(amount != barrel.getItemCount() && !barrel.getFlag(IBarrel.FLAG_IS_CREATIVE))
        {
            boolean wasEmpty = barrel.getItemCount() == 0;
            barrel.setItemCount(amount);

            if(amount == 0 && !barrel.getFlag(IBarrel.FLAG_LOCKED))
            {
                barrel.setStackInSlot(0, null);
            }

            barrel.markBarrelDirty(wasEmpty != (amount == 0));
        }
    }

    @Override
    public void setStoredItemType(ItemStack type, int amount)
    {
        if(!barrel.getFlag(IBarrel.FLAG_IS_CREATIVE))
        {
            if(amount != barrel.getItemCount())
            {
                boolean wasEmpty = barrel.getItemCount() == 0;
                barrel.setItemCount(amount);

                if(amount == 0 && !barrel.getFlag(IBarrel.FLAG_LOCKED))
                {
                    type = null;
                }

                barrel.setStackInSlot(0, type);
                barrel.markBarrelDirty(wasEmpty != (amount == 0));
            }
        }
    }

    @Override
    public int getMaxStoredCount()
    {
        return barrel.getTier().getMaxItems(barrel, barrel.getStackInSlot(0));
    }

    public boolean canConnectRedstone(EnumFacing facing)
    {
        return barrel.getFlag(IBarrel.FLAG_REDSTONE_OUT);
    }

    public int redstoneOutput(EnumFacing facing)
    {
        if(barrel.getFlag(IBarrel.FLAG_REDSTONE_OUT))
        {
            int stored = barrel.getItemCount();
            int count = barrel.getUpgradeNBT().getInteger("RedstoneItemCount");
            return EnumRedstoneCompMode.getMode(barrel.getUpgradeNBT().getByte("RedstoneMode")).matchesCount(stored, count) ? 15 : 0;
        }

        ////return !((Boolean)blockState.getValue(POWERED)).booleanValue() ? 0 : (blockState.getValue(FACING) == side ? 15 : 0);
        return 0;
    }
}