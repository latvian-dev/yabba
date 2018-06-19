package com.latmod.yabba.gui;

import com.feed_the_beast.ftblib.lib.gui.ContainerBase;
import com.feed_the_beast.ftblib.lib.item.ItemEntry;
import com.feed_the_beast.ftblib.lib.item.ItemEntryWithCount;
import com.feed_the_beast.ftblib.lib.item.SlotOnlyInsertItem;
import com.latmod.yabba.net.MessageAntibarrelClickSlot;
import com.latmod.yabba.net.MessageAntibarrelUpdate;
import com.latmod.yabba.tile.TileAntibarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * @author LatvianModder
 */
public class ContainerAntibarrel extends ContainerBase
{
	public final TileAntibarrel tile;
	private int totalChanges;

	public ContainerAntibarrel(EntityPlayer player, TileAntibarrel t)
	{
		super(player);
		tile = t;
		totalChanges = t.totalChanges;

		addSlotToContainer(new SlotOnlyInsertItem(tile, 0, -10000, 0)
		{
			@Override
			public void onSlotChanged()
			{
				if (!tile.getWorld().isRemote)
				{
					tile.markDirty();
				}
			}
		});

		addPlayerSlots(8, 84, false);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		if (totalChanges != tile.totalChanges)
		{
			totalChanges = tile.totalChanges;

			if (player instanceof EntityPlayerMP)
			{
				new MessageAntibarrelUpdate(tile).sendTo((EntityPlayerMP) player);
			}
		}
	}

	@Override
	public int getNonPlayerSlots()
	{
		return 1;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		BlockPos pos = tile.getPos();
		return player.world.getTileEntity(pos) == tile && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
	}

	public void onClick(ItemEntry entry, boolean shift)
	{
		ItemStack stack = player.inventory.getItemStack();

		if (entry.isEmpty() != stack.isEmpty())
		{
			if (entry.isEmpty())
			{
				if (tile.insertItem(0, stack, false).isEmpty())
				{
					player.inventory.setItemStack(ItemStack.EMPTY);
					player.inventory.markDirty();
					tile.markDirty();
				}
			}
			else
			{
				ItemEntryWithCount entryWithCount = tile.items.get(entry);

				if (entryWithCount != null && entryWithCount.count > 0)
				{
					boolean transfered;

					if (shift)
					{
						transfered = player.inventory.addItemStackToInventory(entry.getStack(1, true));
					}
					else
					{
						player.inventory.setItemStack(entry.getStack(1, true));
						player.inventory.markDirty();
						transfered = true;
					}

					if (transfered)
					{
						entryWithCount.count--;

						if (entryWithCount.count == 0)
						{
							tile.items.remove(entry);
							tile.updateContainingBlockInfo();
						}

						tile.totalChanges++;
						tile.markDirty();
					}
				}
			}
		}

		if (tile.getWorld().isRemote)
		{
			new MessageAntibarrelClickSlot(entry, shift).sendToServer();
		}
	}
}