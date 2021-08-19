package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.handler.EventHandler;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin {
	
	@Inject(method = "addExperience", at = @At("TAIL"))
	private void onAddExperience(int experience, CallbackInfo info) {
		EventHandler.experienceAdded((PlayerEntity)(Object)this, experience);
	}
}
