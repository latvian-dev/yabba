package com.latmod.yabba.client.gui;

import com.latmod.yabba.api.IBarrelConfigButton;
import net.minecraft.nbt.NBTBase;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 01.01.2017.
 */
public class ConfigButtonBoolean implements IBarrelConfigButton
{
    @Override
    public int getWidth()
    {
        return 32;
    }

    @Override
    public int getHeight()
    {
        return 16;
    }

    @Override
    public void onClicked(int mouseX, int mouseY)
    {
    }

    @Override
    public void drawButton(int mouseX, int mouseY)
    {
    }

    @Override
    public void addMouseOverText(List<String> list, int mouseX, int mouseY)
    {
        list.add((mouseX >= 8) ? "True" : "False");
    }

    @Nullable
    @Override
    public NBTBase getNBT()
    {
        return null;
    }
}