package com.latmod.yabba.tile;

import com.latmod.yabba.api.BarrelContentType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BarrelNetwork implements ICapabilityProvider
{
	@CapabilityInject(BarrelNetwork.class)
	public static Capability<BarrelNetwork> CAP;

	public static BarrelNetwork get(World world)
	{
		return world.getCapability(CAP, null);
	}

	public final World world;
	public final List<IBarrelConnector> connectors = new ArrayList<>();
	private boolean refresh = true;

	public BarrelNetwork(World w)
	{
		world = w;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CAP;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == CAP ? (T) this : null;
	}

	public void refresh()
	{
		refresh = true;
	}

	public void barrelUpdated(BarrelContentType type)
	{
		for (IBarrelConnector connector : connectors)
		{
			if (connector.getContentType() == type)
			{
				connector.updateBarrels();
			}
		}
	}

	public void tick()
	{
		if (refresh)
		{
			connectors.clear();

			for (TileEntity tileEntity : world.loadedTileEntityList)
			{
				if (!tileEntity.isInvalid() && tileEntity instanceof IBarrelConnector)
				{
					connectors.add((IBarrelConnector) tileEntity);
				}
			}

			for (IBarrelConnector connector : connectors)
			{
				connector.updateBarrels();
			}

			refresh = false;
		}
	}
}
