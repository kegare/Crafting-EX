package kegare.craftingex.handler;

import kegare.craftingex.inventory.ContainerCraftingEX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class CraftingPacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if ("crafting.ex".equals(packet.channel))
		{
			EntityPlayer thePlayer = (EntityPlayer)player;

			if (thePlayer.openContainer instanceof ContainerCraftingEX)
			{
				ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
				ContainerCraftingEX container = (ContainerCraftingEX)thePlayer.openContainer;

				container.actionPerformed(dat.readInt());
			}
		}
	}

	public static Packet250CustomPayload getPacketNextRecipe(int id)
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt(id);

		return new Packet250CustomPayload("crafting.ex", dat.toByteArray());
	}
}
