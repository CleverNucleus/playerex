package com.github.clevernucleus.playerex.mixin;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.util.StoredResistance;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.PotionEntity;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
	private static final Set<StoredResistance> RESISTANCES = new HashSet<StoredResistance>();
	
	static {
		RESISTANCES.add(new StoredResistance("fire", ExAPI.FIRE_RESISTANCE, s -> s.isFire()));
		RESISTANCES.add(new StoredResistance("cold", ExAPI.FREEZE_RESISTANCE, s -> s.equals(DamageSource.FREEZE)));
		RESISTANCES.add(new StoredResistance("drowning", ExAPI.DROWNING_RESISTANCE, s -> s.equals(DamageSource.DROWN)));
		RESISTANCES.add(new StoredResistance("falling", ExAPI.FALLING_RESISTANCE, s -> s.isFromFalling()));
		RESISTANCES.add(new StoredResistance("wither", ExAPI.WITHER_RESISTANCE, s -> s.equals(DamageSource.WITHER)));
		RESISTANCES.add(new StoredResistance("magic", ExAPI.MAGIC_RESISTANCE, s -> s.isMagic()));
	}
	
	protected LivingEntity isMagicAmp(DamageSource source) {
		Entity entity = source.getSource();
		
		if(entity instanceof LivingEntity) return (LivingEntity)entity;
		if(entity instanceof PotionEntity) {
			PotionEntity potion = (PotionEntity)entity;
			Entity origin = potion.getOwner();
			
			if(origin instanceof LivingEntity) return (LivingEntity)origin;
		}
		
		return null;
	}
	
	@ModifyVariable(method = "heal", at = @At("HEAD"))
	private float onHeal(float amount) {
		LivingEntity entity = (LivingEntity)(Object)this;
		AttributeContainer container = entity.getAttributes();
		EntityAttribute attribute = ExAPI.HEAL_AMPLIFICATION.get();
		
		if(!container.hasAttribute(attribute)) return amount;
		float mult = (float)container.getValue(attribute);
		
		return amount * (1.0F + mult);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo info) {
		LivingEntity entity = (LivingEntity)(Object)this;
		
		if(entity.world.isClient) return;
		
		AttributeContainer attributes = entity.getAttributes();
		EntityAttribute attribute = ExAPI.HEALTH_REGENERATION.get();
		
		if(!attributes.hasAttribute(attribute)) return;
		
		float value = (float)attributes.getValue(attribute);
		
		if(value > 0.0F) {
			entity.heal(value);
		} else if(value < 0.0F) {
			entity.damage(DamageSource.MAGIC, value);
		}
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0)
	private float onDamageAmount(float amount, DamageSource source) {
		LivingEntity living = (LivingEntity)(Object)this;
		LivingEntity entity = this.isMagicAmp(source);
		AttributeContainer container = living.getAttributes();
		float after = amount;
		
		if(entity != null) {
			AttributeContainer attributes = entity.getAttributes();
			
			if(source.isMagic()) {
				EntityAttribute attribute = ExAPI.MAGIC_AMPLIFICATION.get();
				
				if(attributes.hasAttribute(attribute)) {
					float value = (float)attributes.getValue(attribute);
					after = after * (1.0F + value);
				}
			}
		}
		
		for(StoredResistance ref : RESISTANCES) {
			if(!ref.isValid(container, source)) continue;
			return after * (1.0F - ref.result(container));
		}
		
		return after;
	}
	
	@Inject(method = "damage", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;despawnCounter:I", ordinal = 0), cancellable = true)
	private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		LivingEntity entity = (LivingEntity)(Object)this;
		AttributeContainer container = entity.getAttributes();
		EntityAttribute evasion = ExAPI.EVASION.get();
		
		if(source.isProjectile() && container.hasAttribute(evasion)) {
			float value = (float)container.getValue(evasion);
			float evade = (new Random()).nextFloat();
			
			if(evade < value) {
				info.setReturnValue(false);
			}
		}
		
		if(source.getSource() instanceof LivingEntity) {
			LivingEntity origin = (LivingEntity)source.getSource();
			AttributeContainer attributes = origin.getAttributes();
			EntityAttribute lifesteal = ExAPI.LIFESTEAL.get();
			
			if(attributes.hasAttribute(lifesteal)) {
				float value = (float)attributes.getValue(lifesteal);
				
				origin.heal(amount * value);
			}
		}
	}
}
