package com.github.clevernucleus.playerex.api.damage;

/**
 * This is where damage modifiers are registered.
 * 
 * @author CleverNucleus
 *
 */
public final class DamageModificationRegistry {
	
	/**
	 * See {@link DamagePredicate#test(net.minecraft.entity.LivingEntity, net.minecraft.entity.damage.DamageSource, float)}
	 * and {@link DamageFunction#apply(net.minecraft.entity.LivingEntity, net.minecraft.entity.damage.DamageSource, float)}
	 * @param predicate
	 * @param function
	 */
	public static void register(final DamagePredicate predicate, final DamageFunction function) {
		com.github.clevernucleus.playerex.impl.DamageModificationImpl.add(predicate, function);
	}
}
