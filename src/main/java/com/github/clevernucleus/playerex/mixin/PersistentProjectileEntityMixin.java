package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;

@Mixin(PersistentProjectileEntity.class)
abstract class PersistentProjectileEntityMixin {
	
	@Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;isCritical()Z"))
	private void playerex_onEntityHit(EntityHitResult entityHitResult, CallbackInfo info) {
		PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity)(Object)this;
		Entity owner = persistentProjectileEntity.getOwner();
		
		if(owner instanceof LivingEntity) {
			DataAttributesAPI.ifPresent((LivingEntity)owner, ExAPI.RANGED_CRIT_CHANCE, (Object)null, value -> {
				persistentProjectileEntity.setCritical(false);
				return (Object)null;
			});
		}
	}
	
	@ModifyArg(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = -1)
	private float playerex_onEntityHit(float i) {
		PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity)(Object)this;
		Entity owner = persistentProjectileEntity.getOwner();
		float damage = 0;
		
		if(owner instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity)owner;
			damage = DataAttributesAPI.ifPresent(livingEntity, ExAPI.RANGED_DAMAGE, i, value -> (float)(value + i));
			
			final float amount = damage;
			
			if(DataAttributesAPI.ifPresent(livingEntity, ExAPI.RANGED_CRIT_CHANCE, persistentProjectileEntity.isCritical(), value -> {
				boolean cache = livingEntity.getRandom().nextFloat() < value;
				persistentProjectileEntity.setCritical(cache);
				return cache;
			})) {
				damage = DataAttributesAPI.ifPresent(livingEntity, ExAPI.RANGED_CRIT_DAMAGE, amount, value -> (float)(amount * (1.0 + (10.0 * value))));
			}
		}
		
		return damage;
	}
}
