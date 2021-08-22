package com.github.clevernucleus.playerex.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.HitResult;

@Mixin(ProjectileEntity.class)
abstract class ProjectileEntityMixin {
	
	@Inject(method = "onCollision", at = @At("HEAD"))
	private void onOnCollision(HitResult hitResult, CallbackInfo info) {
		HitResult.Type type = hitResult.getType();
		
		if(type == HitResult.Type.ENTITY) {
			ProjectileEntity projectile = (ProjectileEntity)(Object)this;
			
			if(projectile instanceof PersistentProjectileEntity) {
				PersistentProjectileEntity arrow = (PersistentProjectileEntity)projectile;
				Entity entity = arrow.getOwner();
				
				if(entity instanceof LivingEntity) {
					LivingEntity living = (LivingEntity)entity;
					AttributeContainer container = living.getAttributes();
					EntityAttribute chance = ExAPI.RANGED_CRIT_CHANCE.get();
					EntityAttribute damage = ExAPI.RANGED_CRIT_DAMAGE.get();
					EntityAttribute bonus = ExAPI.RANGED_DAMAGE.get();
					double result = arrow.getDamage();
					
					boolean isCrit = false;
					
					if(container.hasAttribute(chance)) {
						float value = (float)container.getValue(chance);
						float crit = (new Random()).nextFloat();
						isCrit = crit < value;
						
						arrow.setCritical(isCrit);
					}
					
					if(container.hasAttribute(bonus)) {
						double value = container.getValue(bonus);
						result = result + value;
					}
					
					if(isCrit && container.hasAttribute(damage)) {
						double value = container.getValue(damage);
						result = result * (1.0D + value);
					}
					
					arrow.setDamage(result);
				}
			}
		}
	}
}
