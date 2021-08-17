package com.github.clevernucleus.playerex.config;

import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.nbt.NbtCompound;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public final class ConfigCache {
	public boolean resetOnDeath;
	public boolean showLevelNameplates;
	public String levelFormula;
	public Expression expression;
	
	public ConfigCache() {
		this.expression = this.standard("x + 1");
	}
	
	private Expression standard(final String expression) {
		return new ExpressionBuilder(expression).variable("x").build();
	}
	
	private ConfigImpl get() {
		return (ConfigImpl)ExAPI.CONFIG.get();
	}
	
	public void write(NbtCompound tag) {
		tag.putBoolean("resetOnDeath", this.get().resetOnDeath);
		tag.putBoolean("showLevelNameplates", this.get().showLevelNameplates);
		tag.putString("levelFormula", this.get().levelFormula);
	}
	
	public void read(NbtCompound tag) {
		this.resetOnDeath = tag.getBoolean("resetOnDeath");
		this.showLevelNameplates = tag.getBoolean("showLevelNameplates");
		this.levelFormula = tag.getString("levelFormula");
		this.expression = this.standard(this.levelFormula);
	}
}
