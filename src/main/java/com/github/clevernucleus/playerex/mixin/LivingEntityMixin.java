package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.handler.EventHandler;

import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
	
	@Inject(method = "heal", at = @At("HEAD"))
	private void onHeal(float amount, CallbackInfo info) {
		EventHandler.healed((LivingEntity)(Object)this, amount);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo info) {
		EventHandler.ticked((LivingEntity)(Object)this);
	}
}
