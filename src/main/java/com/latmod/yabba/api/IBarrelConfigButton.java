package com.latmod.yabba.api;

import net.minecraft.nbt.NBTBase;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 01.01.2017.
 */
public interface IBarrelConfigButton
{
    int getWidth();

    int getHeight();

    void onClicked(int mouseX, int mouseY);

    void drawButton(int mouseX, int mouseY);

    void addMouseOverText(List<String> list, int mouseX, int mouseY);

    @Nullable
    NBTBase getNBT();
}