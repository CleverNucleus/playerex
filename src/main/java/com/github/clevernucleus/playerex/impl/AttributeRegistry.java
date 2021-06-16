package com.github.clevernucleus.playerex.impl;

import java.util.HashMap;
import java.util.Map;

import com.ibm.icu.impl.locale.XCldrStub.ImmutableMap;

import net.minecraft.util.Identifier;

public final class AttributeRegistry {
	private Map<Identifier, PlayerAttribute> attributes = new HashMap<Identifier, PlayerAttribute>();
	
	public AttributeRegistry() {}
	
	public void put(final Identifier identifier, PlayerAttribute attribute) {
		this.attributes.put(identifier, attribute);
	}
	
	public Map<Identifier, PlayerAttribute> attributes() {
		return ImmutableMap.copyOf(this.attributes);
	}
}
