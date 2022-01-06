package com.github.clevernucleus.playerex.api.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

/**
 * 
 * @author Another TriFunction.
 *
 */
@FunctionalInterface
public interface DamagePredicate {
	
	/**
	 * Determines, using the input conditions, whether to apply the DamageFunction to the incoming damage of the LivingEntity.
	 * @param livingEntity
	 * @param source
	 * @param damage
	 * @return
	 */
	boolean test(final LivingEntity livingEntity, final DamageSource source, final float damage);
}
