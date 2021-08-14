package com.github.clevernucleus.playerex.client;

import com.github.clevernucleus.playerex.handler.NetworkHandler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;

public final class PlayerExClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		ClientLoginNetworking.registerGlobalReceiver(NetworkHandler.SYNC, NetworkHandlerClient::loginQueryReceived);
	}
}
