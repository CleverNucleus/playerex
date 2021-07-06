package com.github.clevernucleus.playerex.config;

import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.nbt.CompoundTag;

public final class ConfigCache {
	private ConfigImpl config;
	protected byte levelOffset, levelMultiplier;
	protected boolean resetOnDeath, showLevelNameplates;
	
	private byte getOrDefault(CompoundTag tag, String key, byte value) {
		return tag.contains(key) ? tag.getByte(key) : value;
	}
	
	private boolean getOrDefault(CompoundTag tag, String key, boolean value) {
		return tag.contains(key) ? tag.getBoolean(key) : value;
	}
	
	public void read(CompoundTag tag) {
		this.levelOffset = this.getOrDefault(tag, "levelOffset", (byte)this.config.levelOffset);
		this.levelMultiplier = this.getOrDefault(tag, "levelMultiplier", (byte)this.config.levelMultiplier);
		this.resetOnDeath = this.getOrDefault(tag, "resetOnDeath", this.config.resetOnDeath);
		this.showLevelNameplates = this.getOrDefault(tag, "showLevelNameplates", this.config.showLevelNameplates);
	}
	
	public void write(CompoundTag tag) {
		tag.putByte("levelOffset", this.levelOffset);
		tag.putByte("levelMultiplier", this.levelMultiplier);
		tag.putBoolean("resetOnDeath", this.resetOnDeath);
		tag.putBoolean("showLevelNameplates", this.showLevelNameplates);
	}
	
	public void build() {
		this.config = (ConfigImpl)ExAPI.CONFIG.get();
		this.levelOffset = (byte)this.config.levelOffset;
		this.levelMultiplier = (byte)this.config.levelMultiplier;
		this.resetOnDeath = this.config.resetOnDeath;
		this.showLevelNameplates = this.config.showLevelNameplates;
	}
}
