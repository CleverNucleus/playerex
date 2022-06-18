package com.github.clevernucleus.playerex.impl;

import com.github.clevernucleus.playerex.api.ExAPI;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public final class PlayerDataContainer implements EntityComponentInitializer {
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(ExAPI.PLAYER_DATA, PlayerDataManager::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
}
