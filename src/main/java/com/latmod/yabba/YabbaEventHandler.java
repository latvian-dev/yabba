package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.config.SimpleConfigKey;
import com.latmod.yabba.api.IBarrel;
import com.latmod.yabba.api.IBarrelModifiable;
import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.events.YabbaCreateConfigEvent;
import com.latmod.yabba.api.events.YabbaRegistryEvent;
import com.latmod.yabba.block.BlockBarrel;
import com.latmod.yabba.models.ModelCrate;
import com.latmod.yabba.models.ModelPanel;
import com.latmod.yabba.models.ModelSolid;
import com.latmod.yabba.models.ModelSolidBorders;
import com.latmod.yabba.util.EnumRedstoneCompMode;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

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
                reg.addSkin(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, type), "all=blocks/planks_" + type.getUnlocalizedName());
            }

            String woodTex = "blocks/log_" + type.getUnlocalizedName();

            if(type.getMetadata() < 4)
            {
                reg.addSkin(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, type), "up&down=" + woodTex + "_top,all=" + woodTex);
            }
            else
            {
                reg.addSkin(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, type), "up&down=" + woodTex + "_top,all=" + woodTex);
            }
        }

        reg.addSkin(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE), "all=blocks/stone");

        for(BlockStone.EnumType type : BlockStone.EnumType.values())
        {
            if(type != BlockStone.EnumType.STONE)
            {
                reg.addSkin(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, type), "all=blocks/stone_" + type.name().toLowerCase());
            }
        }

        reg.addSkin(Blocks.DIRT.getDefaultState(), "all=blocks/dirt");
        reg.addSkin(Blocks.STONEBRICK.getDefaultState(), "all=blocks/stonebrick");
        reg.addSkin(Blocks.BRICK_BLOCK.getDefaultState(), "all=blocks/brick");
        reg.addSkin(Blocks.OBSIDIAN.getDefaultState(), "all=blocks/obsidian");
        reg.addSkin(Blocks.END_BRICKS.getDefaultState(), "all=blocks/end_bricks");
        reg.addSkin(Blocks.NETHER_BRICK.getDefaultState(), "all=blocks/nether_brick");
        reg.addSkin(Blocks.RED_NETHER_BRICK.getDefaultState(), "all=blocks/red_nether_brick");
        reg.addSkin(Blocks.PRISMARINE.getDefaultState(), "all=blocks/prismarine_bricks");
        reg.addSkin(Blocks.DRAGON_EGG.getDefaultState(), "all=blocks/dragon_egg");
        reg.addSkin(Blocks.MELON_BLOCK.getDefaultState(), "up&down=minecraft:blocks/melon_top,all=minecraft:blocks/melon_side");
        reg.addSkin(Blocks.PUMPKIN.getDefaultState(), "up&down=blocks/pumpkin_top,all=blocks/pumpkin_side");
        reg.addSkin(Blocks.ICE.getDefaultState(), "all=blocks/ice");
        reg.addSkin(Blocks.GLASS.getDefaultState(), "all=blocks/glass");
        reg.addSkin(Blocks.GLOWSTONE.getDefaultState(), "all=blocks/glowstone");
        reg.addSkin(Blocks.MAGMA.getDefaultState(), "all=blocks/magma");
        reg.addSkin(Blocks.NOTEBLOCK.getDefaultState(), "all=blocks/jukebox_side");
        reg.addSkin(Blocks.WATER.getDefaultState(), "all=blocks/water_still");
        reg.addSkin(Blocks.LAVA.getDefaultState(), "all=blocks/lava_still");
        reg.addSkin(Blocks.PORTAL.getDefaultState(), "all=blocks/portal");
        reg.addSkin(Blocks.GOLD_BLOCK.getDefaultState(), "all=blocks/gold_block");
        reg.addSkin(Blocks.IRON_BLOCK.getDefaultState(), "all=blocks/iron_block");
        reg.addSkin(Blocks.LAPIS_BLOCK.getDefaultState(), "all=blocks/lapis_block");
        reg.addSkin(Blocks.DIAMOND_BLOCK.getDefaultState(), "all=blocks/diamond_block");
        reg.addSkin(Blocks.REDSTONE_BLOCK.getDefaultState(), "all=blocks/redstone_block");
        reg.addSkin(Blocks.EMERALD_BLOCK.getDefaultState(), "all=blocks/emerald_block");
        reg.addSkin(Blocks.QUARTZ_BLOCK.getDefaultState(), "all=blocks/quartz_block_lines_top");
        reg.addSkin(Blocks.COAL_BLOCK.getDefaultState(), "all=blocks/coal_block");
        reg.addSkin(Blocks.BONE_BLOCK.getDefaultState(), "up&down=blocks/bone_block_top,all=blocks/bone_block_side");
        reg.addSkin(Blocks.HAY_BLOCK.getDefaultState(), "up&down=blocks/hay_block_top,all=blocks/hay_block_side");
        reg.addSkin(Blocks.BOOKSHELF.getDefaultState(), "up&down=blocks/planks_oak,all=blocks/bookshelf");

        for(EnumDyeColor dye : EnumDyeColor.values())
        {
            reg.addSkin(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, dye), "all=blocks/hardened_clay_stained_" + dye.getName());
            reg.addSkin(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, dye), "all=blocks/wool_colored_" + dye.getName());
        }

        reg.addTier(YabbaCommon.TIER_IRON);
        reg.addTier(YabbaCommon.TIER_GOLD);
        reg.addTier(YabbaCommon.TIER_DMD);

        reg.addModel(new ModelCrate());
        reg.addModel(new ModelSolid());
        reg.addModel(new ModelSolidBorders());
        reg.addModel(new ModelPanel("cover", 0.125F));
        reg.addModel(new ModelPanel("panel", 0.25F));
        reg.addModel(new ModelPanel("slab", 0.5F));
    }

    @SubscribeEvent
    public void createConfigEvent(YabbaCreateConfigEvent event)
    {
        IBarrelModifiable barrel = event.getBarrel();

        if(barrel.getFlag(IBarrel.FLAG_REDSTONE_OUT))
        {
            event.getConfig().add(new SimpleConfigKey("redstone.mode"), new PropertyEnum<EnumRedstoneCompMode>(EnumRedstoneCompMode.NAME_MAP, EnumRedstoneCompMode.EQUAL)
            {
                @Nullable
                @Override
                public EnumRedstoneCompMode get()
                {
                    return EnumRedstoneCompMode.getMode(barrel.getUpgradeNBT().getByte("RedstoneMode"));
                }

                @Override
                public void set(@Nullable EnumRedstoneCompMode e)
                {
                    barrel.setUpgradeData("RedstoneMode", new NBTTagByte((byte) e.ordinal()));
                }
            });

            event.getConfig().add(new SimpleConfigKey("redstone.item_count"), new PropertyInt(0, 0, Integer.MAX_VALUE)
            {
                @Override
                public int getInt()
                {
                    return barrel.getUpgradeNBT().getInteger("RedstoneItemCount");
                }

                @Override
                public void setInt(int v)
                {
                    barrel.setUpgradeData("RedstoneItemCount", new NBTTagInt(v));
                }
            });
        }

        if(barrel.getFlag(IBarrel.FLAG_HOPPER))
        {
            event.getConfig().add(new SimpleConfigKey("hopper.up"), new PropertyBool(true)
            {
                @Override
                public boolean getBoolean()
                {
                    return barrel.getUpgradeNBT().getBoolean("HopperUp");
                }

                @Override
                public void setBoolean(boolean v)
                {
                    barrel.setUpgradeData("HopperUp", new NBTTagByte((byte) (v ? 1 : 0)));
                }
            });

            event.getConfig().add(new SimpleConfigKey("hopper.down"), new PropertyBool(true)
            {
                @Override
                public boolean getBoolean()
                {
                    return barrel.getUpgradeNBT().getBoolean("HopperDown");
                }

                @Override
                public void setBoolean(boolean v)
                {
                    barrel.setUpgradeData("HopperDown", new NBTTagByte((byte) (v ? 1 : 0)));
                }
            });

            event.getConfig().add(new SimpleConfigKey("hopper.collect"), new PropertyBool(false)
            {
                @Override
                public boolean getBoolean()
                {
                    return barrel.getUpgradeNBT().getBoolean("HopperCollect");
                }

                @Override
                public void setBoolean(boolean v)
                {
                    barrel.setUpgradeData("HopperCollect", new NBTTagByte((byte) (v ? 1 : 0)));
                }
            });
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

        Long l = BlockBarrel.LAST_CLICK_MAP.get(event.getEntityPlayer().getGameProfile().getId());
        long time = event.getWorld().getTotalWorldTime();

        if(l != null && (time - l) < 3)
        {
            return;
        }

        BlockBarrel.LAST_CLICK_MAP.put(event.getEntityPlayer().getGameProfile().getId(), time);

        TileEntity tile = world.getTileEntity(event.getPos());

        if(tile == null || !tile.hasCapability(YabbaCommon.BARREL_CAPABILITY, null) || BlockBarrel.normalizeFacing(world.getBlockState(event.getPos())) != event.getFace())
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