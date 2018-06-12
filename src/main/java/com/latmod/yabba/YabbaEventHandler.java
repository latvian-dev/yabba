package com.latmod.yabba;

import com.feed_the_beast.ftblib.events.RegisterContainerProvidersEvent;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.latmod.yabba.gui.ContainerAntibarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileItemBarrel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = Yabba.MOD_ID)
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
			ItemStack stack = player.inventory.getStackInSlot(i);

			if (CommonUtils.hasBlockData(stack))
			{
				Item stackItem = stack.getItem();

				if (stackItem == YabbaItems.ITEM_BARREL_ITEM)
				{
					TileItemBarrel barrel = (TileItemBarrel) YabbaItems.ITEM_BARREL.createTileEntity(entityItem.world, YabbaItems.ITEM_BARREL.getDefaultState());
					barrel.readFromNBT(CommonUtils.getBlockData(stack));

					if (!barrel.getStoredItemType().isEmpty() && barrel.hasUpgrade(YabbaItems.UPGRADE_PICKUP))
					{
						int originalSize = itemStack.getCount();
						itemStack = barrel.insertItem(0, itemStack, false);

						if (originalSize != itemStack.getCount())
						{
							entityItem.setItem(itemStack);
							stack.setTagCompound(barrel.createItemData());
							modified = true;
						}

						if (itemStack.isEmpty())
						{
							entityItem.setDead();
							net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerItemPickupEvent(player, entityItem, itemStack);

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

	@SubscribeEvent
	public static void registerContainers(RegisterContainerProvidersEvent event)
	{
		event.register(ContainerAntibarrel.ID, (player, pos, nbt) -> new ContainerAntibarrel(player, (TileAntibarrel) player.world.getTileEntity(pos)));
	}
}