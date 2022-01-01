package com.github.clevernucleus.playerex.api.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;


@FunctionalInterface
public interface DamageFunction {
	
	
	float apply(final LivingEntity livingEntity, final DamageSource source, final float damage);
}
