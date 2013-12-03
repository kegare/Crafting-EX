package kegare.craftingex.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCraftingEX extends GuiContainer
{
	private final ContainerCraftingEX container;

	private GuiButton nextButton;
	private GuiButton prevButton;

	public GuiCraftingEX(InventoryPlayer inventory, World world, int x, int y, int z)
	{
		super(new ContainerCraftingEX(inventory, world, x, y, z));
		this.container = (ContainerCraftingEX)inventorySlots;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		int var1 = (width - xSize) / 2;
		int var2 = (height - ySize) / 2;
		prevButton = new GuiButton(0, var1 + 105, var2 + 60, 20 , 20 , "<");
		nextButton = new GuiButton(1, var1 + 135, var2 + 60, 20 , 20 , ">");
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if (container.isRecipes())
		{
			if (!buttonList.contains(nextButton))
			{
				buttonList.add(nextButton);
			}

			if (!buttonList.contains(prevButton))
			{
				buttonList.add(prevButton);
			}
		}
		else
		{
			if (buttonList.contains(nextButton))
			{
				buttonList.remove(nextButton);
			}

			if (buttonList.contains(prevButton))
			{
				buttonList.remove(prevButton);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRenderer.drawString(I18n.getString("container.crafting") + " EX", 28, 6, 0x333333);
		fontRenderer.drawString(I18n.getString("container.inventory"), 8, ySize - 96 + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/crafting_table.png"));
		int var1 = (width - xSize) / 2;
		int var2 = (height - ySize) / 2;
		drawTexturedModalRect(var1, var2, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (!button.enabled)
		{
			return;
		}

		container.actionPerformed(button.id);
	}
}