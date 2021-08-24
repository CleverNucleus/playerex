package com.github.clevernucleus.playerex.client;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.PageRegistry;
import com.github.clevernucleus.playerex.client.gui.AttributesPageLayer;
import com.github.clevernucleus.playerex.client.gui.AttributesScreen;
import com.github.clevernucleus.playerex.client.gui.CombatPageLayer;
import com.github.clevernucleus.playerex.handler.NetworkHandler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public final class PlayerExClient implements ClientModInitializer {
	public static final Identifier GUI = new Identifier(ExAPI.MODID, "textures/gui/gui.png");
	public static final Identifier ATTRIBUTES_PAGE = new Identifier(ExAPI.MODID, "attributes");
	public static final Identifier COMBAT_PAGE = new Identifier(ExAPI.MODID, "combat");
	
	@Override
	public void onInitializeClient() {
		ClientLoginNetworking.registerGlobalReceiver(NetworkHandler.SYNC, NetworkHandlerClient::loginQueryReceived);
		ClientPlayNetworking.registerGlobalReceiver(NetworkHandler.LEVEL, NetworkHandlerClient::levelUpEvent);
		
		ScreenRegistry.register(PlayerEx.ATTRIBUTES_SCREEN, AttributesScreen::new);
		PageRegistry.registerPage(ATTRIBUTES_PAGE, new TranslatableText("gui.playerex.page.attributes.title"), new ItemStack(Items.BOOK));
		PageRegistry.registerPage(COMBAT_PAGE, new TranslatableText("gui.playerex.page.combat.title"), new ItemStack(Items.IRON_SWORD));
		PageRegistry.registerLayer(ATTRIBUTES_PAGE, AttributesPageLayer::new);
		PageRegistry.registerLayer(COMBAT_PAGE, CombatPageLayer::new);
		
		ScreenEvents.AFTER_INIT.register(EventHandlerClient::onScreenInit);
	}
}
