package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.EmptyCapStorage;
import com.feed_the_beast.ftbl.lib.item.ODItems;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.feed_the_beast.ftbl.lib.util.RecipeUtils;
import com.latmod.yabba.api.BarrelModelCommonData;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IUpgrade;
import com.latmod.yabba.api.Tier;
import com.latmod.yabba.api.events.YabbaModelDataEvent;
import com.latmod.yabba.block.RecipeBarrelUpgrade;
import com.latmod.yabba.tile.TileAntibarrel;
import com.latmod.yabba.tile.TileBarrel;
import com.latmod.yabba.util.EnumUpgrade;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class YabbaCommon implements YabbaModelDataEvent.YabbaModelDataRegistry
{
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

		LMUtils.register(YabbaItems.UPGRADE);
		LMUtils.register(YabbaItems.PAINTER);
		LMUtils.register(YabbaItems.HAMMER);
		LMUtils.register(YabbaItems.BARREL);
		LMUtils.register(YabbaItems.ANTIBARREL);

		GameRegistry.registerTileEntity(TileBarrel.class, Yabba.MOD_ID + ".barrel");
		GameRegistry.registerTileEntity(TileAntibarrel.class, Yabba.MOD_ID + ".antibarrel");
	}

	public void init()
	{
		RecipeUtils.addIRecipe(new RecipeBarrelUpgrade());

		ItemStack blankUpgrade = EnumUpgrade.BLANK.item();
		String id = Yabba.MOD_ID + ':';

		RecipeUtils.addRecipe(id + "upgrade_blank", ItemHandlerHelper.copyStackWithSize(blankUpgrade, YabbaConfig.CRAFTING_UPGRADE_STACK_SIZE.getInt()), 3,
				ODItems.SLAB, ODItems.SLAB, ODItems.SLAB,
				Blocks.IRON_BARS, ODItems.CHEST_WOOD, Blocks.IRON_BARS,
				ODItems.SLAB, ODItems.SLAB, ODItems.SLAB);

		if (YabbaConfig.CRAFTING_BARREL_EASY_RECIPE.getBoolean())
		{
			RecipeUtils.addRecipe(id + "barrel", YabbaItems.BARREL.createStack(DEFAULT_MODEL_ID, DEFAULT_SKIN_ID, Tier.WOOD), 1, blankUpgrade, ODItems.CHEST_WOOD);
		}
		else
		{
			RecipeUtils.addRecipe(id + "barrel", YabbaItems.BARREL.createStack(DEFAULT_MODEL_ID, DEFAULT_SKIN_ID, Tier.WOOD), 3,
					null, blankUpgrade, null,
					ODItems.SLAB, ODItems.CHEST_WOOD, ODItems.SLAB,
					null, ODItems.PLANKS, null);
		}

		RecipeUtils.addRecipe(id + "painter", new ItemStack(YabbaItems.PAINTER), 3,
				Blocks.WOOL, Blocks.WOOL, blankUpgrade,
				null, ODItems.IRON, null,
				null, ODItems.IRON, null);

		RecipeUtils.addRecipe(id + "hammer", new ItemStack(YabbaItems.HAMMER), 3,
				Blocks.WOOL, blankUpgrade, Blocks.WOOL,
				null, ODItems.IRON, null,
				null, ODItems.IRON, null);

		RecipeUtils.addCircularRecipe(id + "upgrade_iron", EnumUpgrade.IRON_UPGRADE.item(), blankUpgrade, ODItems.IRON);
		RecipeUtils.addCircularRecipe(id + "upgrade_gold", EnumUpgrade.GOLD_UPGRADE.item(), blankUpgrade, ODItems.GOLD);
		RecipeUtils.addShapelessRecipe(id + "upgrade_diamond", EnumUpgrade.DIAMOND_UPGRADE.item(), ODItems.DIAMOND, blankUpgrade, ODItems.DIAMOND);
		RecipeUtils.addShapelessRecipe(id + "upgrade_void", EnumUpgrade.VOID.item(), blankUpgrade, "dyeBlack");
		RecipeUtils.addShapelessRecipe(id + "upgrade_nether_star", EnumUpgrade.NETHER_STAR_UPGRADE.item(), blankUpgrade, ODItems.NETHERSTAR);

		RecipeUtils.addRecipe(id + "upgrade_obsidian_shell", new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.OBSIDIAN_SHELL.metadata), 3,
				null, ODItems.OBSIDIAN, null,
				ODItems.OBSIDIAN, blankUpgrade, ODItems.OBSIDIAN,
				null, ODItems.OBSIDIAN, null);

		RecipeUtils.addShapelessRecipe(id + "upgrade_redstone_out", new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.REDSTONE_OUT.metadata), blankUpgrade, Items.COMPARATOR);
		RecipeUtils.addShapelessRecipe(id + "upgrade_hopper", new ItemStack(YabbaItems.UPGRADE, 1, EnumUpgrade.HOPPER.metadata), blankUpgrade, Blocks.HOPPER);

		RecipeUtils.addRecipe(id + "antibarrel", new ItemStack(YabbaItems.ANTIBARREL), 3,
				ODItems.NETHER_INGOT, ODItems.QUARTZ_BLOCK, ODItems.NETHER_INGOT,
				ODItems.NETHER_INGOT, ODItems.OBSIDIAN, ODItems.NETHER_INGOT,
				ODItems.NETHER_INGOT, ODItems.CHEST_WOOD, ODItems.NETHER_INGOT);
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