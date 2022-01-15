package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.event.PlayerEntityEvents;

import net.minecraft.block.BlockState;
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
	
	@Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
	private void onGetBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> info) {
		float f = info.getReturnValue();
		float r = DataAttributesAPI.ifPresent(this.get(), ExAPI.BREAKING_SPEED, 0.0F, value -> {
			float base = (float)this.get().getAttributeInstance(ExAPI.BREAKING_SPEED.get()).getBaseValue();
			return value - base;
		});
		
		info.setReturnValue(f + r);
	}
}
