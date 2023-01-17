package com.github.clevernucleus.playerex.config;

import com.github.clevernucleus.dataattributes.api.util.Maths;

import net.minecraft.nbt.NbtCompound;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

public final class ConfigServer {
	private static final String RESET_ON_DEATH = "ResetOnDeath", DISABLE_ATTRIBUTES_GUI = "DisableAttributesGui", SKILL_POINTS_PER_LEVEL_UP = "SkillPointsPerLevelUp", LEVEL_NAMEPLATE = "LevelNameplate", LEVEL_FORMULA = "LevelFormula", VARIABLE = "x", RESTORATIVE_FORCE_TICKS = "RestorativeForceTicks", RESTORATIVE_FORCE_MULTIPLIER = "RestorativeForceMultiplier", EXP_NEGATION_FACTOR = "ExpNegationFactor";
	
	/**
	 * stairs(x, stretch, steepness, x-offset, y-offset, y-limit)
	 */
	private static final Function STAIRS = new Function("stairs", 6) {
		
		@Override
		public double apply(double... args) {
			return Math.min(Maths.stairs(args[0], args[1], args[2], args[3], args[4]), args[5]);
		}
	};
	protected static final ConfigServer INSTANCE = new ConfigServer();
	protected boolean resetOnDeath;
	protected boolean disableAttributesGui;
	protected boolean showLevelNameplates;
	protected int skillPointsPerLevelUp;
	protected int restorativeForceTicks;
	protected int restorativeForceMultiplier;
	protected int expNegationFactor;
	protected String levelFormula;
	private Expression expression;
	
	private ConfigServer() {}
	
	private Expression createExpression() {
		return new ExpressionBuilder(this.levelFormula).variable(VARIABLE).function(STAIRS).build();
	}
	
	protected void init(final ConfigImpl config) {
		this.resetOnDeath = config.resetOnDeath;
		this.disableAttributesGui = config.disableAttributesGui;
		this.showLevelNameplates = config.showLevelNameplates;
		this.skillPointsPerLevelUp = config.skillPointsPerLevelUp;
		this.restorativeForceTicks = config.restorativeForceTicks;
		this.restorativeForceMultiplier = config.restorativeForceMultiplier;
		this.expNegationFactor = config.expNegationFactor;
		this.levelFormula = config.levelFormula;
		this.expression = this.createExpression();
	}
	
	protected int level(final double value) {
		Expression expression2 = this.expression.setVariable(VARIABLE, Math.round((float)value));
		return Math.abs(Math.round((float)expression2.evaluate()));
	}
	
	public void readFromNbt(NbtCompound tag) {
		this.resetOnDeath = tag.getBoolean(RESET_ON_DEATH);
		this.disableAttributesGui = tag.getBoolean(DISABLE_ATTRIBUTES_GUI);
		this.showLevelNameplates = tag.getBoolean(LEVEL_NAMEPLATE);
		this.skillPointsPerLevelUp = tag.getInt(SKILL_POINTS_PER_LEVEL_UP);
		this.restorativeForceTicks = tag.getInt(RESTORATIVE_FORCE_TICKS);
		this.restorativeForceMultiplier = tag.getInt(RESTORATIVE_FORCE_MULTIPLIER);
		this.expNegationFactor = tag.getInt(EXP_NEGATION_FACTOR);
		this.levelFormula = tag.getString(LEVEL_FORMULA);
		this.expression = this.createExpression();
	}
	
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean(RESET_ON_DEATH, this.resetOnDeath);
		tag.putBoolean(DISABLE_ATTRIBUTES_GUI, this.disableAttributesGui);
		tag.putBoolean(LEVEL_NAMEPLATE, this.showLevelNameplates);
		tag.putInt(SKILL_POINTS_PER_LEVEL_UP, this.skillPointsPerLevelUp);
		tag.putInt(RESTORATIVE_FORCE_TICKS, this.restorativeForceTicks);
		tag.putInt(RESTORATIVE_FORCE_MULTIPLIER, this.restorativeForceMultiplier);
		tag.putInt(EXP_NEGATION_FACTOR, this.expNegationFactor);
		tag.putString(LEVEL_FORMULA, this.levelFormula);
	}
}
