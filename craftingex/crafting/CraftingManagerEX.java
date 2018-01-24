package craftingex.crafting;

import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class CraftingManagerEX
{
	@Nullable
	public static NonNullList<ItemStack> findMatchingRecipe(InventoryCrafting crafting, World world)
	{
		NonNullList<ItemStack> result = NonNullList.create();

		int i = 0;
		ItemStack itemstack1 = ItemStack.EMPTY;
		ItemStack itemstack2 = ItemStack.EMPTY;

		outside: for (int j = 0; j < crafting.getSizeInventory(); ++j)
		{
			ItemStack itemstack = crafting.getStackInSlot(j);

			switch (i)
			{
				case 0:
					itemstack1 = itemstack;
					break;
				case 1:
					itemstack2 = itemstack;
					break;
				default:
					break outside;
			}

			++i;
		}

		if (i == 2 && itemstack1.getItem() == itemstack2.getItem() && itemstack1.getCount() == 1 && itemstack2.getCount() == 1 && itemstack1.getItem().isRepairable())
		{
			Item item = itemstack1.getItem();
			int maxDamage = itemstack1.getMaxDamage();
			int var1 = maxDamage - itemstack1.getItemDamage();
			int var2 = maxDamage - itemstack2.getItemDamage();
			int var3 = var1 + var2 + maxDamage * 5 / 100;
			int var4 = maxDamage - var3;

			if (var4 < 0)
			{
				var4 = 0;
			}

			result.add(new ItemStack(item, 1, var4));

			return result;
		}

		search: for (IRecipe recipe : CraftingManager.REGISTRY)
		{
			if (recipe.matches(crafting, world))
			{
				itemstack1 = recipe.getCraftingResult(crafting);

				if (!result.contains(itemstack1))
				{
					for (ItemStack item : result)
					{
						if (ItemStack.areItemStacksEqual(item, itemstack1))
						{
							continue search;
						}
					}

					result.add(itemstack1);
				}
			}
		}

		if (result.isEmpty())
		{
			return null;
		}

		return result;
	}
}