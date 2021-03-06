package craftingex.client.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import craftingex.core.CraftingEX;
import craftingex.util.ArrayListExtended;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCraftingResult extends GuiScreen
{
	private final GuiCraftingEX parent;
	private final NonNullList<ItemStack> results;

	private ItemList itemList;
	private GuiButton doneButton;
	private GuiTextField filterTextField;

	public GuiCraftingResult(GuiCraftingEX parent, NonNullList<ItemStack> results)
	{
		this.parent = parent;
		this.results = results;
	}

	@Override
	public void initGui()
	{
		if (itemList == null)
		{
			itemList = new ItemList();
		}

		itemList.setDimensions(width, height, 32, height - 28);

		if (doneButton == null)
		{
			doneButton = new GuiButtonExt(0, 0, 0, 145, 20, I18n.format("gui.done"));
		}

		doneButton.x = width / 2 + 10;
		doneButton.y = height - doneButton.height - 4;

		buttonList.clear();
		buttonList.add(doneButton);

		if (filterTextField == null)
		{
			filterTextField = new GuiTextField(1, fontRenderer, 0, 0, 150, 16);
			filterTextField.setMaxStringLength(100);
		}

		filterTextField.x = width / 2 - filterTextField.width - 5;
		filterTextField.y = height - filterTextField.height - 6;
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button.enabled)
		{
			switch (button.id)
			{
				case 0:
					if (itemList.selected != null)
					{
						parent.resultSelected(itemList.selected);
					}

					mc.displayGuiScreen(parent);

					if (parent == null)
					{
						mc.setIngameFocus();
					}

					break;
				default:
					itemList.actionPerformed(button);
			}
		}
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		filterTextField.updateCursorCounter();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float ticks)
	{
		if (itemList == null || doneButton == null || filterTextField == null)
		{
			return;
		}

		itemList.drawScreen(mouseX, mouseY, ticks);

		drawCenteredString(fontRenderer, I18n.format("craftingex.result.select"), width / 2, 15, 0xFFFFFF);

		super.drawScreen(mouseX, mouseY, ticks);

		filterTextField.drawTextBox();
	}

	@Override
	protected void mouseClicked(int x, int y, int code) throws IOException
	{
		super.mouseClicked(x, y, code);

		filterTextField.mouseClicked(x, y, code);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();

		itemList.handleMouseInput();
	}

	@Override
	protected void keyTyped(char c, int code) throws IOException
	{
		if (filterTextField.isFocused())
		{
			if (code == Keyboard.KEY_ESCAPE)
			{
				filterTextField.setFocused(false);
			}

			String prev = filterTextField.getText();

			filterTextField.textboxKeyTyped(c, code);

			String text = filterTextField.getText();
			boolean changed = text != prev;

			if (Strings.isNullOrEmpty(text) && changed)
			{
				itemList.setFilter(null);
			}
			else if (changed || code == Keyboard.KEY_RETURN)
			{
				itemList.setFilter(text);
			}
		}
		else
		{
			if (code == Keyboard.KEY_ESCAPE)
			{
				mc.displayGuiScreen(parent);

				if (parent == null)
				{
					mc.setIngameFocus();
				}
			}
			else if (code == Keyboard.KEY_BACK)
			{
				itemList.selected = null;
			}
			else if (code == Keyboard.KEY_TAB)
			{
				if (++itemList.nameType > 1)
				{
					itemList.nameType = 0;
				}
			}
			else if (code == Keyboard.KEY_UP)
			{
				itemList.scrollUp();
			}
			else if (code == Keyboard.KEY_DOWN)
			{
				itemList.scrollDown();
			}
			else if (code == Keyboard.KEY_HOME)
			{
				itemList.scrollToTop();
			}
			else if (code == Keyboard.KEY_END)
			{
				itemList.scrollToEnd();
			}
			else if (code == Keyboard.KEY_SPACE)
			{
				itemList.scrollToSelected();
			}
			else if (code == Keyboard.KEY_PRIOR)
			{
				itemList.scrollToPrev();
			}
			else if (code == Keyboard.KEY_NEXT)
			{
				itemList.scrollToNext();
			}
			else if (code == Keyboard.KEY_F || code == mc.gameSettings.keyBindChat.getKeyCode())
			{
				filterTextField.setFocused(true);
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return parent.doesGuiPauseGame();
	}

	protected class ItemList extends GuiListSlot<ItemStack>
	{
		protected final ArrayListExtended<ItemStack> items = new ArrayListExtended<>();
		protected final ArrayListExtended<ItemStack> contents = new ArrayListExtended<>();

		protected ItemStack selected;
		protected int nameType;

		private boolean clickFlag;

		protected ItemList()
		{
			super(GuiCraftingResult.this.mc, 0, 0, 0, 0, 22);

			if (results != null && !results.isEmpty())
			{
				for (ItemStack stack : results)
				{
					ItemStack entry = stack.copy();

					items.add(entry);
					contents.add(entry);
				}
			}
		}

		@Override
		protected List<ItemStack> getContents()
		{
			return contents;
		}

		@Override
		protected List<ItemStack> getSelected()
		{
			return Lists.newArrayList(selected);
		}

		@Override
		public void scrollToSelected()
		{
			if (selected != null && contents.contains(selected))
			{
				scrollToTop();
				scrollBy(getContents().indexOf(selected) * getSlotHeight());
			}
			else
			{
				selected = null;
			}
		}

		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
		{
			if (clickFlag = !clickFlag == false)
			{
				return;
			}

			ItemStack entry = contents.get(slotIndex, ItemStack.EMPTY);

			if (!entry.isEmpty())
			{
				selected = entry;
			}

			if (isDoubleClick && selected != null)
			{
				GuiCraftingResult.this.actionPerformed(GuiCraftingResult.this.doneButton);
			}
		}

		@Override
		protected boolean isSelected(int slotIndex)
		{
			ItemStack entry = contents.get(slotIndex, ItemStack.EMPTY);

			return !entry.isEmpty() && selected != null && entry == selected;
		}

		@Override
		protected void drawBackground()
		{
			GuiCraftingResult.this.drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int slotIndex, int xPos, int yPos, int height, int mouseX, int mouseY, float partialTicks)
		{
			ItemStack entry = contents.get(slotIndex, ItemStack.EMPTY);

			if (entry.isEmpty())
			{
				return;
			}

			String name = null;

			try
			{
				switch (nameType)
				{
					case 1:
						name = entry.getItem().getRegistryName().toString();
						break;
					case 2:
						name = entry.getUnlocalizedName();
						name = name.substring(name.indexOf(".") + 1);
						break;
					default:
						name = entry.getDisplayName();
						break;
				}
			}
			catch (Throwable e) {}

			if (!Strings.isNullOrEmpty(name))
			{
				GuiCraftingResult.this.drawCenteredString(GuiCraftingResult.this.fontRenderer, name, width / 2, yPos + 3, 0xFFFFFF);
			}

			RenderHelper.enableGUIStandardItemLighting();
			int x = width / 2 - 100;
			int y = yPos + 1;
			GuiCraftingResult.this.itemRender.zLevel = 100.0F;
			GuiCraftingResult.this.itemRender.renderItemAndEffectIntoGUI(entry, x, y);
			GuiCraftingResult.this.itemRender.renderItemOverlays(GuiCraftingResult.this.fontRenderer, entry, x, y);
			GuiCraftingResult.this.itemRender.zLevel = 0.0F;
			RenderHelper.disableStandardItemLighting();
		}

		protected void setFilter(String filter)
		{
			List<ItemStack> result;

			if (Strings.isNullOrEmpty(filter))
			{
				result = items;
			}
			else
			{
				result = Lists.newArrayList(Collections2.filter(items, stack -> CraftingEX.itemFilter(stack, filter)));
			}

			if (!contents.equals(result))
			{
				contents.clear();
				contents.addAll(result);
			}
		}
	}
}