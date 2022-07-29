package com.github.clevernucleus.playerex.factory;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttribute;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.github.clevernucleus.playerex.config.ConfigImpl;
import com.github.clevernucleus.playerex.impl.DamageModificationImpl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public final class EventFactory {
	public static void serverStarting(final MinecraftServer server) {
		((ConfigImpl)ExAPI.getConfig()).createServerConfig();
	}
	
	public static void reset(final ServerPlayerEntity oldPlayer, final ServerPlayerEntity newPlayer, final boolean isAlive) {
		if(ExAPI.getConfig().resetOnDeath()) {
			PlayerData playerData = ExAPI.PLAYER_DATA.get(newPlayer);
			playerData.reset();
		}
	}
	
	public static double clamped(final EntityAttribute attributeIn, final double valueIn) {
		IEntityAttribute attribute = (IEntityAttribute)attributeIn;
		
		if(!attribute.hasProperty(ExAPI.INTEGER_PROPERTY)) return valueIn;
		return Math.round((float)valueIn);
	}
	
	public static float healed(final LivingEntity livingEntity, final float amount) {
		return DataAttributesAPI.ifPresent(livingEntity, ExAPI.HEAL_AMPLIFICATION, amount, value -> {
			return (float)(amount * (1.0 + (value * 10.0)));
		});
	}
	
	public static void healthRegeneration(final LivingEntity livingEntity) {
		if(!livingEntity.world.isClient) {
			DataAttributesAPI.ifPresent(livingEntity, ExAPI.HEALTH_REGENERATION, (Object)null, value -> {
				if(value > 0.0 && livingEntity.getHealth() < livingEntity.getMaxHealth()) {
					livingEntity.heal((float)(double)value);
				}
				
				return (Object)null;
			});
		}
	}
	
	public static float onDamage(final LivingEntity livingEntity, final DamageSource source, final float original) {
		float amount = original;
		
		for(var condition : DamageModificationImpl.get()) {
			float damage = amount;
			
			amount = condition.provide((predicate, function) -> {
				if(predicate.test(livingEntity, source, damage)) {
					return function.apply(livingEntity, source, damage);
				} else {
					return damage;
				}
			});
		}
		
		return amount;
	}
	
	public static boolean shouldDamage(final LivingEntity livingEntity, final DamageSource source, final float original) {
		if(original == 0.0F) return true;
		Entity origin = source.getSource();
		Entity attacker = source.getAttacker();
		
		if(attacker instanceof LivingEntity && (origin instanceof LivingEntity || origin instanceof PersistentProjectileEntity)) {
			LivingEntity user = (LivingEntity)attacker;
			DataAttributesAPI.ifPresent(user, ExAPI.LIFESTEAL, (Object)null, value -> {
				user.heal((float)(original * value * 10.0));
				return (Object)null;
			});
		}
		
		return DataAttributesAPI.ifPresent(livingEntity, ExAPI.EVASION, true, value -> {
			float chance = livingEntity.getRandom().nextFloat();
			return !(chance < value && origin instanceof PersistentProjectileEntity);
		});
	}
	
	public static float onCritAttack(final PlayerEntity player, final Entity target, final float amount) {
		if(!(target instanceof LivingEntity)) return amount;
		return DataAttributesAPI.ifPresent(player, ExAPI.MELEE_CRIT_DAMAGE, amount, value -> {
			return (float)(amount * (1.0 + (value * 10.0)) / 1.5);
		});
	}
	
	public static boolean attackIsCrit(final PlayerEntity player, final Entity target, final boolean vanilla) {
		if(!(target instanceof LivingEntity)) return vanilla;
		return DataAttributesAPI.ifPresent(player, ExAPI.MELEE_CRIT_CHANCE, vanilla, value -> {
			float chance = player.getRandom().nextFloat();
			return (chance < value) && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle();
		});
	}
}
