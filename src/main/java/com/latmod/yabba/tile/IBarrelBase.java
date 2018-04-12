package com.latmod.yabba.tile;

import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.util.misc.DataStorage;
import com.latmod.yabba.api.BarrelType;
import com.latmod.yabba.block.Tier;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author LatvianModder
 */
public interface IBarrelBase extends IConfigCallback, ITileWithBarrelLook
{
	default BarrelType getType()
	{
		throw new IllegalStateException("Missing barrel type!");
	}

	Tier getTier();

	boolean setTier(Tier t, boolean simulate);

	void markBarrelDirty(boolean majorChange);

	boolean isLocked();

	void setLocked(boolean b);

	boolean isBarrelInvalid();

	DataStorage getUpgradeData(Item upgrade);

	boolean hasUpgrade(Item upgrade);

	boolean removeUpgrade(Item upgrade, boolean simulate);

	boolean addUpgrade(Item upgrade, boolean simulate);

	void openGui(EntityPlayer player);

	@SideOnly(Side.CLIENT)
	default void addInformation(List<String> tooltip, ITooltipFlag flagIn)
	{
	}
}