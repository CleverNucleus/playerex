package com.github.clevernucleus.playerex.impl;

import java.util.Map;
import java.util.UUID;

import com.google.gson.annotations.Expose;

import net.minecraft.util.Identifier;

public final class ModifiersJson {
	@Expose private Map<String, UUID> values;
	
	private ModifiersJson() {}
	
	public void put(final Map<Identifier, UUID> modifiers) {
		for(String key : this.values.keySet()) {
			Identifier identifier = new Identifier(key);
			UUID uuid = this.values.get(key);
			modifiers.putIfAbsent(identifier, uuid);
		}
	}
}
