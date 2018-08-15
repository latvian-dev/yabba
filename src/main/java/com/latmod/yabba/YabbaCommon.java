package com.latmod.yabba;

import com.latmod.yabba.net.YabbaNetHandler;
import com.latmod.yabba.util.AntibarrelData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * @author LatvianModder
 */
public class YabbaCommon
{
	public void preInit()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Yabba.MOD, new YabbaGuiHandler());
		YabbaConfig.sync();
		YabbaNetHandler.init();

		CapabilityManager.INSTANCE.register(AntibarrelData.class, new Capability.IStorage<AntibarrelData>()
		{
			@Override
			public NBTBase writeNBT(Capability<AntibarrelData> capability, AntibarrelData instance, EnumFacing side)
			{
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<AntibarrelData> capability, AntibarrelData instance, EnumFacing side, NBTBase nbt)
			{
				if (nbt instanceof NBTTagCompound)
				{
					instance.deserializeNBT((NBTTagCompound) nbt);
				}
			}
		}, () -> new AntibarrelData(null));
	}

	public void postInit()
	{
	}
}