/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package craftingex.plugin.nei;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional.Method;
import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import craftingex.client.gui.GuiCraftingEX;

public class NEIPlugin
{
	public static final String MODID = "NotEnoughItems";

	public static boolean enabled()
	{
		return Loader.isModLoaded(MODID);
	}

	@Method(modid = MODID)
	public static void invoke()
	{
		API.registerGuiOverlay(GuiCraftingEX.class, "crafting");
		API.registerGuiOverlayHandler(GuiCraftingEX.class, new DefaultOverlayHandler(), "crafting");
	}
}