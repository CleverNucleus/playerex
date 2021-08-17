package com.github.clevernucleus.playerex;

import com.github.clevernucleus.dataattributes.api.event.EntityAttributeEvents;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.config.ExConfigProvider;
import com.github.clevernucleus.playerex.config.ConfigCache;
import com.github.clevernucleus.playerex.config.ConfigImpl;
import com.github.clevernucleus.playerex.handler.EventHandler;
import com.github.clevernucleus.playerex.handler.NetworkHandler;
import com.github.clevernucleus.playerex.impl.ModifierJsonLoader;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public final class PlayerEx implements ModInitializer {
	public static final ModifierJsonLoader MANAGER = new ModifierJsonLoader();
	/** Manual; ugh, I know. */
	public static final String VERSION = "3.0.1";
	public static final ConfigCache CONFIG = new ConfigCache();
	
	@SuppressWarnings("deprecation")
	@Override
	public void onInitialize() {
		AutoConfig.register(ConfigImpl.class, GsonConfigSerializer::new);
		((ExConfigProvider)ExAPI.CONFIG).build(AutoConfig.getConfigHolder(ConfigImpl.class).get());
		
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MANAGER);
		
		EntityAttributeEvents.MODIFIER_ADDED_PRE.register(EventHandler::healthModified);
		ServerLoginConnectionEvents.QUERY_START.register(NetworkHandler::loginQueryStart);
		ServerPlayerEvents.COPY_FROM.register(EventHandler::respawn);
		ServerLoginNetworking.registerGlobalReceiver(NetworkHandler.SYNC, NetworkHandler::loginQueryResponse);
	}
}
