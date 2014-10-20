/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package com.kegare.craftingex.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.kegare.craftingex.core.CraftingEX;
import com.kegare.craftingex.network.NextRecipeMessage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCraftingEX extends GuiContainer
{
	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");

	private final ContainerCraftingEX container;

	private GuiButton prevButton;
	private GuiButton nextButton;

	public GuiCraftingEX(InventoryPlayer inventory, World world, int x, int y, int z)
	{
		super(new ContainerCraftingEX(inventory, world, x, y, z));
		this.container = (ContainerCraftingEX)inventorySlots;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		prevButton = new GuiButton(0, guiLeft + 105, guiTop + 60, 20, 20, "<");
		nextButton = new GuiButton(1, guiLeft + 135, guiTop + 60, 20, 20, ">");
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if (container.isRecipes())
		{
			if (!buttonList.contains(prevButton))
			{
				buttonList.add(prevButton);
			}

			if (!buttonList.contains(nextButton))
			{
				buttonList.add(nextButton);
			}
		}
		else
		{
			if (buttonList.contains(prevButton))
			{
				buttonList.remove(prevButton);
			}

			if (buttonList.contains(nextButton))
			{
				buttonList.remove(nextButton);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRendererObj.drawString(I18n.format("container.crafting") + " EX", 28, 6, 0x404040);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 94, 0x404040);

		if (container.recipeSize > 1)
		{
			String str = container.currentIndex + 1 + " / " + container.recipeSize;
			fontRendererObj.drawString(str, xSize - fontRendererObj.getStringWidth(str) - 10, 6, 0x707070);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (!button.enabled)
		{
			return;
		}

		int next;

		switch (button.id)
		{
			case 0:
				next = 1;
				break;
			case 1:
				next = -1;
				break;
			default:
				return;
		}

		CraftingEX.network.sendToServer(new NextRecipeMessage(next));

		container.nextRecipe(next);
	}
}