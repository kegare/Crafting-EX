package kegare.craftingex.handler;

import kegare.craftingex.inventory.ContainerCraftingEX;
import kegare.craftingex.inventory.GuiCraftingEX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CraftingGuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			return new ContainerCraftingEX(player.inventory, world, x, y, z);
		}
		else
		{
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			return new GuiCraftingEX(player.inventory, world, x, y, z);
		}
		else
		{
			return null;
		}
	}
}