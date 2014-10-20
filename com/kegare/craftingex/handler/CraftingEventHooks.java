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

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import com.kegare.craftingex.core.CraftingEX;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CraftingEventHooks
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void doOpenCraftingEX(PlayerInteractEvent event)
	{
		if (event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP)event.entityPlayer;
			WorldServer world = player.getServerForPlayer();
			int x = event.x;
			int y = event.y;
			int z = event.z;

			if (world.getBlock(x, y, z) == Blocks.crafting_table && (!player.isSneaking() || player.getHeldItem() == null || player.getHeldItem().getItem().doesSneakBypassUse(world, x, y, z, player)))
			{
				ItemStack current = player.getCurrentEquippedItem();

				if (current == null || current.getItem() != Item.getItemFromBlock(Blocks.crafting_table))
				{
					player.openGui(CraftingEX.instance, 0, world, x, y, z);

					event.setCanceled(true);
				}
			}
		}
	}
}