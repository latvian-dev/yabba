package com.latmod.yabba;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.ServerReloadEvent;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@EventHandler
public class YabbaEventHandler
{
	public static final ResourceLocation RELOAD_CONFIG = new ResourceLocation(Yabba.MOD_ID, "config");

	@SubscribeEvent
	public static void registerReloadIds(ServerReloadEvent.RegisterIds event)
	{
		event.register(RELOAD_CONFIG);
	}

	@SubscribeEvent
	public static void onServerReload(ServerReloadEvent event)
	{
		if (event.reload(RELOAD_CONFIG))
		{
			YabbaConfig.sync();
		}
	}

	@SubscribeEvent
	public static void onItemPickup(EntityItemPickupEvent event)
	{
		boolean modified = false;
		EntityPlayer player = event.getEntityPlayer();
		EntityItem entityItem = event.getItem();
		ItemStack itemStack = entityItem.getItem();

		int size = player.inventory.getSizeInventory();

		for (int i = 0; i < size; i++)
		{
			ItemStack istack = player.inventory.getStackInSlot(i);

			if (!istack.isEmpty() && istack.hasTagCompound() && istack.getTagCompound().hasKey("BlockEntityTag"))
			{
				Item stackItem = istack.getItem();

				if (stackItem == YabbaItems.ITEM_BARREL_ITEM)
				{
					TileItemBarrel barrel = (TileItemBarrel) YabbaItems.ITEM_BARREL.createTileEntity(entityItem.world, YabbaItems.ITEM_BARREL.getDefaultState());
					barrel.readFromNBT(istack.getTagCompound().getCompoundTag("BlockEntityTag"));

					if (!barrel.storedItem.isEmpty() && barrel.hasUpgrade(YabbaItems.UPGRADE_PICKUP))
					{
						int originalSize = itemStack.getCount();
						itemStack = barrel.insertItem(0, itemStack, false);

						if (originalSize != itemStack.getCount())
						{
							entityItem.setItem(itemStack);
							istack.setTagCompound(barrel.createItemData());
							modified = true;
						}

						if (itemStack.isEmpty())
						{
							entityItem.setDead();
							net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerItemPickupEvent(player, entityItem);

							if (!entityItem.isSilent())
							{
								entityItem.world.playSound(null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((entityItem.world.rand.nextFloat() - entityItem.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
							}

							player.onItemPickup(entityItem, originalSize);
							break;
						}
					}
				}
			}
		}

		if (modified)
		{
			event.setCanceled(true);
		}
	}

	/*
	@SubscribeEvent
	public void pickupItems(final EntityItemPickupEvent event)
	{
		boolean modified = false;

		final EntityItem entityItem = event.getItem();
		if (entityItem != null)
		{
			final ItemStack is = entityItem.getEntityItem();
			final EntityPlayer player = event.getEntityPlayer();
			if (is != null && is.getItem() instanceof ItemChiseledBit)
			{
				final int originalSize = ModUtil.getStackSize(is);
				final IInventory inv = player.inventory;
				final List<BagPos> bags = getBags(inv);

				// has the stack?
				final boolean seen = ModUtil.containsAtLeastOneOf(inv, is);

				if (seen)
				{
					for (final BagPos i : bags)
					{
						if (!entityItem.isDead)
						{
							modified = updateEntity(player, entityItem, i.inv.insertItem(ModUtil.nonNull(entityItem.getEntityItem())), originalSize) || modified;
						}
					}
				}
				else
				{
					if (ModUtil.getStackSize(is) > is.getMaxStackSize() && !entityItem.isDead)
					{
						final ItemStack singleStack = is.copy();
						ModUtil.setStackSize(singleStack, singleStack.getMaxStackSize());

						if (player.inventory.addItemStackToInventory(singleStack) == false)
						{
							ModUtil.adjustStackSize(is, -(singleStack.getMaxStackSize() - ModUtil.getStackSize(is)));
						}

						modified = updateEntity(player, entityItem, is, originalSize) || modified;
					}
					else
					{
						return;
					}

					for (final BagPos i : bags)
					{

						if (!entityItem.isDead)
						{
							modified = updateEntity(player, entityItem, i.inv.insertItem(ModUtil.nonNull(entityItem.getEntityItem())), originalSize) || modified;
						}
					}
				}
			}

			cleanupInventory(player, is);
		}

		if (modified)
		{
			event.setCanceled(true);
		}
	}
	*/
}