package craftingex.network;

import craftingex.inventory.ContainerCraftingEX;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class NextRecipeMessage implements IPlayerMessage<NextRecipeMessage, IMessage>
{
	private int next;

	public NextRecipeMessage() {}

	public NextRecipeMessage(int next)
	{
		this.next = next;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		next = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(next);
	}

	@Override
	public IMessage process(EntityPlayerMP player)
	{
		if (player.openContainer != null && player.openContainer instanceof ContainerCraftingEX)
		{
			((ContainerCraftingEX)player.openContainer).nextRecipe(next);
		}

		return null;
	}
}