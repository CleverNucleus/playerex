package com.github.clevernucleus.playerex.config;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.IConfig;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.objecthunter.exp4j.Expression;

@Config(name = ExAPI.MODID)
public class ConfigImpl implements ConfigData, IConfig {
	public static enum Tooltip { DEFAULT, VANILLA, PLAYEREX; }
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.Gui.Tooltip(count = 2)
	protected boolean resetOnDeath = false;
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.Gui.Tooltip(count = 2)
	protected boolean showLevelNameplates = true;
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.Gui.Tooltip(count = 2)
	protected int skillPointsPerLevelUp = 1;
	
	@ConfigEntry.Category(value = "server")
	@ConfigEntry.Gui.Tooltip(count = 2)
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
	@ConfigEntry.BoundedDiscrete(min = 0, max = 50)
	@ConfigEntry.Gui.Tooltip
	private int textScaleX = 50;
	
	@ConfigEntry.Category(value = "client")
	@ConfigEntry.BoundedDiscrete(min = 0, max = 50)
	@ConfigEntry.Gui.Tooltip
	private int textScaleY = 50;
	
	@ConfigEntry.Category(value = "client")
	@ConfigEntry.Gui.Tooltip
	public Tooltip tooltip = Tooltip.PLAYEREX;
	
	public void init() {
		ConfigCache.INSTANCE.set(this);
	}
	
	@Override
	public boolean resetOnDeath() {
		return ConfigCache.INSTANCE.resetOnDeath;
	}
	
	@Override
	public boolean showLevelNameplates() {
		return ConfigCache.INSTANCE.showLevelNameplates;
	}
	
	@Override
	public int skillPointsPerLevelUp() {
		return MathHelper.clamp(ConfigCache.INSTANCE.skillPointsPerLevelUp, 1, Integer.MAX_VALUE);
	}
	
	@Override
	public int requiredXp(final PlayerEntity player) {
		return DataAttributesAPI.ifPresent(player, ExAPI.LEVEL, 1, value -> {
			Expression expression = ConfigCache.INSTANCE.expression.setVariable("x", Math.round(value));
			float amount = (float)expression.evaluate();
			return Math.abs(Math.round(amount));
		});
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
	public float textScaleX() {
		return (this.textScaleX + 25) * 0.01F;
	}
	
	@Override
	public float textScaleY() {
		return (this.textScaleY + 25) * 0.01F;
	}
}
