/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package craftingex.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import craftingex.core.CraftingEX;

public class CraftingEventHooks
{
	public static final CraftingEventHooks instance = new CraftingEventHooks();

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP)event.entityPlayer;
			WorldServer world = player.getServerForPlayer();
			BlockPos pos = event.pos;

			if (world.getBlockState(pos).getBlock() == Blocks.crafting_table && (!player.isSneaking() || player.getHeldItem() == null || player.getHeldItem().getItem().doesSneakBypassUse(world, pos, player)))
			{
				ItemStack current = player.getCurrentEquippedItem();

				if (current == null || current.getItem() != Item.getItemFromBlock(Blocks.crafting_table))
				{
					player.openGui(CraftingEX.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());

					event.setCanceled(true);
				}
			}
		}
	}
}