package com.github.clevernucleus.playerex.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import net.minecraft.util.Identifier;


public final class ExRegistry {
	private Set<Identifier> attributes = new HashSet<Identifier>();
	private Multimap<Identifier, IAttributeFunction> functions = HashMultimap.create();
	
	protected ExRegistry() {}
	
	
	public Collection<Identifier> attributes() {
		return ImmutableSet.copyOf(this.attributes);
	}
	
	
	public Collection<IAttributeFunction> functions(final Identifier id) {
		return ImmutableList.copyOf(this.functions.get(id));
	}
	
	
	public void registerFunction(final Identifier id, final IAttributeFunction function) {
		this.functions.put(id, function);
	}
}
