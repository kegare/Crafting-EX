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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.kegare.craftingex.inventory.ContainerCraftingEX;
import com.kegare.craftingex.inventory.GuiCraftingEX;

import cpw.mods.fml.common.network.IGuiHandler;

public class CraftingGuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			return new ContainerCraftingEX(player.inventory, world, x, y, z);
		}
		else
		{
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			return new GuiCraftingEX(player.inventory, world, x, y, z);
		}
		else
		{
			return null;
		}
	}
}