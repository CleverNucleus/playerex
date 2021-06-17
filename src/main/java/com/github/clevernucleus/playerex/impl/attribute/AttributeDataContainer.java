package com.github.clevernucleus.playerex.impl.attribute;

import com.github.clevernucleus.playerex.api.ExAPI;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;

public final class AttributeDataContainer implements EntityComponentInitializer {
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(ExAPI.DATA, AttributeDataManager::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
}
