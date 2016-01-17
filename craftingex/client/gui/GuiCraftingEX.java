/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package craftingex.client.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import craftingex.core.CraftingEX;
import craftingex.inventory.ContainerCraftingEX;
import craftingex.network.NextRecipeMessage;
import craftingex.network.OpenCraftingMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCraftingEX extends GuiContainer
{
	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
	private static final ItemStack craftingIconItem = new ItemStack(Blocks.crafting_table);

	private final ContainerCraftingEX container;
	private final BlockPos pos;

	private GuiButton prevButton, nextButton;
	private HoverChecker prevHover, nextHover;

	private int recipesX, recipesY;
	private List<String> tooltips = Lists.newArrayList();

	public GuiCraftingEX(InventoryPlayer inventory, World world, BlockPos pos)
	{
		super(new ContainerCraftingEX(inventory, world, pos));
		this.container = (ContainerCraftingEX)inventorySlots;
		this.pos = pos;
	}

	@Override
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);

		super.initGui();

		if (prevButton == null)
		{
			prevButton = new GuiButton(0, 0, 0, 20, 20, "<");
			prevButton.visible = false;
		}

		prevButton.xPosition = guiLeft + 107;
		prevButton.yPosition = guiTop + 60;

		if (nextButton == null)
		{
			nextButton = new GuiButton(1, 0, 0, prevButton.width, prevButton.height, ">");
			nextButton.visible = false;
		}

		nextButton.xPosition = prevButton.xPosition + prevButton.width + 10;
		nextButton.yPosition = prevButton.yPosition;

		buttonList.add(prevButton);
		buttonList.add(nextButton);

		if (prevHover == null)
		{
			prevHover = new HoverChecker(prevButton, 0);
		}

		if (nextHover == null)
		{
			nextHover = new HoverChecker(nextButton, 0);
		}
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		prevButton.visible = nextButton.visible = container.isRecipes();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float ticks)
	{
		super.drawScreen(mouseX, mouseY, ticks);

		int i = 0;

		if (prevHover.checkHover(mouseX, mouseY))
		{
			i = -1;
		}
		else if (nextHover.checkHover(mouseX, mouseY))
		{
			i = 1;
		}

		if (container.isRecipes() && i != 0)
		{
			ItemStack stack = container.getNextRecipe(i);

			if (stack != null)
			{
				drawCreativeTabHoveringText(stack.getDisplayName(), mouseX, mouseY);
			}
		}

		if (container.isRecipes() && mouseX >= guiLeft + recipesX - 5 && mouseX <= guiLeft + xSize - 5 && mouseY >= guiTop + recipesY - 4 && mouseY <= guiTop + recipesY + 10)
		{
			tooltips.clear();

			for (ItemStack stack : container.getRecipes())
			{
				tooltips.add(stack.getDisplayName());
			}

			drawHoveringText(tooltips, mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRendererObj.drawString(I18n.format("container.crafting") + " EX", 28, 6, 0x404040);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 94, 0x404040);

		if (container.isRecipes())
		{
			String str = container.getCurrentIndex() + 1 + " / " + container.getRecipeSize();
			recipesX = xSize - fontRendererObj.getStringWidth(str) - 10;
			recipesY = 6;
			fontRendererObj.drawString(str, recipesX, recipesY, 0x707070);
		}

		if (mouseX >= guiLeft && mouseX <= guiLeft + 20 && mouseY >= guiTop && mouseY <= guiTop + 20)
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.82F, 0.82F, 1.0F);
			RenderHelper.enableGUIStandardItemLighting();
			itemRender.zLevel = 100.0F;
			itemRender.renderItemAndEffectIntoGUI(craftingIconItem, 6, 6);
			itemRender.zLevel = 0.0F;
			RenderHelper.disableStandardItemLighting();
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float ticks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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
				next = -1;
				break;
			case 1:
				next = 1;
				break;
			default:
				return;
		}

		CraftingEX.network.sendToServer(new NextRecipeMessage(next));

		container.nextRecipe(next);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();

		int mouseX = Mouse.getEventX() * width / mc.displayWidth;
		int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
		Slot slot = container.getSlot(0);

		if (isPointInRegion(slot.xDisplayPosition - 5, slot.yDisplayPosition - 5, 21, 21, mouseX, mouseY))
		{
			int wheel = Mouse.getDWheel();

			if (wheel < 0)
			{
				actionPerformed(nextButton);
			}
			else if (wheel > 0)
			{
				actionPerformed(prevButton);
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int code) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, code);

		if (code == 0 && mouseX >= guiLeft && mouseX <= guiLeft + 20 && mouseY >= guiTop && mouseY <= guiTop + 20)
		{
			nextButton.playPressSound(mc.getSoundHandler());

			ItemStack[] items = new ItemStack[container.craftMatrix.getSizeInventory()];

			for (int i = 0; i < items.length; ++i)
			{
				items[i] = container.craftMatrix.removeStackFromSlot(i);
			}

			CraftingEX.network.sendToServer(new OpenCraftingMessage(pos, items));
		}
		else if (code == 0 && mouseX <= guiLeft + xSize && mouseX >=guiLeft + xSize - 50 && mouseY >= guiTop && mouseY <= guiTop + 20 && container.isRecipes())
		{
			mc.displayGuiScreen(new GuiCraftingResult(this, container.getRecipes()));
		}
	}

	@Override
	protected void keyTyped(char c, int code) throws IOException
	{
		super.keyTyped(c, code);

		switch (code)
		{
			case Keyboard.KEY_RIGHT:
				nextButton.playPressSound(mc.getSoundHandler());
				actionPerformed(nextButton);
				break;
			case Keyboard.KEY_LEFT:
				prevButton.playPressSound(mc.getSoundHandler());
				actionPerformed(prevButton);
				break;
		}
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();

		Keyboard.enableRepeatEvents(false);
	}

	protected void resultSelected(ItemStack stack)
	{
		int prev = container.getCurrentIndex();

		container.resetCurrentIndex();

		for (int i = 0; i < container.getRecipeSize(); ++i)
		{
			if (ItemStack.areItemStacksEqual(stack, container.getNextRecipe(i)))
			{
				CraftingEX.network.sendToServer(new NextRecipeMessage(i));

				container.nextRecipe(i);

				return;
			}
		}

		container.nextRecipe(prev);
	}
}