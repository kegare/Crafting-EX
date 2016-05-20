package craftingex.handler;

import craftingex.client.gui.GuiCraftingEX;
import craftingex.inventory.ContainerCraftingEX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CraftingGuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			return new ContainerCraftingEX(player.inventory, world, new BlockPos(x, y, z));
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			return new GuiCraftingEX(player.inventory, world, new BlockPos(x, y, z));
		}

		return null;
	}
}