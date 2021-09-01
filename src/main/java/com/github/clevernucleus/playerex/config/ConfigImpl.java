package com.github.clevernucleus.playerex.config;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.config.IConfig;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.objecthunter.exp4j.Expression;

@Config(name = ExAPI.MODID)
public class ConfigImpl implements ConfigData, IConfig {
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.Gui.Tooltip(count = 2)
	protected boolean resetOnDeath = false;
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.Gui.Tooltip(count = 2)
	protected boolean showLevelNameplates = true;
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.Gui.Tooltip(count = 3)
	protected String levelFormula = "10 + ((0.2 * x) - 2)^3";
	
	@ConfigEntry.Category(value = "client")
	@ConfigEntry.BoundedDiscrete(min = 0, max = 150)
	@ConfigEntry.Gui.Tooltip
	private int levelUpVolume = 100;
	
	@ConfigEntry.Category(value = "client")
	@ConfigEntry.BoundedDiscrete(min = 0, max = 150)
	@ConfigEntry.Gui.Tooltip
	private int skillUpVolume = 100;
	
	@ConfigEntry.Category(value = "client")
	@ConfigEntry.BoundedDiscrete(min = -200, max = 200)
	@ConfigEntry.Gui.Tooltip
	private int inventoryButtonPosX = 155;
	
	@ConfigEntry.Category(value = "client")
	@ConfigEntry.BoundedDiscrete(min = -200, max = 200)
	@ConfigEntry.Gui.Tooltip
	private int inventoryButtonPosY = 7;
	
	public void init(final ConfigCache cache) {
		cache.resetOnDeath = this.resetOnDeath;
		cache.showLevelNameplates = this.showLevelNameplates;
		cache.levelFormula = this.levelFormula;
		cache.expression = cache.standard(this.levelFormula);
	}
	
	@Override
	public boolean resetOnDeath() {
		return PlayerEx.CONFIG.resetOnDeath;
	}
	
	@Override
	public boolean showLevelNameplates() {
		return PlayerEx.CONFIG.showLevelNameplates;
	}
	
	@Override
	public int requiredXp(final PlayerEntity player) {
		if(player == null) return 1;
		
		AttributeContainer container = player.getAttributes();
		EntityAttribute attribute = ExAPI.LEVEL.get();
		
		if(attribute == null || !container.hasAttribute(attribute)) return 1;
		
		int level = (int)Math.round(container.getValue(attribute));
		Expression expression = PlayerEx.CONFIG.expression.setVariable("x", level);
		float result = (float)expression.evaluate();
		int rounded = Math.round(result);
		
		return Math.abs(rounded);
	}
	
	@Override
	public float levelUpVolume() {
		return this.levelUpVolume * 0.01F;
	}
	
	@Override
	public float skillUpVolume() {
		return this.skillUpVolume * 0.01F;
	}
	
	@Override
	public int inventoryButtonPosX() {
		return this.inventoryButtonPosX;
	}
	
	@Override
	public int inventoryButtonPosY() {
		return this.inventoryButtonPosY;
	}
}
