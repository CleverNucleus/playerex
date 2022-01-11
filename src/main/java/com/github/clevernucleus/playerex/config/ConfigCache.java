package com.github.clevernucleus.playerex.config;

import net.minecraft.nbt.NbtCompound;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public final class ConfigCache {
	public static final ConfigCache INSTANCE = new ConfigCache();
	
	protected boolean resetOnDeath;
	protected boolean showLevelNameplates;
	protected int skillPointsPerLevelUp;
	protected String levelFormula;
	protected Expression expression;
	
	private ConfigCache() {}
	
	protected Expression expression(final String expression) {
		return new ExpressionBuilder(expression).variable("x").build();
	}
	
	protected void set(ConfigImpl config) {
		this.resetOnDeath = config.resetOnDeath;
		this.showLevelNameplates = config.showLevelNameplates;
		this.skillPointsPerLevelUp = config.skillPointsPerLevelUp;
		this.levelFormula = config.levelFormula;
		this.expression = this.expression(this.levelFormula);
	}
	
	public void readFromNbt(NbtCompound tag) {
		this.resetOnDeath = tag.getBoolean("resetOnDeath");
		this.showLevelNameplates = tag.getBoolean("showLevelNameplates");
		this.skillPointsPerLevelUp = tag.getInt("skillPointsPerLevelUp");
		this.levelFormula = tag.getString("levelFormula");
		this.expression = this.expression(this.levelFormula);
	}
	
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("resetOnDeath", this.resetOnDeath);
		tag.putBoolean("showLevelNameplates", this.showLevelNameplates);
		tag.putInt("skillPointsPerLevelUp", this.skillPointsPerLevelUp);
		tag.putString("levelFormula", this.levelFormula);
	}
}
