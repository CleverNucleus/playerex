package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;

@Mixin(PlayerInventory.class)
abstract class PlayerInventoryMixin {
	
	@Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
	private void playerex_getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> info) {
		float original = info.getReturnValue();
		float result = DataAttributesAPI.ifPresent(((PlayerInventory)(Object)this).player, ExAPI.BREAKING_SPEED, original, value -> {
			return (float)(original + value - 1.0);
		});
		
		info.setReturnValue(result);
	}
}
