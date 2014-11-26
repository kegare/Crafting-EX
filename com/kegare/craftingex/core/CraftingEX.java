/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package com.kegare.craftingex.core;

import static com.kegare.craftingex.core.CraftingEX.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.kegare.craftingex.handler.CraftingEventHooks;
import com.kegare.craftingex.handler.CraftingGuiHandler;
import com.kegare.craftingex.network.NextRecipeMessage;

@Mod(modid = MODID)
public class CraftingEX
{
	public static final String MODID = "kegare.craftingex";

	@Instance(MODID)
	public static CraftingEX instance;

	public static final SimpleNetworkWrapper network = new SimpleNetworkWrapper(MODID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		network.registerMessage(NextRecipeMessage.class, NextRecipeMessage.class, 0, Side.SERVER);

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new CraftingGuiHandler());

		MinecraftForge.EVENT_BUS.register(new CraftingEventHooks());
	}
}