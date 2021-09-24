package com.github.clevernucleus.playerex;

import com.github.clevernucleus.dataattributes.api.event.EntityAttributeEvents;
import com.github.clevernucleus.dataattributes.api.event.MathClampEvent;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.config.ExConfigProvider;
import com.github.clevernucleus.playerex.api.event.PlayerLevelUpEvent;
import com.github.clevernucleus.playerex.config.ConfigCache;
import com.github.clevernucleus.playerex.config.ConfigImpl;
import com.github.clevernucleus.playerex.handler.AttributesScreenHandler;
import com.github.clevernucleus.playerex.handler.CommandsHandler;
import com.github.clevernucleus.playerex.handler.EventHandler;
import com.github.clevernucleus.playerex.handler.NetworkHandler;
import com.github.clevernucleus.playerex.impl.ModifierJsonLoader;
import com.github.clevernucleus.playerex.util.StoredPlaceholder;

import eu.pb4.placeholders.PlaceholderAPI;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class PlayerEx implements ModInitializer {
	public static final ModifierJsonLoader MANAGER = new ModifierJsonLoader();
	/** Manual; ugh, I know. */
	public static final String VERSION = "3.0.5";
	public static final ConfigCache CONFIG = new ConfigCache();
	public static final SoundEvent LEVEL_UP_SOUND = new SoundEvent(new Identifier(ExAPI.MODID, "level_up"));
	public static final SoundEvent SP_SPEND_SOUND = new SoundEvent(new Identifier(ExAPI.MODID, "sp_spend"));
	public static final ScreenHandlerType<AttributesScreenHandler> ATTRIBUTES_SCREEN = ScreenHandlerRegistry.registerSimple(new Identifier(ExAPI.MODID, "attributes_screen"), AttributesScreenHandler::new);
	
	@SuppressWarnings("deprecation")
	@Override
	public void onInitialize() {
		AutoConfig.register(ConfigImpl.class, GsonConfigSerializer::new);
		((ExConfigProvider)ExAPI.CONFIG).build(AutoConfig.getConfigHolder(ConfigImpl.class).get());
		((ConfigImpl)ExAPI.CONFIG.get()).init(CONFIG);
		
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MANAGER);
		
		PlayerLevelUpEvent.EVENT.register(NetworkHandler::levelUpEvent);
		MathClampEvent.EVENT.register(EventHandler::roundClampedValue);
		EntityAttributeEvents.MODIFIER_ADDED_POST.register(EventHandler::levelUpdate);
		EntityAttributeEvents.MODIFIER_REMOVED_POST.register(EventHandler::levelUpdate);
		ServerLoginConnectionEvents.QUERY_START.register(NetworkHandler::loginQueryStart);
		ServerPlayerEvents.COPY_FROM.register(EventHandler::respawn);
		ServerPlayConnectionEvents.JOIN.register(EventHandler::join);
		CommandRegistrationCallback.EVENT.register(CommandsHandler::init);
		
		ServerLoginNetworking.registerGlobalReceiver(NetworkHandler.SYNC, NetworkHandler::loginQueryResponse);
		ServerPlayNetworking.registerGlobalReceiver(NetworkHandler.SCREEN, NetworkHandler::switchScreen);
		ServerPlayNetworking.registerGlobalReceiver(NetworkHandler.MODIFY, NetworkHandler::modifyAttributes);
		
		Registry.register(Registry.SOUND_EVENT, LEVEL_UP_SOUND.getId(), LEVEL_UP_SOUND);
		Registry.register(Registry.SOUND_EVENT, SP_SPEND_SOUND.getId(), SP_SPEND_SOUND);
		
		StoredPlaceholder.STORE.forEach(PlaceholderAPI::register);
	}
}
