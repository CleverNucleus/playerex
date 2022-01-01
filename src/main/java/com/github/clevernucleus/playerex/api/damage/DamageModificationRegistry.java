package com.github.clevernucleus.playerex.api.damage;


public final class DamageModificationRegistry {
	
	
	public static void register(final DamagePredicate predicate, final DamageFunction function) {
		com.github.clevernucleus.playerex.impl.DamageModificationImpl.add(predicate, function);
	}
}
