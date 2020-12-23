package git.clevernucleus.playerex.client.event;

import git.clevernucleus.playerex.api.ExAPI;
import git.clevernucleus.playerex.client.gui.PlayerElementsScreen;
import git.clevernucleus.playerex.event.RegistryEvents;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client events repository on the MOD event bus.
 */
@Mod.EventBusSubscriber(modid = ExAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
	
	/**
	 * Event handling initial client setup.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onClientLoad(final FMLClientSetupEvent par0) {
		ScreenManager.registerFactory(RegistryEvents.ELEMENTS_CONTAINER, PlayerElementsScreen::new);
	}
}
