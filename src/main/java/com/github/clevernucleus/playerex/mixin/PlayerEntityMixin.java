package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.clevernucleus.playerex.api.event.PlayerEntityEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin {
	
	@Unique
	private PlayerEntity get() {
		return (PlayerEntity)(Object)this;
	}
	
	@ModifyVariable(method = "attack", at = @At("STORE"), name = "bl3", ordinal = 2)
	private boolean onAttackCritBoolean(boolean bl3, Entity target) {
		return PlayerEntityEvents.ATTACK_CRIT_BOOLEAN.invoker().shouldCrit(this.get(), target, bl3);
	}
	
	@ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 2), name = "f", ordinal = 0)
	private float onAttackCritDamage(float f, Entity target) {
		return PlayerEntityEvents.ATTACK_CRIT_DAMAGE.invoker().onCritDamage(this.get(), target, f);
	}
}
