package com.github.clevernucleus.playerex.factory;

import java.util.function.BiConsumer;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.damage.DamageFunction;
import com.github.clevernucleus.playerex.api.damage.DamagePredicate;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.PotionEntity;

import net.minecraft.entity.damage.DamageTypes;

public final class DamageFactory {
	public static final DamageFactory STORE = new DamageFactory();
	
	private DamageFactory() {}
	
	public void forEach(BiConsumer<DamagePredicate, DamageFunction> registry) {
		registry.accept((living, source, damage) -> source.isOf(DamageTypes.ON_FIRE), (living, source, damage) -> DataAttributesAPI.ifPresent(living, ExAPI.FIRE_RESISTANCE, damage, value -> (float)(damage * (1.0 - value))));
		registry.accept((living, source, damage) -> source.isOf(DamageTypes.FREEZE), (living, source, damage) -> DataAttributesAPI.ifPresent(living, ExAPI.FREEZE_RESISTANCE, damage, value -> (float)(damage * (1.0 - value))));
		registry.accept((living, source, damage) -> source.isOf(DamageTypes.LIGHTNING_BOLT), (living, source, damage) -> DataAttributesAPI.ifPresent(living, ExAPI.LIGHTNING_RESISTANCE, damage, value -> (float)(damage * (1.0 - value))));
		registry.accept((living, source, damage) -> living.hasStatusEffect(StatusEffects.POISON) && source.isOf(DamageTypes.MAGIC) && damage <= 1.0F, (living, source, damage) -> DataAttributesAPI.ifPresent(living, ExAPI.FREEZE_RESISTANCE, damage, value -> (float)(damage * (1.0 - value))));
		registry.accept((living, source, damage) -> source.isOf(DamageTypes.WITHER) || (source.isIndirect() && source.isOf(DamageTypes.MAGIC) && (source.getSource() instanceof PotionEntity || source.getSource() instanceof AreaEffectCloudEntity)), (living, source, damage) -> {
			return DataAttributesAPI.ifPresent(living, ExAPI.WITHER_RESISTANCE, damage, value -> {
				if(source.isOf(DamageTypes.WITHER) && living.isUndead()) return 0.0F;
				if(source.isIndirect() && source.isOf(DamageTypes.MAGIC) && source.getSource() instanceof PotionEntity && living.isUndead()) return damage;
				return (float)(damage * (1.0 - value));
			});
		});
	}
}
