package com.github.clevernucleus.playerex.client;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.ClientReg;
import com.github.clevernucleus.playerex.client.gui.DefaultPage;
import com.github.clevernucleus.playerex.client.gui.PlayerAttributesScreen;
import com.github.clevernucleus.playerex.client.gui.ResistancePage;
import com.github.clevernucleus.playerex.init.Registry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistry {
	
	/**
	 * Mod client initialisation event.
	 * @param par0
	 */
	@SubscribeEvent
	public static void clientSetup(final net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent par0) {
		ScreenManager.registerFactory(Registry.ATTRIBUTES_CONTAINER, PlayerAttributesScreen::new);
		ClientReg.registerPage(DefaultPage.REGISTRY_NAME, new DefaultPage());
		ClientReg.registerPage(ResistancePage.REGISTRY_NAME, new ResistancePage());
	}
}
