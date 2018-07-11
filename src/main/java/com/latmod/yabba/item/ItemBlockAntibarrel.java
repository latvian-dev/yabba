package com.latmod.yabba.item;

import com.feed_the_beast.ftblib.lib.block.ItemBlockBase;
import com.feed_the_beast.ftblib.lib.item.ItemEntry;
import com.feed_the_beast.ftblib.lib.item.ItemEntryWithCount;
import com.latmod.yabba.YabbaConfig;
import com.latmod.yabba.util.AntibarrelData;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemBlockAntibarrel extends ItemBlockBase
{
	public ItemBlockAntibarrel(Block block)
	{
		super(block);
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		AntibarrelData data = AntibarrelData.get(stack);
		return data == null || data.items.isEmpty() ? 16 : 1;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new AntibarrelData(null);
	}

	@Override
	@Nullable
	public NBTTagCompound getNBTShareTag(ItemStack stack)
	{
		AntibarrelData data = AntibarrelData.get(stack);
		NBTTagCompound nbt = new NBTTagCompound();

		if (data.items.isEmpty())
		{
			return nbt;
		}

		NBTTagList list = new NBTTagList();

		for (ItemEntryWithCount entry : data.items.values())
		{
			if (!entry.isEmpty())
			{
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setInteger("I", Item.REGISTRY.getIDForObject(entry.entry.item));

				if (entry.count != 1)
				{
					if (entry.count <= 127)
					{
						nbt1.setByte("S", (byte) entry.count);
					}
					else
					{
						nbt1.setInteger("S", entry.count);
					}
				}

				if (entry.entry.metadata != 0)
				{
					nbt1.setShort("M", (short) entry.entry.metadata);
				}

				if (entry.entry.nbt != null)
				{
					nbt1.setTag("N", entry.entry.nbt);
				}

				if (entry.entry.caps != null)
				{
					nbt1.setTag("C", entry.entry.caps);
				}

				list.appendTag(nbt1);
			}
		}

		nbt.setTag("Inv", list);
		return nbt;
	}

	@Override
	public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		if (nbt == null)
		{
			return;
		}

		AntibarrelData data = AntibarrelData.get(stack);
		data.clear();

		NBTTagList list = nbt.getTagList("Inv", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			Item item = Item.REGISTRY.getObjectById(nbt1.getInteger("I"));

			if (item != null && item != Items.AIR)
			{
				int size = nbt1.getInteger("S");
				int meta = nbt1.getShort("M");
				NBTTagCompound tag = (NBTTagCompound) nbt1.getTag("N");
				NBTTagCompound caps = (NBTTagCompound) nbt1.getTag("C");
				ItemEntryWithCount entryc = new ItemEntryWithCount(new ItemEntry(item, meta, tag, caps), size == 0 ? 1 : size);

				if (!entryc.isEmpty())
				{
					data.items.put(entryc.entry, entryc);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		AntibarrelData data = AntibarrelData.get(stack);
		tooltip.add(I18n.format("tile.yabba.antibarrel.tooltip"));
		tooltip.add(I18n.format("tile.yabba.antibarrel.items", data.getTotalItemCount(), data.items.size(), YabbaConfig.general.antibarrel_capacity));
	}
}