package craftingex.inventory;

import craftingex.crafting.CraftingManagerEX;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerCraftingEX extends ContainerWorkbench
{
	private final World world;

	private NonNullList<ItemStack> recipes;

	private int recipeSize, currentIndex;

	public ContainerCraftingEX(InventoryPlayer inventory, World world, BlockPos pos)
	{
		super(inventory, world, pos);
		this.world = world;
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		NonNullList<ItemStack> prev = recipes;
		recipes = CraftingManagerEX.findMatchingRecipe(craftMatrix, world);

		if (recipes != null)
		{
			if (prev != null)
			{
				ItemStack stack = prev.get(currentIndex);

				for (ItemStack output : recipes)
				{
					if (stack.isItemEqual(output))
					{
						craftResult.setInventorySlotContents(0, recipes.get(currentIndex));

						return;
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
			craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
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
			return ItemStack.EMPTY;
		}

		return recipes.get((currentIndex + value + recipeSize) % recipeSize);
	}

	public boolean isRecipes()
	{
		return recipeSize > 1;
	}

	public NonNullList<ItemStack> getRecipes()
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