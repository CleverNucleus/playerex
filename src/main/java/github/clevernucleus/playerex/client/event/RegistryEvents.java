package github.clevernucleus.playerex.client.event;

import github.clevernucleus.playerex.client.gui.DefaultPage;
import github.clevernucleus.playerex.client.gui.PlayerElementsScreen;
import github.clevernucleus.playerex.common.PlayerEx;
import github.clevernucleus.playerex.common.init.Registry;
import github.clevernucleus.playerex.api.Rareness;
import github.clevernucleus.playerex.api.client.ClientReg;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
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
		RenderTypeLookup.setRenderLayer(Registry.MAGIC_ICE, RenderType.getTranslucent());
		ScreenManager.registerFactory(Registry.ELEMENTS_CONTAINER, PlayerElementsScreen::new);
		ItemModelsProperties.func_239418_a_(Registry.RELIC_AMULET, new ResourceLocation(PlayerEx.MODID, "rareness"), RELIC);
		ItemModelsProperties.func_239418_a_(Registry.RELIC_BODY, new ResourceLocation(PlayerEx.MODID, "rareness"), RELIC);
		ItemModelsProperties.func_239418_a_(Registry.RELIC_HEAD, new ResourceLocation(PlayerEx.MODID, "rareness"), RELIC);
		ItemModelsProperties.func_239418_a_(Registry.RELIC_RING, new ResourceLocation(PlayerEx.MODID, "rareness"), RELIC);
		ClientReg.init(new DefaultPage(new TranslationTextComponent(PlayerEx.MODID + ".player_elements")));
	}
	
	private static final IItemPropertyGetter RELIC = (par0, par1, par2) -> {
		if(!par0.hasTag() || !par0.getTag().contains("Rareness")) return 0.0F;
		
		Rareness var0 = Rareness.read(par0.getTag());
		
		return var0.getProperty();
	};
}
