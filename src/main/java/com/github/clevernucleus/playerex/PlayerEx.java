package com.github.clevernucleus.playerex;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ExRegistryProvider;
import com.github.clevernucleus.playerex.impl.AttributeManager;
import com.github.clevernucleus.playerex.impl.ExRegistryImpl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public final class PlayerEx implements ModInitializer {
	
	@SuppressWarnings("deprecation")
	@Override
	public void onInitialize() {
		((ExRegistryProvider)ExAPI.REGISTRY).init(new ExRegistryImpl());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new AttributeManager());
	}
}
