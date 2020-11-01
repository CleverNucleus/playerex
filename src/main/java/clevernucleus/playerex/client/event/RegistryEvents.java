package clevernucleus.playerex.client.event;

import clevernucleus.playerex.api.client.ClientReg;
import clevernucleus.playerex.client.gui.DefaultPage;
import clevernucleus.playerex.client.gui.PlayerElementsScreen;
import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.text.TranslationTextComponent;
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
		ClientReg.init(new DefaultPage(new TranslationTextComponent(PlayerEx.MODID + ".player_elements")));
		//ClientReg.registerPage(new clevernucleus.playerex.api.client.gui.Page(new TranslationTextComponent(PlayerEx.MODID + ".player_smithing")));
		//ClientReg.addTooltip(new net.minecraft.util.ResourceLocation(PlayerEx.MODID, "strength"), (var0, var1) -> net.minecraft.util.text.TextFormatting.GOLD + "+" + (int)(var0.getArmorCoverPercentage() * 100F) + "%");
	}
}
