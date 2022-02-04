package com.github.clevernucleus.playerex.api;

import net.minecraft.entity.player.PlayerEntity;


public interface IConfig {
	
	
	boolean resetOnDeath();
	
	
	boolean showLevelNameplates();
	
	
	int skillPointsPerLevelUp();
	
	
	int requiredXp(final PlayerEntity player);
	
	
	boolean isGuiDisabled();
	
	
	float levelUpVolume();
	
	
	float skillUpVolume();
	
	
	float textScaleX();
	
	
	float textScaleY();
}
