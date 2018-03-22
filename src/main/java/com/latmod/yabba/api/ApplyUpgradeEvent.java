package com.latmod.yabba.api;

import com.latmod.yabba.tile.TileAdvancedBarrelBase;
import com.latmod.yabba.util.UpgradeInst;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class ApplyUpgradeEvent
{
	private final boolean simulate;
	private final TileAdvancedBarrelBase barrel;
	private final UpgradeInst upgrade;
	private final EntityPlayer player;
	private final EnumHand hand;
	private final EnumFacing facing;
	private boolean consumeItem = true;
	private boolean addUpgrade = true;

	public ApplyUpgradeEvent(boolean s, TileAdvancedBarrelBase b, UpgradeInst u, EntityPlayer p, EnumHand h, EnumFacing f)
	{
		simulate = s;
		barrel = b;
		upgrade = u;
		player = p;
		hand = h;
		facing = f;
	}

	public boolean simulate()
	{
		return simulate;
	}

	public World getWorld()
	{
		return getBarrel().getWorld();
	}

	public TileAdvancedBarrelBase getBarrel()
	{
		return barrel;
	}

	public UpgradeInst getUpgrade()
	{
		return upgrade;
	}

	public EntityPlayer getPlayer()
	{
		return player;
	}

	public EnumHand getHand()
	{
		return hand;
	}

	public EnumFacing getFacing()
	{
		return facing;
	}

	public ItemStack getHeldItem()
	{
		return player.getHeldItem(hand);
	}

	public void setConsumeItem(boolean consume)
	{
		consumeItem = consume;
	}

	public boolean consumeItem()
	{
		return consumeItem;
	}

	public void setAddUpgrade(boolean add)
	{
		addUpgrade = add;
	}

	public boolean addUpgrade()
	{
		return addUpgrade;
	}
}