package com.github.clevernucleus.playerex.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.util.ExRegistry;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;

import net.minecraft.util.Identifier;

public final class ExRegistryImpl implements ExRegistry {
	private Map<Identifier, IPlayerAttribute> attributes = new HashMap<Identifier, IPlayerAttribute>();
	private Multimap<Identifier, IAttributeFunction> functions = HashMultimap.create();
	
	public ExRegistryImpl() {}
	
	public void put(final Identifier keyIn, IPlayerAttribute attributeIn) {
		this.attributes.put(keyIn, attributeIn);
	}
	
	@Nullable
	@Override
	public IPlayerAttribute getAttribute(final Identifier keyIn) {
		return this.attributes.getOrDefault(keyIn, (IPlayerAttribute)null);
	}
	
	@Override
	public Map<Identifier, IPlayerAttribute> attributes() {
		return ImmutableMap.copyOf(this.attributes);
	}
	
	@Override
	public Collection<IAttributeFunction> functions(final Identifier keyIn) {
		return ImmutableList.copyOf(this.functions.get(keyIn));
	}
	
	@Override
	public void registerFunction(final Identifier keyIn, final IAttributeFunction functionIn) {
		this.functions.put(keyIn, functionIn);
	}
}
