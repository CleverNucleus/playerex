package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.playerex.handler.EventHandler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	
	@Inject(method = "heal", at = @At("HEAD"))
	private void onHeal(float amount, CallbackInfo info) {
		EventHandler.onHeal((LivingEntity)(Object)this, amount);
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		boolean result = EventHandler.onDamage((LivingEntity)(Object)this, source, amount);
		
		if(result) {
			info.setReturnValue(false);
		}
	}
}
