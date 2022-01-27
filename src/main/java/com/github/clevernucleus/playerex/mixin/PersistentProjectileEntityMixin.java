package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

@Mixin(PersistentProjectileEntity.class)
abstract class PersistentProjectileEntityMixin extends Entity {
	private PersistentProjectileEntityMixin(EntityType<?> type, World world) { super(type, world); }
	
	@Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;isCritical()Z"))
	private void preOnEntityHit(EntityHitResult entityHitResult, CallbackInfo info) {
		PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity)(Object)this;
		Entity owner = persistentProjectileEntity.getOwner();
		
		if(owner instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity)owner;
			DataAttributesAPI.ifPresent(livingEntity, ExAPI.RANGED_CRIT_CHANCE, (Object)null, value -> {
				persistentProjectileEntity.setCritical(false);
				return (Object)null;
			});
		}
	}
	
	@ModifyArg(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = -1)
	private float onDamage(float i) {
		PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity)(Object)this;
		Entity owner = persistentProjectileEntity.getOwner();
		float damage = 0;
		
		if(owner instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity)owner;
			damage = DataAttributesAPI.ifPresent(livingEntity, ExAPI.RANGED_DAMAGE, i, value -> value + i);
			
			final float amount = damage;
			
			if(DataAttributesAPI.ifPresent(livingEntity, ExAPI.RANGED_CRIT_CHANCE, persistentProjectileEntity.isCritical(), value -> {
				boolean cache = this.random.nextFloat() < value;
				persistentProjectileEntity.setCritical(cache);
				return cache;
			})) {
				damage = DataAttributesAPI.ifPresent(livingEntity, ExAPI.RANGED_CRIT_DAMAGE, amount, value -> amount * (1.0F + (10.0F * value)));
			}
		}
		
		return damage;
	}
}
