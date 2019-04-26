package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.UpgradeData;
import com.latmod.yabba.tile.Barrel;
import com.latmod.yabba.tile.ItemBarrel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ItemUpgradeSmelting extends ItemUpgrade
{
	public static class SmeltingUpgradeData extends UpgradeData
	{
		public int progress = 0;

		public SmeltingUpgradeData(ItemStack is)
		{
			super(is);
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setShort("Progress", (short) progress);
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			progress = nbt.getShort("Progress");
		}

		@Override
		public void resetData()
		{
			progress = 0;
		}

		@Override
		public boolean canInsert(Barrel barrel, EntityPlayerMP player)
		{
			return barrel.content instanceof ItemBarrel;
		}

		@Override
		public boolean isTicking(World world)
		{
			return !world.isRemote;
		}

		@Override
		public void onTick(Barrel barrel)
		{
			TileEntity tileEntity = barrel.block.getBarrelTileEntity();

			if (tileEntity == null || !tileEntity.hasWorld())
			{
				return;
			}

			progress++;

			if (progress > 400)
			{
				ItemBarrel itemBarrel = (ItemBarrel) barrel.content;

				if (itemBarrel.count > 0)
				{
					ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(itemBarrel.extractItem(1, 1, true));

					if (!stack.isEmpty())
					{
						itemBarrel.extractItem(1, 1, false);
						World world = tileEntity.getWorld();
						BlockPos pos = tileEntity.getPos();
						EnumFacing facing = barrel.block.getBarrelRotation();
						double x = pos.getX() + 0.5D + facing.getXOffset() * 0.63D;
						double y = pos.getY() + 0.8D;
						double z = pos.getZ() + 0.5D + facing.getZOffset() * 0.63D;
						EntityItem itemEntity = new EntityItem(world, x, y, z, stack.copy());
						itemEntity.motionX = facing.getXOffset() * 0.1D;
						itemEntity.motionY = 0D;
						itemEntity.motionZ = facing.getZOffset() * 0.1D;
						itemEntity.setPickupDelay(3);
						world.spawnEntity(itemEntity);
					}
				}

				progress = 0;
			}
		}
	}

	@Override
	public UpgradeData initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new SmeltingUpgradeData(stack);
	}
}