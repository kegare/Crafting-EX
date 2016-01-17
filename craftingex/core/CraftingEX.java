/*
 * Crafting EX
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package craftingex.core;

import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;

import com.google.common.base.Strings;

import craftingex.handler.CraftingEventHooks;
import craftingex.handler.CraftingGuiHandler;
import craftingex.network.NextRecipeMessage;
import craftingex.network.OpenCraftingMessage;
import craftingex.plugin.nei.NEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "craftingex")
public class CraftingEX
{
	@Instance("craftingex")
	public static CraftingEX instance;

	private static ForkJoinPool pool;

	public static final SimpleNetworkWrapper network = new SimpleNetworkWrapper("craftingex");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		network.registerMessage(NextRecipeMessage.class, NextRecipeMessage.class, 0, Side.SERVER);
		network.registerMessage(OpenCraftingMessage.class, OpenCraftingMessage.class, 1, Side.SERVER);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new CraftingGuiHandler());

		MinecraftForge.EVENT_BUS.register(CraftingEventHooks.instance);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		try
		{
			if (event.getSide().isClient() && NEIPlugin.enabled())
			{
				NEIPlugin.invoke();
			}
		}
		catch (Throwable e)
		{
			FMLLog.log(Level.WARN, e, "Failed to trying invoke plugin: NEIPlugin");
		}
	}

	@NetworkCheckHandler
	public boolean netCheckHandler(Map<String, String> mods, Side side)
	{
		return true;
	}

	public static ForkJoinPool getPool()
	{
		if (pool == null || pool.isShutdown())
		{
			pool = new ForkJoinPool();
		}

		return pool;
	}

	public static int compareWithNull(Object o1, Object o2)
	{
		return (o1 == null ? 1 : 0) - (o2 == null ? 1 : 0);
	}

	public static boolean containsIgnoreCase(String s1, String s2)
	{
		if (Strings.isNullOrEmpty(s1) || Strings.isNullOrEmpty(s2))
		{
			return false;
		}

		return Pattern.compile(Pattern.quote(s2), Pattern.CASE_INSENSITIVE).matcher(s1).find();
	}

	public static boolean itemFilter(ItemStack itemstack, String filter)
	{
		if (itemstack == null || itemstack.getItem() == null || Strings.isNullOrEmpty(filter))
		{
			return false;
		}

		try
		{
			if (containsIgnoreCase(GameData.getItemRegistry().getNameForObject(itemstack.getItem()).toString(), filter))
			{
				return true;
			}
		}
		catch (Throwable e) {}

		try
		{
			if (containsIgnoreCase(itemstack.getUnlocalizedName(), filter))
			{
				return true;
			}
		}
		catch (Throwable e) {}

		try
		{
			if (containsIgnoreCase(itemstack.getDisplayName(), filter))
			{
				return true;
			}
		}
		catch (Throwable e) {}

		try
		{
			if (itemstack.getItem().getToolClasses(itemstack).contains(filter))
			{
				return true;
			}
		}
		catch (Throwable e) {}

		return false;
	}
}