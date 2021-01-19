package com.github.clevernucleus.playerex.client.gui;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.client.ClientConfig;
import com.github.clevernucleus.playerex.init.Registry;
import com.github.clevernucleus.playerex.init.container.SwitchScreens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExAPI.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
	
	/** Custom overlay object. */
	private static OverlayScreen overlay = new OverlayScreen(() -> Minecraft.getInstance());
	
	/**
	 * Event for adding to a gui container.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onGuiInitPost(final net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post par0) {
		Screen var0 = par0.getGui();
		
		if(var0 instanceof InventoryScreen) {
			ContainerScreen<?> var1 = (ContainerScreen<?>)var0;
			
			if(par0.getWidgetList() != null) {
				par0.addWidget(new TexturedButton(var1, 155, 7, 14, 13, 176, 0, 0, (var2, var3) -> {
					if(var2 instanceof InventoryScreen) {
						Registry.NETWORK.sendToServer(new SwitchScreens(false));
					}
				}));
			}
		}
	}
	
	/**
	 * Event for adding ingame HUD elements (pre-render).
	 * @param par0
	 */
	@SubscribeEvent
	public static void onHUDRenderPre(final net.minecraftforge.client.event.RenderGameOverlayEvent.Pre par0) {
		if(!ClientConfig.CLIENT.enableGui.get().booleanValue() || overlay == null) return;
		
		ElementType var0 = par0.getType();
		
		if(var0 == ElementType.HEALTH || var0 == ElementType.ARMOR || var0 == ElementType.FOOD || var0 == ElementType.EXPERIENCE || var0 == ElementType.HEALTHMOUNT || var0 == ElementType.JUMPBAR || var0 == ElementType.AIR) {
			par0.setCanceled(true);
		}
		
		if(var0 != ElementType.HOTBAR) return;
		if(Minecraft.getInstance().player.isCreative()) return;
		
		overlay.draw(par0.getMatrixStack(), true);
	}
	
	/**
	 * Event for adding ingame HUD elements (post-render).
	 * @param par0
	 */
	@SubscribeEvent
	public static void onHUDRenderPost(final net.minecraftforge.client.event.RenderGameOverlayEvent.Post par0) {
		if(!ClientConfig.CLIENT.enableGui.get().booleanValue() || overlay == null) return;
		if(par0.getType() != ElementType.HOTBAR) return;
		if(Minecraft.getInstance().player.isCreative()) return;
		
		overlay.draw(par0.getMatrixStack(), false);
	}
}
