package com.latmod.yabba.tile;

import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.YabbaRegistry;
import com.latmod.yabba.api.BarrelTier;
import com.latmod.yabba.block.Barrel;
import com.latmod.yabba.net.MessageUpdateBarrelFull;
import com.latmod.yabba.net.MessageUpdateBarrelItemCount;
import com.latmod.yabba.net.YabbaNetHandler;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 13.12.2016.
 */
public class TileBarrel extends TileEntity implements ITickable
{
    public final BarrelTile barrel;
    private String cachedItemName, cachedItemCount;
    private float cachedRotation;
    private boolean isDirty = true, updateNumber = false;
    public boolean requestClientUpdate = true;

    public static class BarrelTile extends Barrel implements INBTSerializable<NBTTagCompound>
    {
        private final TileBarrel tile;
        private BarrelTier tier;
        private ItemStack storedItem;
        private int itemCount;
        private NBTTagCompound upgrades;

        public BarrelTile(TileBarrel t)
        {
            tile = t;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("Tier", getTier().getTierID());

            if(storedItem != null)
            {
                nbt.setTag("Item", storedItem.serializeNBT());
                nbt.setInteger("Count", getItemCount());
            }

            if(upgrades != null)
            {
                nbt.setTag("Upgrades", upgrades);
            }

            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            tier = YabbaRegistry.INSTANCE.getTier(nbt.getString("Tier"));
            storedItem = nbt.hasKey("Item") ? ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item")) : null;
            itemCount = storedItem == null ? 0 : nbt.getInteger("Count");
            upgrades = nbt.hasKey("Upgrades") ? nbt.getCompoundTag("Upgrades") : null;
        }

        @Override
        public BarrelTier getTier()
        {
            return tier == null ? BarrelTier.NONE : tier;
        }

        @Override
        public void setTier(BarrelTier t)
        {
            tier = t;
        }

        @Override
        public int getItemCount()
        {
            return itemCount;
        }

        @Override
        public void setItemCount(int v)
        {
            itemCount = v;
        }

        @Override
        public NBTTagCompound getUpgradeNBT()
        {
            return upgrades;
        }

        @Override
        public void setUpgradeNBT(@Nullable NBTTagCompound nbt)
        {
            upgrades = nbt;
        }

        @Override
        public void setStackInSlot(int slot, @Nullable ItemStack stack)
        {
            storedItem = stack;
        }

        @Override
        @Nullable
        public ItemStack getStackInSlot(int slot)
        {
            return storedItem;
        }

        @Override
        public void updateCounter(boolean full)
        {
            if(full)
            {
                tile.markDirty();
            }
            else
            {
                tile.updateNumber = true;
            }
        }
    }

    public TileBarrel()
    {
        barrel = new BarrelTile(this);
        clearCachedData();
    }

    public void clearCachedData()
    {
        cachedItemName = null;
        cachedItemCount = null;
        cachedRotation = -1F;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        barrel.deserializeNBT(nbt.getCompoundTag("Barrel"));
        clearCachedData();
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
        if(isDirty)
        {
            super.markDirty();

            if(!worldObj.isRemote)
            {
                NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 300);
                YabbaNetHandler.NET.sendToAllAround(new MessageUpdateBarrelFull(this), targetPoint);
            }

            isDirty = false;
        }

        if(updateNumber)
        {
            if(!worldObj.isRemote)
            {
                NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 300);
                YabbaNetHandler.NET.sendToAllAround(new MessageUpdateBarrelItemCount(this), targetPoint);
            }

            updateNumber = false;
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        clearCachedData();
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void markDirty()
    {
        isDirty = true;
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
            if(barrel.getTier().equals(YabbaCommon.TIER_CREATIVE))
            {
                cachedItemCount = "\u221E";
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
            EnumFacing facing = worldObj.getBlockState(getPos()).getValue(BlockHorizontal.FACING);
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

    public void onRightClick(EntityPlayer playerIn, @Nullable ItemStack heldItem)
    {
        if(heldItem == null)
        {
            if(playerIn.isSneaking())
            {
                if(barrel.getUpgradeData("Lockable") != null)
                {
                    NBTBase nbt = barrel.getUpgradeData("Locked");
                    barrel.setUpgradeData("Locked", new NBTTagByte(((byte) (nbt == null || ((NBTPrimitive) nbt).getByte() == 0 ? 1 : 0))));
                }

                return;
            }

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
        }
        else
        {
            ItemStack is = barrel.insertItem(0, heldItem, false);
            heldItem.stackSize = is == null ? 0 : is.stackSize;
        }

        markDirty();
    }

    public boolean onLeftClick(EntityPlayer playerIn, @Nullable ItemStack heldItem)
    {
        if(barrel.storedItem != null && barrel.itemCount > 0)
        {
            int size = 1;

            if(playerIn.isSneaking())
            {
                size = barrel.storedItem.getMaxStackSize();
            }

            ItemStack stack = barrel.extractItem(0, size, false);

            if(stack != null)
            {
                if(playerIn.inventory.addItemStackToInventory(stack))
                {
                    playerIn.inventory.markDirty();

                    if(playerIn.openContainer != null)
                    {
                        playerIn.openContainer.detectAndSendChanges();
                    }
                }
                else
                {
                    EntityItem ei = new EntityItem(playerIn.worldObj, playerIn.posX, playerIn.posY, playerIn.posZ, stack);
                    ei.motionX = ei.motionY = ei.motionZ = 0D;
                    ei.setPickupDelay(0);
                    playerIn.worldObj.spawnEntityInWorld(ei);
                }
            }

            playerIn.swingProgress = 0;
            markDirty();
            return true;
        }

        return barrel.getItemCount() > 0;
    }
}