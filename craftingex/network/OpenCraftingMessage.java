/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package craftingex.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenCraftingMessage implements IMessage, IMessageHandler<OpenCraftingMessage, IMessage>
{
	private int x, y, z;
	private ItemStack[] items;

	public OpenCraftingMessage() {}

	public OpenCraftingMessage(BlockPos pos, ItemStack[] items)
	{
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.items = items;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		items = new ItemStack[buffer.readInt()];

		for (int i = 0; i < items.length; ++i)
		{
			items[i] = ByteBufUtils.readItemStack(buffer);
		}
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(items.length);

		for (ItemStack item : items)
		{
			ByteBufUtils.writeItemStack(buffer, item);
		}
	}

	@Override
	public IMessage onMessage(OpenCraftingMessage message, MessageContext ctx)
	{
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;

		player.displayGui(new BlockWorkbench.InterfaceCraftingTable(player.worldObj, new BlockPos(message.x, message.y, message.z)));

		if (player.openContainer != null && player.openContainer instanceof ContainerWorkbench)
		{
			ItemStack[] items = message.items;

			for (int i = 0; i < items.length; ++i)
			{
				player.openContainer.getSlot(i + 1).putStack(items[i]);
			}
		}

		return null;
	}
}