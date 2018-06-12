package com.latmod.yabba.client;

import com.feed_the_beast.ftblib.events.client.RegisterGuiProvidersEvent;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.gui.ContainerAntibarrel;
import com.latmod.yabba.gui.GuiAntibarrel;
import com.latmod.yabba.tile.TileAntibarrel;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = Yabba.MOD_ID, value = Side.CLIENT)
public class YabbaClientEventHandler
{
	@SubscribeEvent
	public static void registerGuis(RegisterGuiProvidersEvent event)
	{
		event.register(ContainerAntibarrel.ID, (player, pos, nbt) -> new GuiAntibarrel(new ContainerAntibarrel(player, (TileAntibarrel) player.world.getTileEntity(pos))).getWrapper());
	}
}