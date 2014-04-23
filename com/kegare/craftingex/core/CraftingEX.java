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

import net.minecraftforge.common.MinecraftForge;

import com.kegare.craftingex.handler.CraftingEventHooks;
import com.kegare.craftingex.handler.CraftingGuiHandler;
import com.kegare.craftingex.packet.NextRecipePacket;
import com.kegare.craftingex.packet.PacketPipeline;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "kegare.craftingex")
public class CraftingEX
{
	public static final PacketPipeline packetPipeline = new PacketPipeline();

	@Instance("kegare.craftingex")
	public static CraftingEX instance;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new CraftingGuiHandler());

		MinecraftForge.EVENT_BUS.register(new CraftingEventHooks());

		packetPipeline.init("kegare.craftingex");
		packetPipeline.registerPacket(NextRecipePacket.class);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		packetPipeline.postInit();
	}
}