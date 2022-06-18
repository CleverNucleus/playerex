package com.github.clevernucleus.playerex.api.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

/**
 * Basically just a dedicated TriFunction
 * 
 * @author CleverNucleus
 *
 */
@FunctionalInterface
public interface DamageFunction {
	
	/**
	 * Using the input conditions, modifies the incoming damage (either reducing or increasing it) and returns the result.
	 * @param livingEntity
	 * @param source
	 * @param damage
	 * @return
	 */
	float apply(final LivingEntity livingEntity, final DamageSource source, final float damage);
}
