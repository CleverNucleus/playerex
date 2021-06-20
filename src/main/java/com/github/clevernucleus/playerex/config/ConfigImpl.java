package com.github.clevernucleus.playerex.config;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.util.IConfig;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = ExAPI.MODID)
public class ConfigImpl implements ConfigData, IConfig {
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.BoundedDiscrete(min = 1, max = 20)
	@ConfigEntry.Gui.Tooltip(count = 2)
	protected int levelOffset = 2;
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.BoundedDiscrete(min = 1, max = 100)
	@ConfigEntry.Gui.Tooltip(count = 3)
	protected int levelMultiplier = 20;
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.Gui.Tooltip(count = 2)
	protected boolean resetOnDeath = false;
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.Gui.Tooltip(count = 2)
	protected boolean announceLevelUp = true;
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.Gui.Tooltip(count = 2)
	protected boolean showLevelNameplates = true;
	
	@ConfigEntry.Category(value = "client")
	@ConfigEntry.Gui.Tooltip
	private boolean playSkillUpSound = true;
	
	@ConfigEntry.Category(value = "client")
	@ConfigEntry.BoundedDiscrete(min = -200, max = 200)
	@ConfigEntry.Gui.Tooltip
	private int guiButtonPosX = 155;
	
	@ConfigEntry.Category(value = "client")
	@ConfigEntry.BoundedDiscrete(min = -200, max = 200)
	@ConfigEntry.Gui.Tooltip
	private int guiButtonPosY = 7;
	
	@Override
	public int levelOffset() {
		return (int)PlayerEx.CONFIG_CACHE.levelOffset;
	}
	
	@Override
	public float levelMultiplier() {
		return (float)PlayerEx.CONFIG_CACHE.levelMultiplier * 0.01F;
	}
	
	@Override
	public boolean resetOnDeath() {
		return PlayerEx.CONFIG_CACHE.resetOnDeath;
	}
	
	@Override
	public boolean announceLevelUp() {
		return PlayerEx.CONFIG_CACHE.announceLevelUp;
	}
	
	@Override
	public boolean showLevelNameplates() {
		return PlayerEx.CONFIG_CACHE.showLevelNameplates;
	}
	
	@Override
	public boolean playSkillUpSound() {
		return this.playSkillUpSound;
	}
	
	@Override
	public int guiButtonPosX() {
		return this.guiButtonPosX;
	}
	
	@Override
	public int guiButtonPosY() {
		return this.guiButtonPosY;
	}
}
