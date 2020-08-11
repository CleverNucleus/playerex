package clevernucleus.playerex.client.event;

import clevernucleus.playerex.client.gui.PlayerElementsScreen;
import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client events repository on the MOD event bus.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegistryEvents {
	
	/**
	 * Event handling initial client setup.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onClientLoad(final FMLClientSetupEvent par0) {
		ScreenManager.registerFactory(Registry.ELEMENTS_CONTAINER, PlayerElementsScreen::new);
	}
}
