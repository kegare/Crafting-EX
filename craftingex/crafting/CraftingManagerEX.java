package craftingex.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class CraftingManagerEX
{
	private static final CraftingManagerEX instance = new CraftingManagerEX();

	public static final CraftingManagerEX getInstance()
	{
		return instance;
	}

	public List<ItemStack> findMatchingRecipe(InventoryCrafting crafting, World world)
	{
		List<ItemStack> result = Lists.newArrayList();

		int i = 0;
		ItemStack itemstack1 = null;
		ItemStack itemstack2 = null;

		outside: for (int j = 0; j < crafting.getSizeInventory(); ++j)
		{
			ItemStack itemstack = crafting.getStackInSlot(j);

			if (itemstack != null)
			{
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
		}

		if (i == 2 && itemstack1.getItem() == itemstack2.getItem() && itemstack1.stackSize == 1 && itemstack2.stackSize == 1 && itemstack1.getItem().isRepairable())
		{
			Item item = itemstack1.getItem();
			int var1 = item.getMaxDamage() - itemstack1.getItemDamage();
			int var2 = item.getMaxDamage() - itemstack2.getItemDamage();
			int var3 = var1 + var2 + item.getMaxDamage() * 5 / 100;
			int var4 = item.getMaxDamage() - var3;

			if (var4 < 0)
			{
				var4 = 0;
			}

			result.add(new ItemStack(item, 1, var4));

			return result;
		}

		search: for (Object obj : CraftingManager.getInstance().getRecipeList())
		{
			if (obj instanceof IRecipe)
			{
				IRecipe recipe = (IRecipe)obj;

				if (recipe.matches(crafting, world))
				{
					itemstack1 = recipe.getCraftingResult(crafting);

					if (itemstack1 != null && !result.contains(itemstack1))
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
		}

		if (result.isEmpty())
		{
			return null;
		}

		return result;
	}
}