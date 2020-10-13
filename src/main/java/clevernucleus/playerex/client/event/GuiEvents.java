package clevernucleus.playerex.client.event;

import clevernucleus.playerex.client.gui.TexturedButton;
import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.network.SwitchScreens;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Events holder on the FORGE bus for client side hooks.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GuiEvents {
	
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
}
