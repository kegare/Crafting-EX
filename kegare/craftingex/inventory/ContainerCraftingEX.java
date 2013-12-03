package kegare.craftingex.inventory;

import java.util.List;

import kegare.craftingex.crafting.CraftingManagerEX;
import kegare.craftingex.handler.CraftingPacketHandler;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerCraftingEX extends ContainerWorkbench
{
	private final World world;
	private final EntityPlayer player;

	private List<ItemStack> recipes;
	private int recipeSize;
	private int currentIndex;

	public ContainerCraftingEX(InventoryPlayer inventory, World world, int x, int y, int z)
	{
		super(inventory, world, x, y, z);
		this.world = world;
		this.player = inventory.player;
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		List<ItemStack> oldRecipes = recipes;
		recipes = CraftingManagerEX.getInstance().findMatchingRecipe(craftMatrix, world);

		if (recipes != null)
		{
			if (oldRecipes != null)
			{
				ItemStack checkstack = oldRecipes.get(currentIndex);

				if (checkstack != null)
				{
					for (ItemStack itemstack : recipes)
					{
						if (itemstack != null && checkstack.isItemEqual(itemstack))
						{
							craftResult.setInventorySlotContents(0, recipes.get(currentIndex));

							return;
						}
					}
				}
			}

			recipeSize = recipes.size();
			currentIndex = 0;
			craftResult.setInventorySlotContents(0, recipes.get(0));
		}
		else
		{
			recipeSize = -1;
			currentIndex = -1;
			craftResult.setInventorySlotContents(0, null);
		}
	}

	public void actionPerformed(int id)
	{
		if (world.isRemote)
		{
			((EntityClientPlayerMP)player).sendQueue.addToSendQueue(CraftingPacketHandler.getPacketNextRecipe(id));
		}

		if (id == 0)
		{
			nextRecipe(1);
		}
		else if (id == 1)
		{
			nextRecipe(-1);
		}
	}

	public void nextRecipe(int value)
	{
		if (currentIndex < 0)
		{
			return;
		}

		currentIndex = (currentIndex + value + recipeSize) % recipeSize;
		craftResult.setInventorySlotContents(0, recipes.get(currentIndex));
	}

	public boolean isRecipes()
	{
		return recipeSize > 1;
	}
}