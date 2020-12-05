package git.clevernucleus.playerex.event;

import git.clevernucleus.playerex.api.ExAPI;
import git.clevernucleus.playerex.api.element.Elements;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Repository for common events on the MOD bus.
 */
@Mod.EventBusSubscriber(modid = ExAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {
	
	@SubscribeEvent
	public static void onCommonSetup(final FMLCommonSetupEvent par0) {
		/*Elements.registerGetFunction(Elements.EXPERIENCE.registry(), (var0, var1) -> var1.get(Elements.EXPERIENCE));
		Elements.registerGetFunction(Elements.LEVEL.registry(), (var0, var1) -> var1.get(Elements.LEVEL));
		Elements.registerGetFunction(Elements.SKILLPOINTS.registry(), (var0, var1) -> var1.get(Elements.SKILLPOINTS));
		Elements.registerGetFunction(Elements.CONSTITUTION.registry(), (var0, var1) -> var1.get(Elements.CONSTITUTION));
		Elements.registerGetFunction(Elements.STRENGTH.registry(), (var0, var1) -> var1.get(Elements.STRENGTH));
		Elements.registerGetFunction(Elements.DEXTERITY.registry(), (var0, var1) -> var1.get(Elements.DEXTERITY));
		Elements.registerGetFunction(Elements.INTELLIGENCE.registry(), (var0, var1) -> var1.get(Elements.INTELLIGENCE));
		Elements.registerGetFunction(Elements.LUCKINESS.registry(), (var0, var1) -> var1.get(Elements.LUCKINESS));
		Elements.registerGetFunction(Elements.EXPERIENCE.registry(), (var0, var1) -> var1.get(Elements.EXPERIENCE));
		Elements.registerGetFunction(Elements.HEALTH.registry(), (var0, var1) -> var0.getMaxHealth());
		Elements.registerGetFunction(Elements.HEALTH_REGEN.registry(), (var0, var1) -> var1.get(Elements.HEALTH_REGEN));
		Elements.registerGetFunction(Elements.HEALTH_REGEN.registry(), (var0, var1) -> var1.get(Elements.HEALTH_REGEN));*/
	}
}
