/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package com.kegare.craftingex.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

import com.kegare.craftingex.inventory.ContainerCraftingEX;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class NextRecipeMessage implements IMessage, IMessageHandler<NextRecipeMessage, IMessage>
{
	private int next;

	public NextRecipeMessage() {}

	public NextRecipeMessage(int next)
	{
		this.next = next;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		next = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(next);
	}

	@Override
	public IMessage onMessage(NextRecipeMessage message, MessageContext ctx)
	{
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;

		if (player.openContainer != null && player.openContainer instanceof ContainerCraftingEX)
		{
			((ContainerCraftingEX)player.openContainer).nextRecipe(message.next);
		}

		return null;
	}
}