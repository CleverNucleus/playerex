package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.playerex.api.event.LivingEntityEvents;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
	
	@Unique
	private int playerex_ticks;
	
	@ModifyVariable(method = "heal", at = @At("HEAD"))
	private float playerex_heal(float amount) {
		return LivingEntityEvents.ON_HEAL.invoker().onHeal((LivingEntity)(Object)this, amount);
	}
	
	@Inject(method = "heal", at = @At("HEAD"), cancellable = true)
	private void playerex_heal(float amount, CallbackInfo info) {
		final boolean cancel = LivingEntityEvents.SHOULD_HEAL.invoker().shouldHeal((LivingEntity)(Object)this, amount);
		
		if(!cancel) {
			info.cancel();
		}
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void playerex_tick(CallbackInfo info) {
		LivingEntity livingEntity = (LivingEntity)(Object)this;
		
		if(this.playerex_ticks < 20) {
			this.playerex_ticks++;
		} else {
			LivingEntityEvents.EVERY_SECOND.invoker().everySecond(livingEntity);
			this.playerex_ticks = 0;
		}
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0)
	private float playerex_damage(float amount, DamageSource source) {
		return LivingEntityEvents.ON_DAMAGE.invoker().onDamage((LivingEntity)(Object)this, source, amount);
	}
	
	@Inject(method = "damage", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;despawnCounter:I", ordinal = 0), cancellable = true)
	private void playerex_damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		final boolean cancel = LivingEntityEvents.SHOULD_DAMAGE.invoker().shouldDamage((LivingEntity)(Object)this, source, amount);
		
		if(!cancel) {
			info.setReturnValue(false);
		}
	}
}
