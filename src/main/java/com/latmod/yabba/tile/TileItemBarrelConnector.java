package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import com.feed_the_beast.ftblib.lib.tile.TileBase;
import com.latmod.yabba.YabbaConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TileItemBarrelConnector extends TileBase implements IItemHandlerModifiable, ITickable
{
	private static final HashSet<TileItemBarrelConnector> ALL_CONNECTORS = new HashSet<>();

	private static final Comparator<TileItemBarrel> BARREL_COMPARATOR = (o1, o2) -> {
		int i = Boolean.compare(o1.isLocked.getBoolean(), o2.isLocked.getBoolean());
		return i == 0 ? o2.itemCount - o1.itemCount : i;
	};

	public static void markAllDirty(int dimension)
	{
		Iterator<TileItemBarrelConnector> iterator = ALL_CONNECTORS.iterator();

		while (iterator.hasNext())
		{
			TileItemBarrelConnector connector = iterator.next();

			if (connector.isInvalid())
			{
				iterator.remove();
			}
			else if (connector.world != null && dimension == connector.world.provider.getDimension())
			{
				connector.markDirty();
			}
		}
	}

	public final List<TileItemBarrel> linkedBarrels = new ArrayList<>();
	private long lastUpdate = 0L;

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? (T) this : super.getCapability(capability, facing);
	}

	@Override
	public void validate()
	{
		super.validate();
		ALL_CONNECTORS.add(this);
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		ALL_CONNECTORS.remove(this);
	}

	@Override
	public void update()
	{
		checkIfDirty();
	}

	@Override
	public boolean notifyBlock()
	{
		return false;
	}

	@Override
	protected void sendDirtyUpdate()
	{
		lastUpdate = 0L;
		super.sendDirtyUpdate();
	}

	public void updateLinks()
	{
		linkedBarrels.clear();

		if (world != null && !world.isRemote)
		{
			HashSet<TileItemBarrel> scanned = new HashSet<>();

			for (EnumFacing facing : EnumFacing.VALUES)
			{
				addToList(scanned, pos.offset(facing), facing.getOpposite());
			}

			linkedBarrels.addAll(scanned);
			linkedBarrels.sort(BARREL_COMPARATOR);
		}
	}

	private void addToList(HashSet<TileItemBarrel> scanned, BlockPos pos, EnumFacing from)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileItemBarrel && scanned.add((TileItemBarrel) tileEntity))
		{
			for (EnumFacing facing1 : EnumFacing.VALUES)
			{
				if (facing1 != from)
				{
					addToList(scanned, pos.offset(facing1), facing1.getOpposite());
				}
			}
		}
	}

	@Nullable
	private TileItemBarrel getAt(int slot)
	{
		if (world == null || world.isRemote)
		{
			return null;
		}
		else if (world.getTotalWorldTime() - lastUpdate >= YabbaConfig.general.connector_update_ticks)
		{
			lastUpdate = world.getTotalWorldTime();
			updateLinks();
		}

		TileItemBarrel barrel = (slot < 0 || slot >= linkedBarrels.size()) ? null : linkedBarrels.get(slot);
		return barrel == null || barrel.isInvalid() ? null : barrel;
	}

	/*
	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		TileItemBarrel barrel = getAt(slot);

		if (barrel != null)
		{
			barrel.setStackInSlot(0, stack);
		}
	}*/

	@Override
	public int getSlots()
	{
		getAt(-1);
		return linkedBarrels.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		TileItemBarrel barrel = getAt(slot);
		return barrel == null ? ItemStack.EMPTY : barrel.getStackInSlot(0);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		TileItemBarrel barrel = getAt(slot);

		if (barrel != null)
		{
			barrel.setStackInSlot(0, stack);
		}
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		TileItemBarrel barrel = getAt(slot);
		return barrel == null ? stack : barrel.insertItem(0, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		TileItemBarrel barrel = getAt(slot);
		return barrel == null ? ItemStack.EMPTY : barrel.extractItem(0, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot)
	{
		TileItemBarrel barrel = getAt(slot);
		return barrel == null ? 64 : barrel.getSlotLimit(0);
	}
}