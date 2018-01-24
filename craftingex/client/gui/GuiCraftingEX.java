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
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCraftingEX extends GuiContainer implements IRecipeShownListener
{
	private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/crafting_table.png");
	private static final ItemStack CRAFTING_TABLE = new ItemStack(Blocks.CRAFTING_TABLE);

	private final ContainerCraftingEX container;
	private final BlockPos pos;
	private final GuiRecipeBook recipeBookGui;

	private GuiButtonImage recipeButton;
	private GuiButton prevButton, nextButton;
	private HoverChecker prevHover, nextHover;

	private boolean widthTooNarrow;
	private int recipesX, recipesY;

	private List<String> tooltips = Lists.newArrayList();

	public GuiCraftingEX(InventoryPlayer inventory, World world, BlockPos pos)
	{
		super(new ContainerCraftingEX(inventory, world, pos));
		this.container = (ContainerCraftingEX)inventorySlots;
		this.pos = pos;
		this.recipeBookGui = new GuiRecipeBook();
	}

	@Override
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);

		super.initGui();

		widthTooNarrow = width < 379;

		recipeBookGui.func_194303_a(width, height, mc, widthTooNarrow, container.craftMatrix);

		guiLeft = recipeBookGui.updateScreenPosition(widthTooNarrow, width, xSize);

		if (recipeButton == null)
		{
			recipeButton = new GuiButtonImage(10, 0, 0, 20, 18, 0, 168, 19, CRAFTING_TABLE_GUI_TEXTURE);
		}

		recipeButton.setPosition(guiLeft + 5, height / 2 - 49);

		if (prevButton == null)
		{
			prevButton = new GuiButton(0, 0, 0, 20, 20, "<");
			prevButton.visible = false;
		}

		prevButton.x = guiLeft + 107;
		prevButton.y = guiTop + 60;

		if (nextButton == null)
		{
			nextButton = new GuiButton(1, 0, 0, prevButton.width, prevButton.height, ">");
			nextButton.visible = false;
		}

		nextButton.x = prevButton.x + prevButton.width + 10;
		nextButton.y = prevButton.y;

		buttonList.add(recipeButton);
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

		recipeBookGui.tick();

		prevButton.visible = nextButton.visible = container.isRecipes();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();

		if (recipeBookGui.isVisible() && widthTooNarrow)
		{
			drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

			recipeBookGui.render(mouseX, mouseY, partialTicks);
		}
		else
		{
			recipeBookGui.render(mouseX, mouseY, partialTicks);

			super.drawScreen(mouseX, mouseY, partialTicks);

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

				drawHoveringText(stack.getDisplayName(), mouseX, mouseY);
			}

			if (container.isRecipes() && mouseX >= guiLeft + recipesX - 5 && mouseX <= guiLeft + xSize - 20 && mouseY >= guiTop + recipesY - 4 && mouseY <= guiTop + recipesY + 10)
			{
				tooltips.clear();

				for (ItemStack stack : container.getRecipes())
				{
					tooltips.add(stack.getDisplayName());
				}

				drawHoveringText(tooltips, mouseX, mouseY);
			}

			recipeBookGui.renderGhostRecipe(guiLeft, guiTop, true, partialTicks);
		}

		renderHoveredToolTip(mouseX, mouseY);

		recipeBookGui.renderTooltip(guiLeft, guiTop, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRenderer.drawString(I18n.format("container.crafting") + " EX", 28, 6, 0x404040);
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 94, 0x404040);

		if (container.isRecipes())
		{
			String str = container.getCurrentIndex() + 1 + " / " + container.getRecipeSize();
			recipesX = xSize - fontRenderer.getStringWidth(str) - 25;
			recipesY = 6;
			fontRenderer.drawString(str, recipesX, recipesY, 0x707070);
		}

		if (mouseX >= guiLeft && mouseX <= guiLeft + 20 && mouseY >= guiTop && mouseY <= guiTop + 20)
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.85F, 0.85F, 1.0F);
			RenderHelper.enableGUIStandardItemLighting();
			itemRender.zLevel = 100.0F;
			itemRender.renderItemAndEffectIntoGUI(CRAFTING_TABLE, 6, 6);
			itemRender.zLevel = 0.0F;
			RenderHelper.disableStandardItemLighting();
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float ticks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
	{
		return (!widthTooNarrow || !recipeBookGui.isVisible()) && super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (!button.enabled)
		{
			return;
		}

		if (button.id == 10)
		{
			recipeBookGui.initVisuals(widthTooNarrow, container.craftMatrix);
			recipeBookGui.toggleVisibility();

			guiLeft = recipeBookGui.updateScreenPosition(widthTooNarrow, width, xSize);

			recipeButton.setPosition(guiLeft + 5, height / 2 - 49);

			prevButton.x = guiLeft + 107;
			nextButton.x = prevButton.x + prevButton.width + 10;

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

		CraftingEX.NETWORK.sendToServer(new NextRecipeMessage(next));

		container.nextRecipe(next);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();

		int mouseX = Mouse.getEventX() * width / mc.displayWidth;
		int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
		Slot slot = container.getSlot(0);

		if (isPointInRegion(slot.xPos - 5, slot.yPos - 5, 21, 21, mouseX, mouseY))
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
		if (!recipeBookGui.mouseClicked(mouseX, mouseY, code))
		{
			if (!widthTooNarrow || !recipeBookGui.isVisible())
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

					CraftingEX.NETWORK.sendToServer(new OpenCraftingMessage(pos, items));
				}
				else if (code == 0 && mouseX >= guiLeft + recipesX - 5 && mouseX <= guiLeft + xSize - 20 && mouseY >= guiTop + recipesY - 4 && mouseY <= guiTop + recipesY + 10 && container.isRecipes())
				{
					mc.displayGuiScreen(new GuiCraftingResult(this, container.getRecipes()));
				}
			}
		}
	}

	@Override
	protected boolean hasClickedOutside(int par1, int par2, int par3, int par4)
	{
		boolean flag = par1 < par3 || par2 < par4 || par1 >= par3 + xSize || par2 >= par4 + ySize;

		return recipeBookGui.hasClickedOutside(par1, par2, guiLeft, guiTop, xSize, ySize) && flag;
	}

	@Override
	protected void keyTyped(char typedChar, int code) throws IOException
	{
		if (!recipeBookGui.keyPressed(typedChar, code))
		{
			super.keyTyped(typedChar, code);

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
	}

	@Override
	protected void handleMouseClick(Slot slot, int slotId, int mouseButton, ClickType type)
	{
		super.handleMouseClick(slot, slotId, mouseButton, type);

		recipeBookGui.slotClicked(slot);
	}

	@Override
	public void onGuiClosed()
	{
		recipeBookGui.removed();

		super.onGuiClosed();

		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void recipesUpdated()
	{
		recipeBookGui.recipesUpdated();
	}

	@Override
	public GuiRecipeBook func_194310_f()
	{
		return recipeBookGui;
	}

	protected void resultSelected(ItemStack stack)
	{
		int prev = container.getCurrentIndex();

		container.resetCurrentIndex();

		for (int i = 0; i < container.getRecipeSize(); ++i)
		{
			if (ItemStack.areItemStacksEqual(stack, container.getNextRecipe(i)))
			{
				CraftingEX.NETWORK.sendToServer(new NextRecipeMessage(i));

				container.nextRecipe(i);

				return;
			}
		}

		container.nextRecipe(prev);
	}
}