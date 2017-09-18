package com.latmod.yabba.integration;

import com.raoulvdberge.refinedstorage.api.IRSAPI;
import com.raoulvdberge.refinedstorage.api.RSAPIInject;

/**
 * @author LatvianModder
 */
public class RefinedStorageIntegration
{
	@RSAPIInject
	public static IRSAPI API;
}