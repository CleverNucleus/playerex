package com.github.clevernucleus.playerex.client.factory;

import java.util.List;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ExConfig;
import com.github.clevernucleus.playerex.client.PlayerExClient;
import com.github.clevernucleus.playerex.client.gui.ExScreenData;
import com.github.clevernucleus.playerex.client.gui.Page;
import com.github.clevernucleus.playerex.client.gui.widget.TabButtonWidget;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public final class EventFactoryClient {
	public static void onScreenInit(MinecraftClient client, Screen screen, int width, int height) {
		ExConfig config = ExAPI.getConfig();

		if(screen instanceof InventoryScreen && !config.isAttributesGuiDisabled() && !config.disableInventoryTabs()) {
			HandledScreen<?> handledScreen = (HandledScreen<?>)screen;
			ExScreenData screenData = (ExScreenData)screen;
			
			if(Screens.getButtons(screen) != null) {
				Screens.getButtons(screen).add(new TabButtonWidget(handledScreen, PlayerExClient.INVENTORY, 0, 0, -28, false, btn -> {}));
				List<Page> pages = screenData.pages();
				
				for(int i = 0; i < pages.size(); i++) {
					Page page = pages.get(i);
					int j = i + 1;
					int u = ((j % 5) * 29) + (j < 6 ? 0 : 3);
					int v = j < 6 ? -28 : 162;
					
					Screens.getButtons(screen).add(new TabButtonWidget(handledScreen, page, j, u, v, true, btn -> NetworkFactoryClient.openAttributesScreen(j - 1)));
				}
			}
		}
	}
	
	public static void onKeyPressed(MinecraftClient client) {
		if(ExAPI.getConfig().isAttributesGuiDisabled()) return;
		while(PlayerExClient.keyBinding.wasPressed()) {
			if(client.currentScreen == null && !client.interactionManager.hasRidingInventory()) {
				NetworkFactoryClient.openAttributesScreen(0);
			}
		}
	}
}
