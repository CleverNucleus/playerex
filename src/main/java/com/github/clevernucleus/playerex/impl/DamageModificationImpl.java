package com.github.clevernucleus.playerex.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.github.clevernucleus.playerex.api.damage.DamageFunction;
import com.github.clevernucleus.playerex.api.damage.DamagePredicate;

public final class DamageModificationImpl {
	private static final List<Function<BiFunction<DamagePredicate, DamageFunction, Float>, Float>> REGISTRY = new ArrayList<Function<BiFunction<DamagePredicate, DamageFunction, Float>, Float>>();
	
	public static void add(final DamagePredicate predicate, final DamageFunction function) {
		REGISTRY.add(registry -> registry.apply(predicate, function));
	}
	
	public static List<Function<BiFunction<DamagePredicate, DamageFunction, Float>, Float>> get() {
		return REGISTRY;
	}
}
