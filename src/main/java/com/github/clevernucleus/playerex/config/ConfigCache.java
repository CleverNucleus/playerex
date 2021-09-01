package com.github.clevernucleus.playerex.config;

import net.minecraft.nbt.NbtCompound;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public final class ConfigCache {
	public boolean resetOnDeath;
	public boolean showLevelNameplates;
	public String levelFormula;
	public Expression expression;
	
	public ConfigCache() {}
	
	protected Expression standard(final String expression) {
		return new ExpressionBuilder(expression).variable("x").build();
	}
	
	public void write(NbtCompound tag) {
		tag.putBoolean("resetOnDeath", this.resetOnDeath);
		tag.putBoolean("showLevelNameplates", this.showLevelNameplates);
		tag.putString("levelFormula", this.levelFormula);
	}
	
	public void read(NbtCompound tag) {
		this.resetOnDeath = tag.getBoolean("resetOnDeath");
		this.showLevelNameplates = tag.getBoolean("showLevelNameplates");
		
		final String levelFormula = tag.getString("levelFormula");
		
		this.levelFormula = levelFormula;
		this.expression = this.standard(levelFormula);
	}
}
