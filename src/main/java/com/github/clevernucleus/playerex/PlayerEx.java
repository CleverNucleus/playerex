package com.github.clevernucleus.playerex;

import com.github.clevernucleus.playerex.impl.AttributeManager;
import com.github.clevernucleus.playerex.impl.AttributeRegistry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public final class PlayerEx implements ModInitializer {
	public static final AttributeRegistry ATTRIBUTES = new AttributeRegistry();
	
	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new AttributeManager());
	}
}
