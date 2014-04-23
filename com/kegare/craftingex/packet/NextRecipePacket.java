/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package com.kegare.craftingex.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import com.kegare.craftingex.inventory.ContainerCraftingEX;

public class NextRecipePacket extends AbstractPacket
{
	private int next;

	public NextRecipePacket() {}

	public NextRecipePacket(int next)
	{
		this.next = next;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(next);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		next = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		if (player.openContainer instanceof ContainerCraftingEX)
		{
			((ContainerCraftingEX)player.openContainer).actionPerformed(next);
		}
	}
}