package clevernucleus.playerex.client.event;

import clevernucleus.playerex.client.gui.OverlayScreen;
import clevernucleus.playerex.client.gui.TexturedButton;
import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.network.SwitchScreens;
import clevernucleus.playerex.common.util.ConfigSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Events holder on the FORGE bus for client side hooks.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GuiEvents {
	
	/** Custom overlay object. */
	private static OverlayScreen overlay = new OverlayScreen(() -> Minecraft.getInstance());
	
	/**
	 * Event for adding components to a container.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onGuiInit(final GuiScreenEvent.InitGuiEvent.Post par0) {
		Screen var0 = par0.getGui();
		
		if(var0 instanceof InventoryScreen) {
			ContainerScreen<?> var1 = (ContainerScreen<?>)var0;
			
			if(par0.getWidgetList() != null) {
				par0.addWidget(new TexturedButton(var1, 155, 7, 14, 13, 176, 0, 0, (var2, var3) -> {
					if(var2 instanceof InventoryScreen) {
						Registry.NETWORK.sendToServer(new SwitchScreens(false));
					}
				}, null));
			}
		}
	}
	
	@SubscribeEvent
	public static void onHUDRender(final RenderGameOverlayEvent.Pre par0) {
		if(!ConfigSetting.CLIENT.enableGui.get().booleanValue() || overlay == null) return;
		
		ElementType var0 = par0.getType();
		
		if(var0 == ElementType.HEALTH || var0 == ElementType.ARMOR || var0 == ElementType.FOOD || var0 == ElementType.EXPERIENCE || var0 == ElementType.HEALTHMOUNT || var0 == ElementType.JUMPBAR || var0 == ElementType.AIR) {
			par0.setCanceled(true);
		}
		
		if(var0 != ElementType.HOTBAR) return;
		if(Minecraft.getInstance().player.isCreative()) return;
		
		overlay.draw(par0.getMatrixStack(), true);
	}
	
	@SubscribeEvent
	public static void onRenderIntercept(final RenderGameOverlayEvent.Post par0) {
		if(!ConfigSetting.CLIENT.enableGui.get().booleanValue() || overlay == null) return;
		if(par0.getType() != ElementType.HOTBAR) return;
		if(Minecraft.getInstance().player.isCreative()) return;
		
		overlay.draw(par0.getMatrixStack(), false);
	}
}
