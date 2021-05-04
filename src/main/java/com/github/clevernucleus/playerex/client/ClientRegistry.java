package com.github.clevernucleus.playerex.client;

import org.lwjgl.glfw.GLFW;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.ClientReg;
import com.github.clevernucleus.playerex.client.gui.DefaultPage;
import com.github.clevernucleus.playerex.client.gui.PlayerAttributesScreen;
import com.github.clevernucleus.playerex.client.gui.ResistancePage;
import com.github.clevernucleus.playerex.init.Registry;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistry {
	public static final KeyBinding HUD = new KeyBinding("key." + ExAPI.MODID + ".hud", KeyConflictContext.IN_GAME, key(GLFW.GLFW_KEY_LEFT_ALT), "PlayerEx");
	
	private static InputMappings.Input key(int par0) {
		return InputMappings.Type.KEYSYM.getOrMakeInput(par0);
	}
	
	/**
	 * Mod client initialisation event.
	 * @param par0
	 */
	@SubscribeEvent
	public static void clientSetup(final net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent par0) {
		ScreenManager.registerFactory(Registry.ATTRIBUTES_CONTAINER, PlayerAttributesScreen::new);
		net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(HUD);
		
		par0.enqueueWork(() -> {
			ClientReg.registerPage(DefaultPage.REGISTRY_NAME, new DefaultPage());
			ClientReg.registerPage(ResistancePage.REGISTRY_NAME, new ResistancePage());
		});
	}
}
