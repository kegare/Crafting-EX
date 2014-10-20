/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package com.kegare.craftingex.plugin.nei;

import codechicken.nei.api.API;

import com.kegare.craftingex.inventory.GuiCraftingEX;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Method;

public class NEIPlugin
{
	public static final String MODID = "NotEnoughItems";

	public static boolean enabled()
	{
		return Loader.isModLoaded(MODID);
	}

	@Method(modid = MODID)
	public static void invoke()
	{
		API.registerGuiOverlay(GuiCraftingEX.class, "crafting");
	}
}