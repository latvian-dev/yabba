package com.latmod.yabba;

import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.events.YabbaRegistryEvent;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.models.ModelCrate;
import com.latmod.yabba.net.MessageSyncData;
import com.latmod.yabba.net.YabbaNetHandler;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Locale;

/**
 * Created by LatvianModder on 14.12.2016.
 */
public class YabbaEventHandler
{
    @SubscribeEvent
    public void onRegistryEvent(YabbaRegistryEvent event)
    {
        IYabbaRegistry reg = event.getRegistry();

        for(BlockPlanks.EnumType type : BlockPlanks.EnumType.values())
        {
            if(type != BlockPlanks.EnumType.OAK)
            {
                reg.addSkin(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, type),
                        new ItemStack(Blocks.PLANKS, 1, type.getMetadata()),
                        "all=blocks/planks_" + type.getUnlocalizedName());
            }

            String woodTex = "blocks/log_" + type.getUnlocalizedName();

            if(type.getMetadata() < 4)
            {
                reg.addSkin(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, type), new ItemStack(Blocks.LOG, 1, type.getMetadata()), "up&down=" + woodTex + "_top,all=" + woodTex);
            }
            else
            {
                reg.addSkin(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, type), new ItemStack(Blocks.LOG2, 1, type.getMetadata() - 4), "up&down=" + woodTex + "_top,all=" + woodTex);
            }
        }

        reg.addSkin(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE), new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.getMetadata()), "all=blocks/stone");

        for(BlockStone.EnumType type : BlockStone.EnumType.values())
        {
            if(type != BlockStone.EnumType.STONE)
            {
                reg.addSkin(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, type), new ItemStack(Blocks.STONE, 1, type.getMetadata()), "all=blocks/stone_" + type.name().toLowerCase(Locale.ENGLISH));
            }
        }

        reg.addSkin(Blocks.DIRT.getDefaultState(), "dirt", "all=blocks/dirt");
        reg.addSkin(Blocks.STONEBRICK.getDefaultState(), Blocks.STONEBRICK, "all=blocks/stonebrick");
        reg.addSkin(Blocks.BRICK_BLOCK.getDefaultState(), Blocks.BRICK_BLOCK, "all=blocks/brick");
        reg.addSkin(Blocks.OBSIDIAN.getDefaultState(), "obsidian", "all=blocks/obsidian");
        reg.addSkin(Blocks.END_BRICKS.getDefaultState(), Blocks.END_BRICKS, "all=blocks/end_bricks");
        reg.addSkin(Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK, "all=blocks/nether_brick");
        reg.addSkin(Blocks.RED_NETHER_BRICK.getDefaultState(), Blocks.RED_NETHER_BRICK, "all=blocks/red_nether_brick");
        reg.addSkin(Blocks.PRISMARINE.getDefaultState(), "blockPrismarineBrick", "all=blocks/prismarine_bricks");
        reg.addSkin(Blocks.DRAGON_EGG.getDefaultState(), Blocks.DRAGON_EGG, "all=blocks/dragon_egg");
        reg.addSkin(Blocks.MELON_BLOCK.getDefaultState(), Blocks.MELON_BLOCK, "up&down=minecraft:blocks/melon_top,all=minecraft:blocks/melon_side");
        reg.addSkin(Blocks.PUMPKIN.getDefaultState(), Blocks.PUMPKIN, "up&down=blocks/pumpkin_top,all=blocks/pumpkin_side");
        reg.addSkin(Blocks.ICE.getDefaultState(), Blocks.ICE, "all=blocks/ice");
        reg.addSkin(Blocks.GLASS.getDefaultState(), Blocks.GLASS, "all=blocks/glass");
        reg.addSkin(Blocks.GLOWSTONE.getDefaultState(), Blocks.GLOWSTONE, "all=blocks/glowstone");
        reg.addSkin(Blocks.MAGMA.getDefaultState(), Blocks.MAGMA, "all=blocks/magma");
        reg.addSkin(Blocks.NOTEBLOCK.getDefaultState(), Blocks.NOTEBLOCK, "all=blocks/jukebox_side");
        reg.addSkin(Blocks.WATER.getDefaultState(), Items.WATER_BUCKET, "all=blocks/water_still");
        reg.addSkin(Blocks.LAVA.getDefaultState(), Items.LAVA_BUCKET, "all=blocks/lava_still");
        reg.addSkin(Blocks.PORTAL.getDefaultState(), null, "all=blocks/portal");
        reg.addSkin(Blocks.GOLD_BLOCK.getDefaultState(), "blockGold", "all=blocks/gold_block");
        reg.addSkin(Blocks.IRON_BLOCK.getDefaultState(), "blockIron", "all=blocks/iron_block");
        reg.addSkin(Blocks.LAPIS_BLOCK.getDefaultState(), "blockLapis", "all=blocks/lapis_block");
        reg.addSkin(Blocks.DIAMOND_BLOCK.getDefaultState(), "blockDiamond", "all=blocks/diamond_block");
        reg.addSkin(Blocks.REDSTONE_BLOCK.getDefaultState(), "blockRedstone", "all=blocks/redstone_block");
        reg.addSkin(Blocks.EMERALD_BLOCK.getDefaultState(), "blockEmerald", "all=blocks/emerald_block");
        reg.addSkin(Blocks.QUARTZ_BLOCK.getDefaultState(), "blockQuartz", "all=blocks/quartz_block_lines_top");
        reg.addSkin(Blocks.COAL_BLOCK.getDefaultState(), "blockCoal", "all=blocks/coal_block");
        reg.addSkin(Blocks.BONE_BLOCK.getDefaultState(), Blocks.BONE_BLOCK, "up&down=blocks/bone_block_top,all=blocks/bone_block_side");
        reg.addSkin(Blocks.HAY_BLOCK.getDefaultState(), Blocks.HAY_BLOCK, "up&down=blocks/hay_block_top,all=blocks/hay_block_side");
        reg.addSkin(Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF, "up&down=blocks/planks_oak,all=blocks/bookshelf");

        for(EnumDyeColor dye : EnumDyeColor.values())
        {
            reg.addSkin(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, dye),
                    new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, dye.getMetadata()),
                    "all=blocks/hardened_clay_stained_" + dye.getName());

            reg.addSkin(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, dye),
                    new ItemStack(Blocks.WOOL, 1, dye.getMetadata()),
                    "all=blocks/wool_colored_" + dye.getName());
        }

        reg.addTier(YabbaCommon.TIER_DIRT);
        reg.addTier(YabbaCommon.TIER_WOOD);
        reg.addTier(YabbaCommon.TIER_IRON);
        reg.addTier(YabbaCommon.TIER_GOLD);
        reg.addTier(YabbaCommon.TIER_DMD);

        reg.addModel(ModelBarrel.INSTANCE);
        reg.addModel(ModelCrate.INSTANCE);
    }

    @SubscribeEvent
    public void loadWorldData(WorldEvent.Load event)
    {
        if(!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0)
        {
            YabbaRegistry.INSTANCE.loadData(new File(event.getWorld().getSaveHandler().getWorldDirectory(), "data/yabba.dat"));
        }
    }

    @SubscribeEvent
    public void saveWorldData(WorldEvent.Save event)
    {
        if(!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0)
        {
            YabbaRegistry.INSTANCE.saveData(new File(event.getWorld().getSaveHandler().getWorldDirectory(), "data/yabba.dat"));
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if(event.player instanceof EntityPlayerMP)
        {
            YabbaNetHandler.NET.sendTo(new MessageSyncData(), (EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event)
    {
        World world = event.getWorld();

        if(world.isRemote)
        {
            return;
        }

        TileEntity tile = world.getTileEntity(event.getPos());

        if(tile == null || !tile.hasCapability(YabbaCommon.BARREL_CAPABILITY, null))
        {
            return;
        }

        IBlockState state = world.getBlockState(event.getPos());

        if(state.getValue(BlockHorizontal.FACING) != event.getFace())
        {
            return;
        }

        IBarrel barrel = tile.getCapability(YabbaCommon.BARREL_CAPABILITY, null);

        if(barrel instanceof IBarrelModifiable && onLeftClick((IBarrelModifiable) barrel, event.getEntityPlayer(), event.getItemStack()))
        {
            event.setCanceled(true);
        }
    }

    private static boolean onLeftClick(IBarrelModifiable barrel, EntityPlayer playerIn, @Nullable ItemStack heldItem)
    {
        ItemStack storedItem = barrel.getStackInSlot(0);
        if(storedItem != null && barrel.getItemCount() == 0 && (barrel.getFlags() & IBarrel.FLAG_LOCKED) == 0)
        {
            barrel.setStackInSlot(0, null);
            barrel.markBarrelDirty(true);
            return true;
        }

        if(storedItem != null && barrel.getItemCount() > 0)
        {
            int size = 1;

            if(playerIn.isSneaking())
            {
                size = storedItem.getMaxStackSize();
            }

            ItemStack stack = barrel.extractItem(0, size, false);

            if(stack != null)
            {
                if(playerIn.inventory.addItemStackToInventory(stack))
                {
                    playerIn.inventory.markDirty();

                    if(playerIn.openContainer != null)
                    {
                        playerIn.openContainer.detectAndSendChanges();
                    }
                }
                else
                {
                    EntityItem ei = new EntityItem(playerIn.worldObj, playerIn.posX, playerIn.posY, playerIn.posZ, stack);
                    ei.motionX = ei.motionY = ei.motionZ = 0D;
                    ei.setPickupDelay(0);
                    playerIn.worldObj.spawnEntityInWorld(ei);
                }
            }

            return !playerIn.isSneaking();
        }

        return barrel.getItemCount() > 0;
    }
}