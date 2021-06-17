package com.github.clevernucleus.playerex.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.handler.EventHandler;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.HitResult;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {
	protected Random playerex$random = new Random();
	
	@Inject(method = "onCollision", at = @At("HEAD"))
	private void onCollision(HitResult hitResult, CallbackInfo info) {
		HitResult.Type type = hitResult.getType();
		
		if(type == HitResult.Type.ENTITY) {
			EventHandler.onArrowHit((ProjectileEntity)(Object)this, this.playerex$random);
		}
	}
}
