package com.latmod.yabba.tile;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.config.ConfigBoolean;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.tile.EnumSaveType;
import com.feed_the_beast.ftbl.lib.tile.TileBase;
import com.feed_the_beast.ftbl.lib.util.DataStorage;
import com.google.gson.JsonObject;
import com.latmod.yabba.Yabba;
import com.latmod.yabba.YabbaCommon;
import com.latmod.yabba.api.BarrelType;
import com.latmod.yabba.api.RemoveUpgradeEvent;
import com.latmod.yabba.api.YabbaCreateConfigEvent;
import com.latmod.yabba.block.BlockStorageBarrelBase;
import com.latmod.yabba.block.Tier;
import com.latmod.yabba.item.IUpgrade;
import com.latmod.yabba.item.YabbaItems;
import com.latmod.yabba.item.upgrade.ItemUpgradeHopper;
import com.latmod.yabba.item.upgrade.ItemUpgradeRedstone;
import com.latmod.yabba.util.UpgradeInst;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class TileBarrelBase extends TileBase implements ITickable, IConfigCallback
{
	public Tier tier = Tier.WOOD;
	public Map<Item, UpgradeInst> upgrades = new HashMap<>();
	public String model = "";
	public String skin = "";
	public ConfigBoolean isLocked = new ConfigBoolean(false);
	public ConfigBoolean alwaysDisplayData = new ConfigBoolean(false);
	public ConfigBoolean displayBar = new ConfigBoolean(false);

	private float cachedRotationX, cachedRotationY;

	public TileBarrelBase()
	{
		updateContainingBlockInfo();
	}

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (tier != Tier.WOOD)
		{
			nbt.setString("Tier", tier.getName());
		}

		if (!upgrades.isEmpty())
		{
			NBTTagCompound nbt1 = new NBTTagCompound();

			for (UpgradeInst inst : upgrades.values())
			{
				ResourceLocation id = Item.REGISTRY.getNameForObject(inst.getItem());

				if (id != null)
				{
					NBTTagCompound nbt2 = new NBTTagCompound();

					if (!inst.getData().isEmpty())
					{
						inst.getData().serializeNBT(nbt2, type);
					}

					nbt1.setTag(id.toString(), nbt2);
				}
			}

			nbt.setTag("Upgrades", nbt1);
		}

		if (!model.isEmpty())
		{
			nbt.setString("Model", model);
		}

		if (!skin.isEmpty())
		{
			nbt.setString("Skin", skin);
		}

		if (isLocked.getBoolean())
		{
			nbt.setBoolean("Locked", true);
		}

		if (alwaysDisplayData.getBoolean())
		{
			nbt.setBoolean("AlwaysDisplayData", true);
		}

		if (displayBar.getBoolean())
		{
			nbt.setBoolean("DisplayBar", true);
		}
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		tier = Tier.NAME_MAP.get(nbt.getString("Tier"));

		upgrades.clear();
		NBTTagCompound upgradeTag = nbt.getCompoundTag("Upgrades");

		for (String s : upgradeTag.getKeySet())
		{
			Item item = Item.REGISTRY.getObject(new ResourceLocation(s));

			if (item instanceof IUpgrade)
			{
				UpgradeInst inst = new UpgradeInst(item);

				if (!inst.getData().isEmpty())
				{
					inst.getData().deserializeNBT(upgradeTag.getCompoundTag(s), type);
				}

				upgrades.put(item, inst);
			}
		}

		model = nbt.getString("Model");
		skin = nbt.getString("Skin");
		isLocked.setBoolean(nbt.getBoolean("Locked"));
		alwaysDisplayData.setBoolean(nbt.getBoolean("AlwaysDisplayData"));
		displayBar.setBoolean(nbt.getBoolean("DisplayBar"));
	}

	public BarrelType getType()
	{
		throw new IllegalStateException("Missing barrel type!");
	}

	@Override
	public IBlockState createState(IBlockState state)
	{
		if (state instanceof IExtendedBlockState)
		{
			return ((IExtendedBlockState) state).withProperty(BlockStorageBarrelBase.MODEL, model).withProperty(BlockStorageBarrelBase.SKIN, skin);
		}

		return state;
	}

	public void markBarrelDirty(boolean majorChange)
	{
		if (majorChange)
		{
			updateContainingBlockInfo();
		}
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		cachedRotationX = -1F;
		cachedRotationY = -1F;
	}

	@Override
	public void onUpdatePacket()
	{
		updateContainingBlockInfo();
	}

	@Override
	public void update()
	{
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		updateContainingBlockInfo();
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public void markDirty()
	{
		//barrel.markBarrelDirty();
	}

	public float getRotationAngleX()
	{
		if (cachedRotationX == -1F)
		{
			IBlockState state = getBlockState();

			if (!(state.getBlock() instanceof BlockStorageBarrelBase))
			{
				return 0F;
			}

			cachedRotationX = state.getValue(BlockStorageBarrelBase.ROTATION).ordinal() * 90F;
		}

		return cachedRotationX;
	}

	public float getRotationAngleY()
	{
		if (cachedRotationY == -1F)
		{
			IBlockState state = getBlockState();

			if (!(state.getBlock() instanceof BlockStorageBarrelBase))
			{
				return 0F;
			}

			cachedRotationY = state.getValue(BlockHorizontal.FACING).getHorizontalAngle() + 180F;
		}

		return cachedRotationY;
	}

	public boolean setTier(Tier t, boolean simulate)
	{
		if (tier != t)
		{
			if (!simulate)
			{
				tier = t;
				markBarrelDirty(true);
			}

			return true;
		}

		return false;
	}

	public boolean setSkin(String v, boolean simulate)
	{
		if (v.equals(YabbaCommon.DEFAULT_SKIN_ID))
		{
			v = "";
		}

		if (!skin.equals(v))
		{
			if (!simulate)
			{
				skin = v;
				markBarrelDirty(true);
			}

			return true;
		}

		return false;
	}

	public boolean setModel(String v, boolean simulate)
	{
		if (v.equals(YabbaCommon.DEFAULT_MODEL_ID))
		{
			v = "";
		}

		if (!model.equals(v))
		{
			if (!simulate)
			{
				model = v;
				markBarrelDirty(true);
			}

			return true;
		}

		return false;
	}

	public DataStorage getUpgradeData(Item upgrade)
	{
		UpgradeInst inst = upgrades.get(upgrade);
		return inst == null ? DataStorage.EMPTY : inst.getData();
	}

	public boolean hasUpgrade(Item upgrade)
	{
		return upgrades.containsKey(upgrade);
	}

	public boolean removeUpgrade(Item upgrade, boolean simulate)
	{
		if (hasUpgrade(upgrade))
		{
			if (!simulate)
			{
				UpgradeInst inst = upgrades.remove(upgrade);
				inst.getUpgrade().onRemoved(new RemoveUpgradeEvent(this, inst));
				markBarrelDirty(true);
			}

			return true;
		}

		return false;
	}

	public boolean addUpgrade(Item upgrade, boolean simulate)
	{
		if (upgrade instanceof IUpgrade && !hasUpgrade(upgrade))
		{
			if (!simulate)
			{
				upgrades.put(upgrade, new UpgradeInst(upgrade));
				markBarrelDirty(true);
			}

			return true;
		}

		return false;
	}

	public boolean canConnectRedstone(@Nullable EnumFacing facing)
	{
		return getUpgradeData(YabbaItems.UPGRADE_REDSTONE_OUT) instanceof ItemUpgradeRedstone.Data;
	}

	public int redstoneOutput(EnumFacing facing)
	{
		return 0;
	}

	public String getItemDisplayName()
	{
		return "ERROR";
	}

	public String getItemDisplayCount(boolean isSneaking, boolean isCreative, boolean infinite)
	{
		return "ERROR";
	}

	@Override
	public boolean shouldDrop()
	{
		return super.shouldDrop() || !skin.isEmpty() || !model.isEmpty() || !upgrades.isEmpty() || tier != Tier.WOOD || isLocked.getBoolean();
	}

	public void addItem(EntityPlayer player, EnumHand hand)
	{
	}

	public void addAllItems(EntityPlayer player, EnumHand hand)
	{
	}

	public void removeItem(EntityPlayer player, boolean stack)
	{
	}

	@Override
	public void saveConfig(ConfigTree tree, ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
	{
		tree.fromJson(json);
		markBarrelDirty(true);
	}

	public void createConfig(YabbaCreateConfigEvent event)
	{
		String group = Yabba.MOD_ID;

		event.add(group, "always_display_data", alwaysDisplayData).setNameLangKey("yabba_client.config.general.always_display_data");
		event.add(group, "display_bar", displayBar).setNameLangKey("yabba_client.config.general.display_bar");

		DataStorage data = getUpgradeData(YabbaItems.UPGRADE_REDSTONE_OUT);
		if (data instanceof ItemUpgradeRedstone.Data)
		{
			group = Yabba.MOD_ID + ".redstone";
			ItemUpgradeRedstone.Data data1 = (ItemUpgradeRedstone.Data) data;
			event.add(group, "mode", data1.mode);
			event.add(group, "count", data1.count);
		}

		data = getUpgradeData(YabbaItems.UPGRADE_HOPPER);
		if (data instanceof ItemUpgradeHopper.Data)
		{
			group = Yabba.MOD_ID + ".hopper";
			ItemUpgradeHopper.Data data1 = (ItemUpgradeHopper.Data) data;
			event.add(group, "up", data1.up);
			event.add(group, "down", data1.down);
			event.add(group, "collect", data1.collect);
		}
	}

	public final void displayConfig(EntityPlayer player)
	{
		ConfigTree configTree = new ConfigTree();
		YabbaCreateConfigEvent event = new YabbaCreateConfigEvent(this, configTree, player);
		event.post();
		createConfig(event);
		FTBLibAPI.API.editServerConfig((EntityPlayerMP) player, configTree, getDisplayName(), null, this);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip, ITooltipFlag flagIn)
	{
		if (!upgrades.isEmpty())
		{
			tooltip.add("Upgrades:"); //LANG

			for (UpgradeInst upgrade : upgrades.values())
			{
				tooltip.add("> " + upgrade.getStack().getDisplayName());
			}
		}
	}
}