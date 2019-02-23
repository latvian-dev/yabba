package com.latmod.yabba.item.upgrade;

import com.latmod.yabba.api.UpgradeData;
import com.latmod.yabba.tile.Barrel;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ItemUpgradeCreative extends ItemUpgrade
{
	public static class CreativeUpgradeData extends UpgradeData
	{
		public CreativeUpgradeData(ItemStack is)
		{
			super(is);
		}

		@Override
		public boolean canInsert(Barrel barrel, EntityPlayerMP player)
		{
			return !barrel.isCreative() && !barrel.content.isEmpty() && (player.capabilities.isCreativeMode || barrel.content.isValidForCreativeUpgrade());
		}

		@Override
		public void onInserted(Barrel barrel, EntityPlayerMP player)
		{
			barrel.setLocked(false);
			barrel.content.onCreativeChange();
		}

		@Override
		public boolean canRemove(Barrel barrel, EntityPlayerMP player)
		{
			return player.capabilities.isCreativeMode;
		}
	}

	@Override
	public UpgradeData initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new CreativeUpgradeData(stack);
	}
}