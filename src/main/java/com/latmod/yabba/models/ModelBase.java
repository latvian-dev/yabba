package com.latmod.yabba.models;

import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.latmod.yabba.api.IBarrelModel;

/**
 * @author LatvianModder
 */
public abstract class ModelBase extends FinalIDObject implements IBarrelModel
{
	public ModelBase(String id)
	{
		super(id);
	}
}