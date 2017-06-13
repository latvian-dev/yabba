package com.latmod.yabba.api.events;

import com.latmod.yabba.api.IBarrelSkin;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author LatvianModder
 */
public class YabbaSkinsEvent extends Event
{
	public interface YabbaSkinRegistry
	{
		void addSkin(IBarrelSkin skin);

		IBarrelSkin addSkin(IBlockState state, String icons, String uname);

		default IBarrelSkin addSkin(IBlockState state, String icons)
		{
			return addSkin(state, icons, "");
		}

		default IBarrelSkin addSkin(Block block, String icons)
		{
			return addSkin(block.getDefaultState(), icons);
		}
	}

	private final YabbaSkinRegistry registry;

	public YabbaSkinsEvent(YabbaSkinRegistry reg)
	{
		registry = reg;
	}

	public YabbaSkinRegistry getRegistry()
	{
		return registry;
	}
}