package kegare.craftingex.core;

import kegare.craftingex.handler.CraftingGuiHandler;
import kegare.craftingex.handler.CraftingPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod
(
	modid = "kegare.craftingex"
)
@NetworkMod
(
	clientSideRequired = true,
	serverSideRequired = false,
	channels = {"crafting.ex"},
	packetHandler = CraftingPacketHandler.class
)
public class CraftingEX
{
	@Instance("kegare.craftingex")
	public static CraftingEX instance;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.instance().registerGuiHandler(instance, new CraftingGuiHandler());

		MinecraftForge.EVENT_BUS.register(instance);
	}

	@ForgeSubscribe
	public void doOpenCraftingEX(PlayerInteractEvent event)
	{
		if (event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP)event.entityPlayer;
			WorldServer world = player.getServerForPlayer();
			int x = event.x;
			int y = event.y;
			int z = event.z;
			int blockID = world.getBlockId(x, y, z);

			if (blockID == Block.workbench.blockID && (!player.isSneaking() || (player.getHeldItem() == null || player.getHeldItem().getItem().shouldPassSneakingClickToBlock(world, x, y, z))))
			{
				player.openGui(instance, 0, world, x, y, z);

				event.setCanceled(true);
			}
		}
	}
}