package com.github.clevernucleus.playerex.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import com.github.clevernucleus.playerex.api.damage.DamageFunction;
import com.github.clevernucleus.playerex.api.damage.DamagePredicate;

public final class DamageModificationImpl {
	private static final List<DamageModification> REGISTRY = new ArrayList<>();
	
	public static void add(final DamagePredicate predicate, final DamageFunction function) {
		REGISTRY.add(registry -> registry.apply(predicate, function));
	}
	
	public static Collection<DamageModification> get() {
		return REGISTRY;
	}
	
	@FunctionalInterface
	public interface DamageModification {
		float provide(BiFunction<DamagePredicate, DamageFunction, Float> provider);
	}
}
