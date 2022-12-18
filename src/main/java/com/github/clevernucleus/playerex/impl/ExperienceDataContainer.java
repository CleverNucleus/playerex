package com.github.clevernucleus.playerex.impl;

import com.github.clevernucleus.playerex.api.ExAPI;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;

public final class ExperienceDataContainer implements ChunkComponentInitializer {
	
	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(ExAPI.EXPERIENCE_DATA, ExperienceDataManager.class, ExperienceDataManager::new);
	}
}
