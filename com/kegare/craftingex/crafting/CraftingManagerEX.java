/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package com.kegare.craftingex.crafting;

import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import com.google.common.collect.Lists;

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

		for (int j = 0; j < crafting.getSizeInventory(); ++j)
		{
			ItemStack itemstack = crafting.getStackInSlot(j);

			if (itemstack == null)
			{
				continue;
			}

			if (i == 0)
			{
				itemstack1 = itemstack;
			}
			else if (i == 1)
			{
				itemstack2 = itemstack;
			}

			++i;
		}

		if (i == 2 && itemstack1.getItem() == itemstack2.getItem() && itemstack1.stackSize == 1 && itemstack2.stackSize == 1 && itemstack1.getItem().isDamageable())
		{
			Item item = itemstack1.getItem();
			int var1 = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
			int var2 = item.getMaxDamage() - itemstack2.getItemDamageForDisplay();
			int var3 = var1 + var2 + item.getMaxDamage() * 10 / 100;
			int var4 = item.getMaxDamage() - var3;

			if (var4 < 0)
			{
				var4 = 0;
			}

			result.add(new ItemStack(item, 1, var4));

			return result;
		}

		for (Object obj : CraftingManager.getInstance().getRecipeList())
		{
			if (obj instanceof IRecipe)
			{
				if (((IRecipe)obj).matches(crafting, world))
				{
					result.add(((IRecipe)obj).getCraftingResult(crafting));
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