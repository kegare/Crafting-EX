package craftingex.network;

import craftingex.inventory.ContainerCraftingEX;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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