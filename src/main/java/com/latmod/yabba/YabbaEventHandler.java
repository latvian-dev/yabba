package com.latmod.yabba;

import com.feed_the_beast.ftblib.events.RegisterContainerProvidersEvent;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.io.DataReader;
import com.google.gson.JsonElement;
import com.latmod.yabba.gui.ContainerAntibarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileItemBarrel;
import com.latmod.yabba.util.BarrelModelCustomData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

/**
 * @author LatvianModder
 */
@EventHandler
public class YabbaEventHandler
{
	public static final ResourceLocation RELOAD_CONFIG = new ResourceLocation(Yabba.MOD_ID, "config");
	public static final ResourceLocation RELOAD_MODEL_DATA = new ResourceLocation(Yabba.MOD_ID, "model_data");

	@SubscribeEvent
	public static void registerReloadIds(ServerReloadEvent.RegisterIds event)
	{
		event.register(RELOAD_CONFIG);
		event.register(RELOAD_MODEL_DATA);
	}

	@SubscribeEvent
	public static void onServerReload(ServerReloadEvent event)
	{
		if (event.reload(RELOAD_CONFIG))
		{
			YabbaConfig.sync();
		}

		if (event.reload(RELOAD_MODEL_DATA))
		{
			YabbaCommon.DATA_MAP.clear();

			for (ModContainer mod : Loader.instance().getModList())
			{
				try
				{
					JsonElement json = DataReader.get(YabbaEventHandler.class.getResourceAsStream("/assets/" + mod.getModId() + "/yabba_models/_custom_data.json")).json();

					if (json.isJsonObject())
					{
						for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
						{
							YabbaCommon.DATA_MAP.put(new ResourceLocation(mod.getModId(), entry.getKey()).toString(), BarrelModelCustomData.from(entry.getValue()));
						}
					}
				}
				catch (Exception ex)
				{
				}
			}
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

					if (!barrel.getStoredItemType().isEmpty() && barrel.hasUpgrade(YabbaItems.UPGRADE_PICKUP))
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