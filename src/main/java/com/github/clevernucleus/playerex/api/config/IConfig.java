package com.github.clevernucleus.playerex.api.config;

import java.util.function.Supplier;

import net.minecraft.entity.LivingEntity;


public interface IConfig {
	
	
	boolean resetOnDeath();
	
	
	boolean showLevelNameplates();
	
	
	int requiredXp(final LivingEntity entity);
	
	
	float levelUpVolume();
	
	
	float skillUpVolume();
	
	
	int inventoryButtonPosX();
	
	
	int inventoryButtonPosY();
	
	
	@FunctionalInterface
	interface Provider extends Supplier<IConfig> {}
}
