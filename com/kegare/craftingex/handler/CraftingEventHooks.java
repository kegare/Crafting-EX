/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package com.kegare.craftingex.handler;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;

import com.kegare.craftingex.core.CraftingEX;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CraftingEventHooks
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event)
	{
		if (event.gui != null && event.gui.getClass() == GuiCrafting.class)
		{
			EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
			World world = player.worldObj;
			int x = Math.round((float)player.posX);
			int y = Math.round((float)player.posY);
			int z = Math.round((float)player.posZ);

			player.openGui(CraftingEX.instance, 0, world, x, y, z);

			event.setCanceled(true);
		}
	}
}