package com.github.clevernucleus.playerex.impl;

import java.util.Random;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ExConfig;
import com.github.clevernucleus.playerex.api.ExperienceData;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.chunk.Chunk;

public final class ExperienceDataManager implements ExperienceData {
	private static final String KEY_EXP_NEGATION_CHANCE = "ExpNegationFactor";
	private static final Random RANDOM = new Random();
	private final Chunk chunk;
	private float expNegationFactor;
	private int ticks;
	private final int restorativeForceTicks;
	private final float restorativeForce;
	private final float expNegationMultiplier;
	
	public ExperienceDataManager(final Chunk chunk) {
		this.chunk = chunk;
		this.expNegationFactor = 1.0F;
		this.ticks = 0;
		
		ExConfig config = ExAPI.getConfig();
		this.restorativeForceTicks = config.restorativeForceTicks();
		this.restorativeForce = config.restorativeForceMultiplier();
		this.expNegationMultiplier = config.expNegationFactor();
	}
	
	@Override
	public boolean updateExperienceNegationFactor(final int amount) {
		if(RANDOM.nextFloat() > this.expNegationFactor) return true;
		float dynamicMultiplier = this.expNegationMultiplier + ((1.0F - this.expNegationMultiplier) * (1.0F - (0.1F * (float)amount)));
		this.expNegationFactor = Math.max(this.expNegationFactor * dynamicMultiplier, 0.0F);
		this.chunk.setNeedsSaving(true);
		return false;
	}
	
	@Override
	public void resetExperienceNegationFactor() {
		this.expNegationFactor = 1.0F;
	}
	
	@Override
	public void serverTick() {
		if(this.expNegationFactor == 1.0F) return;
		if(this.ticks < this.restorativeForceTicks) {
			this.ticks++;
		} else {
			this.ticks = 0;
			this.expNegationFactor = Math.min(this.expNegationFactor * this.restorativeForce, 1.0F);
			this.chunk.setNeedsSaving(true);
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		this.expNegationFactor = tag.getFloat(KEY_EXP_NEGATION_CHANCE);
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putFloat(KEY_EXP_NEGATION_CHANCE, this.expNegationFactor);
	}
}
