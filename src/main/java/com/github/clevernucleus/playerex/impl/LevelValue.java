package com.github.clevernucleus.playerex.impl;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.opc.api.CacheableValue;
import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class LevelValue implements CacheableValue<Integer> {
	private final Identifier id;
	
	public LevelValue() { this.id = new Identifier(ExAPI.MODID, "level"); }
	
	@Override
	public Integer get(final ServerPlayerEntity player) {
		return DataAttributesAPI.ifPresent(player, ExAPI.LEVEL, 0, value -> value.intValue());
	}
	
	@Override
	public Integer readFromNbt(final NbtCompound tag) {
		return tag.getInt("level");
	}
	
	@Override
	public void writeToNbt(final NbtCompound tag, final Object value) {
		tag.putInt("level", (Integer)value);
	}
	
	@Override
	public Identifier id() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof LevelValue)) return false;
		
		LevelValue levelValue = (LevelValue)obj;
		return this.id.equals(levelValue.id);
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
	@Override
	public String toString() {
		return this.id.toString();
	}
}
