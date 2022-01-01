package com.github.clevernucleus.playerex.config;

import net.minecraft.entity.player.PlayerEntity;


public interface IConfig {
	
	
	boolean resetOnDeath();
	
	
	boolean showLevelNameplates();
	
	
	int requiredXp(final PlayerEntity player);
	
	
	float levelUpVolume();
	
	
	float skillUpVolume();
	
	
	int inventoryButtonPosX();
	
	
	int inventoryButtonPosY();
	
	
	float textScaleX();
	
	
	float textScaleY();
}
