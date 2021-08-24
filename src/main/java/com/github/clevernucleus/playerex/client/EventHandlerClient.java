package com.github.clevernucleus.playerex.client;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.client.gui.widget.ScreenButtonWidget;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public final class EventHandlerClient {
	public static void onScreenInit(MinecraftClient client, Screen screen, int width, int height) {
		if(screen instanceof InventoryScreen) {
			HandledScreen<?> handledScreen = (HandledScreen<?>)screen;
			
			if(Screens.getButtons(screen) != null) {
				int x = ExAPI.CONFIG.get().inventoryButtonPosX();
				int y = ExAPI.CONFIG.get().inventoryButtonPosY();
				
				Screens.getButtons(screen).add(new ScreenButtonWidget(handledScreen, x, y, 176, 0, 14, 13, NetworkHandlerClient::openAttributesScreen));
			}
		}
	}
}
