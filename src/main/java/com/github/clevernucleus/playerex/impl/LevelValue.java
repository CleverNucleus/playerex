package com.github.clevernucleus.playerex.impl;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.opc.api.CacheableValue;
import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class LevelValue extends CacheableValue<Integer> {
	public LevelValue() {
		super(new Identifier(ExAPI.MODID, "level"));
	}
	
	@Override
	public Integer get(final ServerPlayerEntity player) {
		return DataAttributesAPI.ifPresent(player, ExAPI.LEVEL, 0, value -> Math.round(value));
	}
	
	@Override
	public Integer readFromNbt(NbtCompound tag) {
		return tag.getInt("level");
	}
	
	@Override
	public void writeToNbt(NbtCompound tag, Object value) {
		tag.putInt("level", (Integer)value);
	}
}
