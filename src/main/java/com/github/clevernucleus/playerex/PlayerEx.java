package com.github.clevernucleus.playerex;

import com.github.clevernucleus.playerex.handler.EventHandler;
import com.github.clevernucleus.playerex.handler.NetworkHandler;
import com.github.clevernucleus.playerex.impl.ModifierJsonLoader;

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
	
	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MANAGER);
		ServerLoginConnectionEvents.QUERY_START.register(NetworkHandler::loginQueryStart);
		ServerPlayerEvents.COPY_FROM.register(EventHandler::respawn);
		ServerLoginNetworking.registerGlobalReceiver(NetworkHandler.SYNC, NetworkHandler::loginQueryResponse);
	}
}
