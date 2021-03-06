package craftingex.handler;

import craftingex.core.CraftingEX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CraftingEventHooks
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.world;

		if (!world.isRemote)
		{
			BlockPos pos = event.getPos();

			if (world.getBlockState(pos).getBlock() == Blocks.CRAFTING_TABLE)
			{
				ItemStack stack = event.getItemStack();

				if (!player.isSneaking() || stack.isEmpty() || stack.getItem().doesSneakBypassUse(stack, world, pos, player))
				{
					if (stack.isEmpty() || stack.getItem() != Item.getItemFromBlock(Blocks.CRAFTING_TABLE))
					{
						player.openGui(CraftingEX.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());

						event.setCanceled(true);
					}
				}
			}
		}
	}
}