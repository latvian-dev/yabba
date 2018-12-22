package com.latmod.yabba.item.upgrade;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.util.InvUtils;
import com.latmod.yabba.api.UpgradeData;
import com.latmod.yabba.tile.Barrel;
import com.latmod.yabba.tile.ItemBarrel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ItemUpgradeHopper extends ItemUpgrade
{
	public static class HopperUpgradeData extends UpgradeData
	{
		public boolean down = true;
		public boolean up = true;
		public boolean collect = false;

		public HopperUpgradeData(ItemStack is)
		{
			super(is);
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("Down", down);
			nbt.setBoolean("Up", up);
			nbt.setBoolean("Collect", collect);
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			down = nbt.getBoolean("Down");
			up = nbt.getBoolean("Up");
			collect = nbt.getBoolean("Collect");
		}

		@Override
		public void getConfig(Barrel barrel, ConfigGroup config)
		{
			config.addBool("up", () -> up, v -> up = v, true);
			config.addBool("down", () -> down, v -> down = v, true);
			config.addBool("collect", () -> collect, v -> collect = v, false);
		}

		@Override
		public boolean hasData()
		{
			return !down || !up || collect;
		}

		@Override
		public void resetData()
		{
			down = true;
			up = true;
			collect = false;
		}

		@Override
		public void onTick(Barrel barrel)
		{
			TileEntity tileEntity = barrel.block.getBarrelTileEntity();

			if (tileEntity == null || !tileEntity.hasWorld())
			{
				return;
			}

			World world = tileEntity.getWorld();
			BlockPos pos = tileEntity.getPos();

			if ((world.getTotalWorldTime() % 8L) == (pos.hashCode() & 7))
			{
				if (barrel.content instanceof ItemBarrel)
				{
					ItemBarrel itemBarrel = (ItemBarrel) barrel.content;
					int maxItems = 1 << barrel.getTier().transferTier;

					if (itemBarrel.count > 0 && down)
					{
						TileEntity tileDown = world.getTileEntity(pos.offset(EnumFacing.DOWN));

						if (tileDown != null && tileDown.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
						{
							InvUtils.transferItems(itemBarrel, tileDown.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP), Math.min(maxItems, itemBarrel.count), InvUtils.NO_FILTER);
						}
					}

					if (up)
					{
						TileEntity tileUp = world.getTileEntity(pos.offset(EnumFacing.UP));

						if (tileUp != null && tileUp.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
						{
							InvUtils.transferItems(tileUp.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN), itemBarrel, Math.min(maxItems, itemBarrel.getFreeSpace()), InvUtils.NO_FILTER);
						}
					}

					if (collect)
					{
						AxisAlignedBB aabb = new AxisAlignedBB(pos.add(0, 1, 0), pos.add(1, 2, 1));

						for (EntityItem item : world.getEntitiesWithinAABB(EntityItem.class, aabb, null))
						{
							ItemStack stack = itemBarrel.insertItem(0, item.getItem().copy(), false);

							if (stack.isEmpty())
							{
								item.setDead();
							}
							else
							{
								item.setItem(stack);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public UpgradeData initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new HopperUpgradeData(stack);
	}
}