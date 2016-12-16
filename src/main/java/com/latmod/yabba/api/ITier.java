package com.latmod.yabba.api;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 15.12.2016.
 */
public interface ITier
{
    byte getTierID();

    int getCapacity();

    @Nullable
    Object getNextTierUpgradeItem();

    @Nullable
    ITier getNextTier();
}