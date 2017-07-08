package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.EmptyCapStorage;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.latmod.yabba.api.BarrelModelCommonData;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.api.events.YabbaModelDataEvent;
import com.latmod.yabba.block.BlockAntibarrel;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.item.ItemHammer;
import com.latmod.yabba.item.ItemPainter;
import com.latmod.yabba.item.ItemUpgrade;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class YabbaCommon implements YabbaModelDataEvent.YabbaModelDataRegistry
{
	@GameRegistry.ObjectHolder(Yabba.MOD_ID + ":barrel")
	public static BlockBarrel BARREL;

	@GameRegistry.ObjectHolder(Yabba.MOD_ID + ":antibarrel")
	public static BlockAntibarrel ANTIBARREL;

	@GameRegistry.ObjectHolder(Yabba.MOD_ID + ":upgrade")
	public static ItemUpgrade UPGRADE;

	@GameRegistry.ObjectHolder(Yabba.MOD_ID + ":painter")
	public static ItemPainter PAINTER;

	@GameRegistry.ObjectHolder(Yabba.MOD_ID + ":hammer")
	public static ItemHammer HAMMER;

	@CapabilityInject(IBarrel.class)
	public static Capability<IBarrel> BARREL_CAPABILITY;

	@CapabilityInject(IUpgrade.class)
	public static Capability<IUpgrade> UPGRADE_CAPABILITY;

	public static final YabbaCreativeTab TAB = new YabbaCreativeTab();

	private static final Map<String, BarrelModelCommonData> DATA_MAP = new HashMap<>();
	public static final String DEFAULT_MODEL_ID = "barrel";
	public static final IBlockState DEFAULT_SKIN_STATE = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
	public static final String DEFAULT_SKIN_ID = LMUtils.getName(DEFAULT_SKIN_STATE);

	public void preInit()
	{
		CapabilityManager.INSTANCE.register(IUpgrade.class, new EmptyCapStorage<>(), () -> EnumUpgrade.BLANK);
		CapabilityManager.INSTANCE.register(IBarrel.class, new EmptyCapStorage<>(), () -> null);
	}

	public void init()
	{
	}

	public void postInit()
	{
		MinecraftForge.EVENT_BUS.post(new YabbaModelDataEvent(this));
	}

	public void openModelGui()
	{
	}

	public void openSkinGui()
	{
	}

	public void loadModelsAndSkins()
	{
	}

	@Override
	public void addModelData(String id, BarrelModelCommonData data)
	{
		DATA_MAP.put(id, data);
	}

	public static BarrelModelCommonData getModelData(String id)
	{
		BarrelModelCommonData data = DATA_MAP.get(id);
		return data == null ? BarrelModelCommonData.DEFAULT : data;
	}
}