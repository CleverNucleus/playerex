package com.github.clevernucleus.playerex.impl;

import com.github.clevernucleus.playerex.api.ExAPI;

import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;

public class PersistentPlayerCacheContainer implements LevelComponentInitializer {
	
	@Override
	public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
		registry.register(ExAPI.CACHE, p -> new PersistentPlayerCacheManager());
	}
}
