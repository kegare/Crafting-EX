package craftingex.core;

import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

import craftingex.handler.CraftingEventHooks;
import craftingex.handler.CraftingGuiHandler;
import craftingex.network.NextRecipeMessage;
import craftingex.network.OpenCraftingMessage;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "craftingex")
public class CraftingEX
{
	@Instance("craftingex")
	public static CraftingEX instance;

	private static ForkJoinPool pool;

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("craftingex");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		NETWORK.registerMessage(NextRecipeMessage.class, NextRecipeMessage.class, 0, Side.SERVER);
		NETWORK.registerMessage(OpenCraftingMessage.class, OpenCraftingMessage.class, 1, Side.SERVER);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new CraftingGuiHandler());

		MinecraftForge.EVENT_BUS.register(new CraftingEventHooks());
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
		if (itemstack.isEmpty() || Strings.isNullOrEmpty(filter))
		{
			return false;
		}

		try
		{
			if (containsIgnoreCase(itemstack.getItem().getRegistryName().toString(), filter))
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