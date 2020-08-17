package clevernucleus.playerex.client.event;

import clevernucleus.playerex.client.gui.PlayerElementsScreen;
import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.rarity.Rareness;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.util.ResourceLocation;
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
		Registry.RELIC_AMULET.addPropertyOverride(new ResourceLocation(PlayerEx.MODID, "rareness"), RELIC);
		Registry.RELIC_BODY.addPropertyOverride(new ResourceLocation(PlayerEx.MODID, "rareness"), RELIC);
		Registry.RELIC_HEAD.addPropertyOverride(new ResourceLocation(PlayerEx.MODID, "rareness"), RELIC);
		Registry.RELIC_RING.addPropertyOverride(new ResourceLocation(PlayerEx.MODID, "rareness"), RELIC);
	}
	
	private static final IItemPropertyGetter RELIC = (par0, par1, par2) -> {
		if(!par0.hasTag() || !par0.getTag().contains("Rareness")) return 0.0F;
		
		Rareness var0 = Rareness.read(par0.getTag());
		
		return var0.getProperty();
	};
}
