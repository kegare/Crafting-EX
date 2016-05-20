package craftingex.inventory;

import java.util.List;

import craftingex.crafting.CraftingManagerEX;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerCraftingEX extends ContainerWorkbench
{
	private final World world;

	private List<ItemStack> recipes;

	private int recipeSize, currentIndex;

	public ContainerCraftingEX(InventoryPlayer inventory, World world, BlockPos pos)
	{
		super(inventory, world, pos);
		this.world = world;
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		List<ItemStack> prev = recipes;
		recipes = CraftingManagerEX.getInstance().findMatchingRecipe(craftMatrix, world);

		if (recipes != null)
		{
			if (prev != null)
			{
				ItemStack item = prev.get(currentIndex);

				if (item != null)
				{
					for (ItemStack itemstack : recipes)
					{
						if (itemstack != null && item.isItemEqual(itemstack))
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

	public void nextRecipe(int value)
	{
		if (currentIndex < 0)
		{
			return;
		}

		currentIndex = (currentIndex + value + recipeSize) % recipeSize;
		craftResult.setInventorySlotContents(0, recipes.get(currentIndex));
	}

	public ItemStack getNextRecipe(int value)
	{
		if (currentIndex < 0)
		{
			return null;
		}

		return recipes.get((currentIndex + value + recipeSize) % recipeSize);
	}

	public boolean isRecipes()
	{
		return recipeSize > 1;
	}

	public List<ItemStack> getRecipes()
	{
		return recipes;
	}

	public int getRecipeSize()
	{
		return recipeSize;
	}

	public int getCurrentIndex()
	{
		return currentIndex;
	}

	public void resetCurrentIndex()
	{
		currentIndex = 0;
		craftResult.setInventorySlotContents(0, recipes.get(0));
	}
}