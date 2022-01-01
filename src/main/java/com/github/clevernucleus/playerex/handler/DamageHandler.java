package com.github.clevernucleus.playerex.handler;

import java.util.function.BiConsumer;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.damage.DamageFunction;
import com.github.clevernucleus.playerex.api.damage.DamagePredicate;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.PotionEntity;

public final class DamageHandler {
	public static final DamageHandler STORE = new DamageHandler();
	
	private DamageHandler() {}
	
	public void forEach(BiConsumer<DamagePredicate, DamageFunction> registry) {
		registry.accept((living, source, damage) -> source.isFire(), (living, source, damage) -> DataAttributesAPI.ifPresent(living, ExAPI.FIRE_RESISTANCE, damage, value -> damage * (1.0F - value)));
		registry.accept((living, source, damage) -> source.equals(DamageSource.FREEZE), (living, source, damage) -> DataAttributesAPI.ifPresent(living, ExAPI.COLD_RESISTANCE, damage, value -> damage * (1.0F - value)));
		registry.accept((living, source, damage) -> source.equals(DamageSource.LIGHTNING_BOLT), (living, source, damage) -> DataAttributesAPI.ifPresent(living, ExAPI.LIGHTNING_RESISTANCE, damage, value -> damage * (1.0F - value)));registry.accept((living, source, damage) -> living.hasStatusEffect(StatusEffects.POISON) && source.isMagic() && damage <= 1.0F, (living, source, damage) -> DataAttributesAPI.ifPresent(living, ExAPI.POISON_RESISTANCE, damage, value -> damage * (1.0F - value)));
		registry.accept((living, source, damage) -> living.hasStatusEffect(StatusEffects.POISON) && source.isMagic() && damage <= 1.0F, (living, source, damage) -> DataAttributesAPI.ifPresent(living, ExAPI.POISON_RESISTANCE, damage, value -> damage * (1.0F - value)));
		registry.accept((living, source, damage) -> source.equals(DamageSource.WITHER) || (source.name.equals("indirectMagic") && source.getSource() instanceof PotionEntity), (living, source, damage) -> {
			return DataAttributesAPI.ifPresent(living, ExAPI.WITHER_RESISTANCE, damage, value -> {
				if(source.equals(DamageSource.WITHER) && living.isUndead()) return 0.0F;
				if(source.name.equals("indirectMagic") && source.getSource() instanceof PotionEntity && living.isUndead()) return damage;
				
				return damage * (1.0F - value);
			});
		});
	}
}
