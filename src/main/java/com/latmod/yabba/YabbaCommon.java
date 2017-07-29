package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.EmptyCapStorage;
import com.feed_the_beast.ftbl.lib.block.ItemBlockBase;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.latmod.yabba.api.Barrel;
import com.latmod.yabba.api.BarrelModelCommonData;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.api.events.YabbaModelDataEvent;
import com.latmod.yabba.block.BlockAntibarrel;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.block.RecipeBarrelUpgrade;
import com.latmod.yabba.item.ItemBlockBarrel;
import com.latmod.yabba.item.ItemHammer;
import com.latmod.yabba.item.ItemPainter;
import com.latmod.yabba.item.ItemUpgrade;
import com.latmod.yabba.item.YabbaItems;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = Yabba.MOD_ID)
public class YabbaCommon implements YabbaModelDataEvent.YabbaModelDataRegistry
{
	@CapabilityInject(Barrel.class)
	public static Capability<Barrel> BARREL_CAPABILITY;

	@CapabilityInject(IUpgrade.class)
	public static Capability<IUpgrade> UPGRADE_CAPABILITY;

	private static final Map<String, BarrelModelCommonData> DATA_MAP = new HashMap<>();
	public static final String DEFAULT_MODEL_ID = "barrel";
	public static final IBlockState DEFAULT_SKIN_STATE = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
	public static final String DEFAULT_SKIN_ID = LMUtils.getName(DEFAULT_SKIN_STATE);

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				new BlockBarrel(),
				new BlockAntibarrel());

		GameRegistry.registerTileEntity(TileBarrel.class, Yabba.MOD_ID + ".barrel");
		GameRegistry.registerTileEntity(TileAntibarrel.class, Yabba.MOD_ID + ".antibarrel");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new ItemBlockBarrel(YabbaItems.BARREL),
				new ItemBlockBase(YabbaItems.ANTIBARREL),
				new ItemUpgrade(),
				new ItemHammer(),
				new ItemPainter());
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		event.getRegistry().register(new RecipeBarrelUpgrade().setRegistryName(Yabba.MOD_ID + ":upgrade_barrel"));
	}

	public void preInit()
	{
		CapabilityManager.INSTANCE.register(IUpgrade.class, new EmptyCapStorage<>(), () -> EnumUpgrade.BLANK);
		CapabilityManager.INSTANCE.register(Barrel.class, new EmptyCapStorage<>(), () -> null);
	}

	public void postInit()
	{
		new YabbaModelDataEvent(this).post();
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