package com.github.clevernucleus.playerex.client;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.PageRegistry;
import com.github.clevernucleus.playerex.api.client.event.NameplateRenderEvent;
import com.github.clevernucleus.playerex.client.gui.AttributesPage;
import com.github.clevernucleus.playerex.client.gui.AttributesScreen;
import com.github.clevernucleus.playerex.client.gui.CombatPage;
import com.github.clevernucleus.playerex.client.network.ClientNetworkHandler;
import com.github.clevernucleus.playerex.handler.NetworkHandler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.util.Identifier;

public final class PlayerExClient implements ClientModInitializer {
	public static final Identifier ATTTRIBUTES_PAGE_KEY = new Identifier(ExAPI.MODID, "attributes");
	public static final Identifier COMBAT_PAGE_KEY = new Identifier(ExAPI.MODID, "combat");
	
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(NetworkHandler.SYNC_CONFIG, ClientNetworkHandler::syncConfig);
		ClientPlayNetworking.registerGlobalReceiver(NetworkHandler.SYNC_ATTRIBUTES, ClientNetworkHandler::syncAttributes);
		
		ScreenRegistry.register(PlayerEx.ATTRIBUTES_SCREEN_HANDLER, AttributesScreen::new);
		PageRegistry.register(ATTTRIBUTES_PAGE_KEY, (parent, handler, inv) -> new AttributesPage(parent, handler, inv));
		PageRegistry.register(COMBAT_PAGE_KEY, (parent, handler, inv) -> new CombatPage(parent, handler, inv));
		
		NameplateRenderEvent.ON_RENDER.register(ClientEventHandler::nameplateRender);
		ScreenEvents.AFTER_INIT.register(ClientEventHandler::screenInit);
		ItemTooltipCallback.EVENT.register(ClientEventHandler::tooltipModify);
	}
}
