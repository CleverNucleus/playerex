package com.github.clevernucleus.playerex.client;

import com.github.clevernucleus.playerex.client.factory.NetworkFactoryClient;
import com.github.clevernucleus.playerex.factory.NetworkFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PlayerExClient implements ClientModInitializer {
    
	@Override
	public void onInitializeClient() {
		ClientLoginNetworking.registerGlobalReceiver(NetworkFactory.CONFIG, NetworkFactoryClient::loginQueryReceived);
		ClientPlayNetworking.registerGlobalReceiver(NetworkFactory.NOTIFY, NetworkFactoryClient::notifiedLevelUp);
	}
}
