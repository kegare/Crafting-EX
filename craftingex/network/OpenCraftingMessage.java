package craftingex.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class OpenCraftingMessage implements IPlayerMessage<OpenCraftingMessage, IMessage>
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
	public void fromBytes(ByteBuf buf)
	{
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		items = new ItemStack[buf.readInt()];

		for (int i = 0; i < items.length; ++i)
		{
			items[i] = ByteBufUtils.readItemStack(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(items.length);

		for (ItemStack item : items)
		{
			ByteBufUtils.writeItemStack(buf, item);
		}
	}

	@Override
	public IMessage process(EntityPlayerMP player)
	{
		player.displayGui(new BlockWorkbench.InterfaceCraftingTable(player.world, new BlockPos(x, y, z)));

		if (player.openContainer != null && player.openContainer instanceof ContainerWorkbench)
		{
			for (int i = 0; i < items.length; ++i)
			{
				player.openContainer.getSlot(i + 1).putStack(items[i]);
			}
		}

		return null;
	}
}